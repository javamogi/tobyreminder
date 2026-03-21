package toby.ai.tobyreminder.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.dto.ReminderListRequest;
import toby.ai.tobyreminder.dto.ReminderListResponse;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;
import toby.ai.tobyreminder.repository.ReminderListRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderListServiceTest {

    @Autowired
    private ReminderListService reminderListService;

    @Autowired
    private ReminderListRepository reminderListRepository;

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("리스트를 생성하고 응답을 반환한다")
        void createList() {
            ReminderListRequest request = new ReminderListRequest("개인", "#FF3B30", "star");

            ReminderListResponse response = reminderListService.create(request);

            assertThat(response.id()).isNotNull();
            assertThat(response.name()).isEqualTo("개인");
            assertThat(response.color()).isEqualTo("#FF3B30");
            assertThat(response.icon()).isEqualTo("star");
        }

        @Test
        @DisplayName("색상, 아이콘 미지정 시 기본값으로 생성된다")
        void createWithDefaults() {
            ReminderListRequest request = new ReminderListRequest("업무", null, null);

            ReminderListResponse response = reminderListService.create(request);

            assertThat(response.color()).isEqualTo("#007AFF");
            assertThat(response.icon()).isEqualTo("list.bullet");
        }

        @Test
        @DisplayName("displayOrder가 기존 리스트 수로 설정된다")
        void createWithNextDisplayOrder() {
            reminderListService.create(new ReminderListRequest("첫번째", null, null));
            reminderListService.create(new ReminderListRequest("두번째", null, null));

            ReminderListResponse third = reminderListService.create(new ReminderListRequest("세번째", null, null));

            assertThat(third.displayOrder()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("모든 리스트를 displayOrder 순으로 반환한다")
        void findAllOrderByDisplayOrder() {
            reminderListService.create(new ReminderListRequest("개인", null, null));
            reminderListService.create(new ReminderListRequest("업무", null, null));

            List<ReminderListResponse> result = reminderListService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).name()).isEqualTo("개인");
            assertThat(result.get(1).name()).isEqualTo("업무");
        }

        @Test
        @DisplayName("리스트가 없으면 빈 목록을 반환한다")
        void findAllEmpty() {
            List<ReminderListResponse> result = reminderListService.findAll();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("ID로 리스트를 조회한다")
        void findById() {
            ReminderListResponse created = reminderListService.create(new ReminderListRequest("개인", "#FF3B30", "star"));

            ReminderListResponse response = reminderListService.findById(created.id());

            assertThat(response.name()).isEqualTo("개인");
            assertThat(response.color()).isEqualTo("#FF3B30");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 예외가 발생한다")
        void findByIdNotFound() {
            assertThatThrownBy(() -> reminderListService.findById(999L))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("리스트 이름, 색상, 아이콘을 수정한다")
        void updateList() {
            ReminderListResponse created = reminderListService.create(new ReminderListRequest("개인", null, null));

            ReminderListResponse response = reminderListService.update(created.id(),
                    new ReminderListRequest("업무", "#FF3B30", "briefcase"));

            assertThat(response.name()).isEqualTo("업무");
            assertThat(response.color()).isEqualTo("#FF3B30");
            assertThat(response.icon()).isEqualTo("briefcase");
        }

        @Test
        @DisplayName("색상과 아이콘이 null이면 기존 값을 유지한다")
        void updateWithNullKeepsExisting() {
            ReminderListResponse created = reminderListService.create(new ReminderListRequest("개인", "#FF9500", "star"));

            ReminderListResponse response = reminderListService.update(created.id(),
                    new ReminderListRequest("개인 수정", null, null));

            assertThat(response.name()).isEqualTo("개인 수정");
            assertThat(response.color()).isEqualTo("#FF9500");
            assertThat(response.icon()).isEqualTo("star");
        }

        @Test
        @DisplayName("존재하지 않는 리스트를 수정하면 예외가 발생한다")
        void updateNotFound() {
            assertThatThrownBy(() -> reminderListService.update(999L,
                    new ReminderListRequest("업무", null, null)))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("리스트를 삭제한다")
        void deleteList() {
            ReminderListResponse created = reminderListService.create(new ReminderListRequest("개인", null, null));

            reminderListService.delete(created.id());

            assertThat(reminderListRepository.findById(created.id())).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 리스트를 삭제하면 예외가 발생한다")
        void deleteNotFound() {
            assertThatThrownBy(() -> reminderListService.delete(999L))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }
}
