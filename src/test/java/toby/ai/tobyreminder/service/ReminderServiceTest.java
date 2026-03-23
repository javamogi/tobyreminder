package toby.ai.tobyreminder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.domain.Priority;
import toby.ai.tobyreminder.dto.ReminderListRequest;
import toby.ai.tobyreminder.dto.ReminderListResponse;
import toby.ai.tobyreminder.dto.ReminderRequest;
import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;
import toby.ai.tobyreminder.service.ports.in.ReminderService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderListService reminderListService;

    private Long listId;

    @BeforeEach
    void setUp() {
        ReminderListResponse list = reminderListService.create(new ReminderListRequest("개인", null, null));
        listId = list.id();
    }

    private ReminderRequest simpleRequest(String title, String notes) {
        return new ReminderRequest(title, notes, null, null, null, null);
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("리마인더를 생성한다")
        void createReminder() {
            ReminderResponse response = reminderService.create(listId, simpleRequest("장보기", "우유, 빵"));

            assertThat(response.id()).isNotNull();
            assertThat(response.title()).isEqualTo("장보기");
            assertThat(response.notes()).isEqualTo("우유, 빵");
            assertThat(response.listId()).isEqualTo(listId);
            assertThat(response.completed()).isFalse();
            assertThat(response.priority()).isEqualTo(Priority.NONE);
            assertThat(response.flagged()).isFalse();
        }

        @Test
        @DisplayName("모든 속성을 포함하여 생성한다")
        void createWithAllFields() {
            ReminderRequest request = new ReminderRequest("장보기", "우유",
                    LocalDate.of(2026, 3, 25), LocalTime.of(9, 0), Priority.HIGH, true);
            ReminderResponse response = reminderService.create(listId, request);

            assertThat(response.dueDate()).isEqualTo(LocalDate.of(2026, 3, 25));
            assertThat(response.dueTime()).isEqualTo(LocalTime.of(9, 0));
            assertThat(response.priority()).isEqualTo(Priority.HIGH);
            assertThat(response.flagged()).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 리스트에 생성하면 예외가 발생한다")
        void createWithInvalidListId() {
            assertThatThrownBy(() -> reminderService.create(999L, simpleRequest("장보기", null)))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("displayOrder가 순차 증가한다")
        void createWithIncrementingOrder() {
            reminderService.create(listId, simpleRequest("첫번째", null));
            ReminderResponse second = reminderService.create(listId, simpleRequest("두번째", null));

            assertThat(second.displayOrder()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("findByListId")
    class FindByListId {

        @Test
        @DisplayName("리스트의 미완료 리마인더를 조회한다")
        void findByListId() {
            reminderService.create(listId, simpleRequest("장보기", null));
            reminderService.create(listId, simpleRequest("운동", null));

            List<ReminderResponse> result = reminderService.findByListId(listId);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).title()).isEqualTo("장보기");
        }

        @Test
        @DisplayName("완료된 리마인더는 조회되지 않는다")
        void findByListIdExcludesCompleted() {
            ReminderResponse reminder = reminderService.create(listId, simpleRequest("장보기", null));
            reminderService.toggleComplete(reminder.id());
            reminderService.create(listId, simpleRequest("운동", null));

            List<ReminderResponse> result = reminderService.findByListId(listId);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).title()).isEqualTo("운동");
        }

        @Test
        @DisplayName("존재하지 않는 리스트 조회 시 예외가 발생한다")
        void findByInvalidListId() {
            assertThatThrownBy(() -> reminderService.findByListId(999L))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("ID로 리마인더를 조회한다")
        void findById() {
            ReminderResponse created = reminderService.create(listId, simpleRequest("장보기", "우유"));

            ReminderResponse response = reminderService.findById(created.id());

            assertThat(response.title()).isEqualTo("장보기");
            assertThat(response.notes()).isEqualTo("우유");
        }

        @Test
        @DisplayName("존재하지 않는 ID 조회 시 예외가 발생한다")
        void findByIdNotFound() {
            assertThatThrownBy(() -> reminderService.findById(999L))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("리마인더를 수정한다")
        void updateReminder() {
            ReminderResponse created = reminderService.create(listId, simpleRequest("장보기", "우유"));

            ReminderResponse response = reminderService.update(created.id(),
                    new ReminderRequest("운동하기", "헬스장", LocalDate.of(2026, 4, 1), null, Priority.MEDIUM, true));

            assertThat(response.title()).isEqualTo("운동하기");
            assertThat(response.notes()).isEqualTo("헬스장");
            assertThat(response.dueDate()).isEqualTo(LocalDate.of(2026, 4, 1));
            assertThat(response.priority()).isEqualTo(Priority.MEDIUM);
            assertThat(response.flagged()).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 리마인더 수정 시 예외가 발생한다")
        void updateNotFound() {
            assertThatThrownBy(() -> reminderService.update(999L, simpleRequest("운동", null)))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("리마인더를 삭제한다")
        void deleteReminder() {
            ReminderResponse created = reminderService.create(listId, simpleRequest("장보기", null));

            reminderService.delete(created.id());

            assertThatThrownBy(() -> reminderService.findById(created.id()))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("존재하지 않는 리마인더 삭제 시 예외가 발생한다")
        void deleteNotFound() {
            assertThatThrownBy(() -> reminderService.delete(999L))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("toggleComplete")
    class ToggleComplete {

        @Test
        @DisplayName("미완료 → 완료로 전환한다")
        void toggleToComplete() {
            ReminderResponse created = reminderService.create(listId, simpleRequest("장보기", null));

            ReminderResponse response = reminderService.toggleComplete(created.id());

            assertThat(response.completed()).isTrue();
            assertThat(response.completedAt()).isNotNull();
        }

        @Test
        @DisplayName("완료 → 미완료로 전환한다")
        void toggleToIncomplete() {
            ReminderResponse created = reminderService.create(listId, simpleRequest("장보기", null));
            reminderService.toggleComplete(created.id());

            ReminderResponse response = reminderService.toggleComplete(created.id());

            assertThat(response.completed()).isFalse();
            assertThat(response.completedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("toggleFlag")
    class ToggleFlag {

        @Test
        @DisplayName("플래그를 토글한다")
        void toggleFlag() {
            ReminderResponse created = reminderService.create(listId, simpleRequest("장보기", null));

            ReminderResponse flagged = reminderService.toggleFlag(created.id());
            assertThat(flagged.flagged()).isTrue();

            ReminderResponse unflagged = reminderService.toggleFlag(created.id());
            assertThat(unflagged.flagged()).isFalse();
        }
    }

    @Nested
    @DisplayName("search")
    class Search {

        @Test
        @DisplayName("제목으로 검색한다")
        void searchByTitle() {
            reminderService.create(listId, simpleRequest("장보기", null));
            reminderService.create(listId, simpleRequest("운동하기", null));

            List<ReminderResponse> result = reminderService.search("장보");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).title()).isEqualTo("장보기");
        }

        @Test
        @DisplayName("메모로 검색한다")
        void searchByNotes() {
            reminderService.create(listId, simpleRequest("장보기", "우유와 빵"));
            reminderService.create(listId, simpleRequest("운동", "헬스장"));

            List<ReminderResponse> result = reminderService.search("우유");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).title()).isEqualTo("장보기");
        }
    }

    @Nested
    @DisplayName("reorder")
    class Reorder {

        @Test
        @DisplayName("리마인더 순서를 변경한다")
        void reorderReminders() {
            ReminderResponse first = reminderService.create(listId, simpleRequest("첫번째", null));
            ReminderResponse second = reminderService.create(listId, simpleRequest("두번째", null));

            reminderService.reorderReminders(List.of(second.id(), first.id()));

            List<ReminderResponse> result = reminderService.findByListId(listId);
            assertThat(result.get(0).title()).isEqualTo("두번째");
            assertThat(result.get(1).title()).isEqualTo("첫번째");
        }
    }

    @Nested
    @DisplayName("findByListId with sort")
    class FindByListIdWithSort {

        @Test
        @DisplayName("제목 기준으로 정렬한다")
        void sortByTitle() {
            reminderService.create(listId, simpleRequest("다", null));
            reminderService.create(listId, simpleRequest("가", null));
            reminderService.create(listId, simpleRequest("나", null));

            List<ReminderResponse> result = reminderService.findByListId(listId, "title");

            assertThat(result.get(0).title()).isEqualTo("가");
            assertThat(result.get(1).title()).isEqualTo("나");
            assertThat(result.get(2).title()).isEqualTo("다");
        }
    }
}
