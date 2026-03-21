package toby.ai.tobyreminder.controller;

import org.junit.jupiter.api.BeforeEach;
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
import toby.ai.tobyreminder.dto.ReminderRequest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long listId;

    @BeforeEach
    void setUp() throws Exception {
        String response = mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReminderListRequest("개인", null, null))))
                .andReturn().getResponse().getContentAsString();
        listId = objectMapper.readTree(response).get("id").asLong();
    }

    private Long createReminderAndGetId(String title, String notes) throws Exception {
        String response = mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReminderRequest(title, notes))))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asLong();
    }

    @Nested
    @DisplayName("POST /api/lists/{listId}/reminders")
    class Create {

        @Test
        @DisplayName("201 - 리마인더를 생성한다")
        void createReminder() throws Exception {
            mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReminderRequest("장보기", "우유, 빵"))))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.title").value("장보기"))
                    .andExpect(jsonPath("$.notes").value("우유, 빵"))
                    .andExpect(jsonPath("$.listId").value(listId))
                    .andExpect(jsonPath("$.completed").value(false));
        }

        @Test
        @DisplayName("400 - 제목이 비어있으면 실패한다")
        void createWithBlankTitle() throws Exception {
            mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReminderRequest("", null))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("404 - 존재하지 않는 리스트")
        void createWithInvalidListId() throws Exception {
            mockMvc.perform(post("/api/lists/{listId}/reminders", 999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReminderRequest("장보기", null))))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/lists/{listId}/reminders")
    class FindByListId {

        @Test
        @DisplayName("200 - 리스트의 리마인더를 조회한다")
        void findByListId() throws Exception {
            createReminderAndGetId("장보기", null);
            createReminderAndGetId("운동", null);

            mockMvc.perform(get("/api/lists/{listId}/reminders", listId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        @DisplayName("404 - 존재하지 않는 리스트")
        void findByInvalidListId() throws Exception {
            mockMvc.perform(get("/api/lists/{listId}/reminders", 999))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/reminders/{id}")
    class FindById {

        @Test
        @DisplayName("200 - ID로 리마인더를 조회한다")
        void findById() throws Exception {
            Long id = createReminderAndGetId("장보기", "우유");

            mockMvc.perform(get("/api/reminders/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("장보기"))
                    .andExpect(jsonPath("$.notes").value("우유"));
        }

        @Test
        @DisplayName("404 - 존재하지 않는 ID")
        void findByIdNotFound() throws Exception {
            mockMvc.perform(get("/api/reminders/{id}", 999))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /api/reminders/{id}")
    class Update {

        @Test
        @DisplayName("200 - 리마인더를 수정한다")
        void updateReminder() throws Exception {
            Long id = createReminderAndGetId("장보기", null);

            mockMvc.perform(put("/api/reminders/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReminderRequest("운동하기", "헬스장"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("운동하기"))
                    .andExpect(jsonPath("$.notes").value("헬스장"));
        }

        @Test
        @DisplayName("404 - 존재하지 않는 리마인더")
        void updateNotFound() throws Exception {
            mockMvc.perform(put("/api/reminders/{id}", 999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReminderRequest("운동", null))))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/reminders/{id}")
    class Delete {

        @Test
        @DisplayName("204 - 리마인더를 삭제한다")
        void deleteReminder() throws Exception {
            Long id = createReminderAndGetId("장보기", null);

            mockMvc.perform(delete("/api/reminders/{id}", id))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/api/reminders/{id}", id))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("404 - 존재하지 않는 리마인더")
        void deleteNotFound() throws Exception {
            mockMvc.perform(delete("/api/reminders/{id}", 999))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PATCH /api/reminders/{id}/complete")
    class ToggleComplete {

        @Test
        @DisplayName("200 - 완료 토글한다")
        void toggleComplete() throws Exception {
            Long id = createReminderAndGetId("장보기", null);

            mockMvc.perform(patch("/api/reminders/{id}/complete", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.completed").value(true))
                    .andExpect(jsonPath("$.completedAt").isNotEmpty());
        }

        @Test
        @DisplayName("200 - 완료 해제한다")
        void toggleUncomplete() throws Exception {
            Long id = createReminderAndGetId("장보기", null);
            mockMvc.perform(patch("/api/reminders/{id}/complete", id));

            mockMvc.perform(patch("/api/reminders/{id}/complete", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.completed").value(false))
                    .andExpect(jsonPath("$.completedAt").isEmpty());
        }

        @Test
        @DisplayName("404 - 존재하지 않는 리마인더")
        void toggleNotFound() throws Exception {
            mockMvc.perform(patch("/api/reminders/{id}/complete", 999))
                    .andExpect(status().isNotFound());
        }
    }
}
