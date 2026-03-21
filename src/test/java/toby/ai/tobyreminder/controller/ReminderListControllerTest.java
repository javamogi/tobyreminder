package toby.ai.tobyreminder.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import toby.ai.tobyreminder.dto.ReminderListRequest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String createList(String name, String color, String icon) throws Exception {
        ReminderListRequest request = new ReminderListRequest(name, color, icon);
        return mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
    }

    private Long createListAndGetId(String name) throws Exception {
        String response = createList(name, null, null);
        return objectMapper.readTree(response).get("id").asLong();
    }

    @Nested
    @DisplayName("POST /api/lists")
    class Create {

        @Test
        @DisplayName("201 - 리스트를 생성한다")
        void createList() throws Exception {
            ReminderListRequest request = new ReminderListRequest("개인", "#FF3B30", "star");

            mockMvc.perform(post("/api/lists")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").value("개인"))
                    .andExpect(jsonPath("$.color").value("#FF3B30"))
                    .andExpect(jsonPath("$.icon").value("star"))
                    .andExpect(jsonPath("$.displayOrder").value(0))
                    .andExpect(jsonPath("$.createdAt").isNotEmpty());
        }

        @Test
        @DisplayName("201 - 색상, 아이콘 미지정 시 기본값으로 생성된다")
        void createWithDefaults() throws Exception {
            ReminderListRequest request = new ReminderListRequest("업무", null, null);

            mockMvc.perform(post("/api/lists")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.color").value("#007AFF"))
                    .andExpect(jsonPath("$.icon").value("list.bullet"));
        }

        @Test
        @DisplayName("400 - 이름이 비어있으면 실패한다")
        void createWithBlankName() throws Exception {
            ReminderListRequest request = new ReminderListRequest("", null, null);

            mockMvc.perform(post("/api/lists")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").isNotEmpty());
        }
    }

    @Nested
    @DisplayName("GET /api/lists")
    class FindAll {

        @Test
        @DisplayName("200 - 모든 리스트를 반환한다")
        void findAll() throws Exception {
            createList("개인", null, null);
            createList("업무", null, null);

            mockMvc.perform(get("/api/lists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name").value("개인"))
                    .andExpect(jsonPath("$[1].name").value("업무"));
        }

        @Test
        @DisplayName("200 - 리스트가 없으면 빈 배열을 반환한다")
        void findAllEmpty() throws Exception {
            mockMvc.perform(get("/api/lists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/lists/{id}")
    class FindById {

        @Test
        @DisplayName("200 - ID로 리스트를 조회한다")
        void findById() throws Exception {
            Long id = createListAndGetId("개인");

            mockMvc.perform(get("/api/lists/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.name").value("개인"));
        }

        @Test
        @DisplayName("404 - 존재하지 않는 ID")
        void findByIdNotFound() throws Exception {
            mockMvc.perform(get("/api/lists/{id}", 999))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").isNotEmpty());
        }
    }

    @Nested
    @DisplayName("PUT /api/lists/{id}")
    class Update {

        @Test
        @DisplayName("200 - 리스트를 수정한다")
        void updateList() throws Exception {
            Long id = createListAndGetId("개인");
            ReminderListRequest request = new ReminderListRequest("업무", "#FF3B30", "briefcase");

            mockMvc.perform(put("/api/lists/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("업무"))
                    .andExpect(jsonPath("$.color").value("#FF3B30"))
                    .andExpect(jsonPath("$.icon").value("briefcase"));
        }

        @Test
        @DisplayName("404 - 존재하지 않는 리스트 수정")
        void updateNotFound() throws Exception {
            ReminderListRequest request = new ReminderListRequest("업무", null, null);

            mockMvc.perform(put("/api/lists/{id}", 999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("400 - 이름이 비어있으면 실패한다")
        void updateWithBlankName() throws Exception {
            Long id = createListAndGetId("개인");
            ReminderListRequest request = new ReminderListRequest("", null, null);

            mockMvc.perform(put("/api/lists/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /api/lists/{id}")
    class Delete {

        @Test
        @DisplayName("204 - 리스트를 삭제한다")
        void deleteList() throws Exception {
            Long id = createListAndGetId("개인");

            mockMvc.perform(delete("/api/lists/{id}", id))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/api/lists/{id}", id))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("404 - 존재하지 않는 리스트 삭제")
        void deleteNotFound() throws Exception {
            mockMvc.perform(delete("/api/lists/{id}", 999))
                    .andExpect(status().isNotFound());
        }
    }
}
