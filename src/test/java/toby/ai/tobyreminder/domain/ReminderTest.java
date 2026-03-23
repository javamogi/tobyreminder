package toby.ai.tobyreminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderTest {

    private ReminderList createList() {
        return ReminderList.builder().name("개인").build();
    }

    @Nested
    @DisplayName("생성자")
    class Constructor {

        @Test
        @DisplayName("필수 필드로 생성한다")
        void createWithRequiredFields() {
            ReminderList list = createList();
            Reminder reminder = Reminder.builder()
                    .list(list)
                    .title("장보기")
                    .build();

            assertThat(reminder.getTitle()).isEqualTo("장보기");
            assertThat(reminder.getList()).isEqualTo(list);
            assertThat(reminder.isCompleted()).isFalse();
            assertThat(reminder.getPriority()).isEqualTo(Priority.NONE);
            assertThat(reminder.isFlagged()).isFalse();
            assertThat(reminder.getDueDate()).isNull();
            assertThat(reminder.getDueTime()).isNull();
            assertThat(reminder.getDisplayOrder()).isZero();
        }

        @Test
        @DisplayName("모든 필드를 포함하여 생성한다")
        void createWithAllFields() {
            Reminder reminder = Reminder.builder()
                    .list(createList())
                    .title("장보기")
                    .notes("우유, 빵")
                    .dueDate(LocalDate.of(2026, 3, 25))
                    .dueTime(LocalTime.of(9, 0))
                    .priority(Priority.HIGH)
                    .flagged(true)
                    .displayOrder(3)
                    .build();

            assertThat(reminder.getNotes()).isEqualTo("우유, 빵");
            assertThat(reminder.getDueDate()).isEqualTo(LocalDate.of(2026, 3, 25));
            assertThat(reminder.getDueTime()).isEqualTo(LocalTime.of(9, 0));
            assertThat(reminder.getPriority()).isEqualTo(Priority.HIGH);
            assertThat(reminder.isFlagged()).isTrue();
            assertThat(reminder.getDisplayOrder()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("모든 필드를 수정한다")
        void updateAllFields() {
            Reminder reminder = Reminder.builder()
                    .list(createList())
                    .title("장보기")
                    .notes("우유")
                    .build();

            reminder.update("운동하기", "헬스장", LocalDate.of(2026, 3, 25),
                    LocalTime.of(10, 0), Priority.MEDIUM, true);

            assertThat(reminder.getTitle()).isEqualTo("운동하기");
            assertThat(reminder.getNotes()).isEqualTo("헬스장");
            assertThat(reminder.getDueDate()).isEqualTo(LocalDate.of(2026, 3, 25));
            assertThat(reminder.getDueTime()).isEqualTo(LocalTime.of(10, 0));
            assertThat(reminder.getPriority()).isEqualTo(Priority.MEDIUM);
            assertThat(reminder.isFlagged()).isTrue();
        }
    }

    @Nested
    @DisplayName("toggleComplete")
    class ToggleComplete {

        @Test
        @DisplayName("미완료 → 완료로 전환하면 completedAt이 설정된다")
        void completeReminder() {
            Reminder reminder = Reminder.builder()
                    .list(createList())
                    .title("장보기")
                    .build();

            reminder.toggleComplete();

            assertThat(reminder.isCompleted()).isTrue();
            assertThat(reminder.getCompletedAt()).isNotNull();
        }

        @Test
        @DisplayName("완료 → 미완료로 전환하면 completedAt이 null이 된다")
        void uncompleteReminder() {
            Reminder reminder = Reminder.builder()
                    .list(createList())
                    .title("장보기")
                    .build();
            reminder.toggleComplete();

            reminder.toggleComplete();

            assertThat(reminder.isCompleted()).isFalse();
            assertThat(reminder.getCompletedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("toggleFlag")
    class ToggleFlag {

        @Test
        @DisplayName("플래그를 토글한다")
        void toggleFlag() {
            Reminder reminder = Reminder.builder()
                    .list(createList())
                    .title("장보기")
                    .build();

            reminder.toggleFlag();
            assertThat(reminder.isFlagged()).isTrue();

            reminder.toggleFlag();
            assertThat(reminder.isFlagged()).isFalse();
        }
    }

    @Nested
    @DisplayName("changeList")
    class ChangeList {

        @Test
        @DisplayName("소속 리스트를 변경한다")
        void changeList() {
            ReminderList original = ReminderList.builder().name("개인").build();
            ReminderList target = ReminderList.builder().name("업무").build();
            Reminder reminder = Reminder.builder().list(original).title("장보기").build();

            reminder.changeList(target);

            assertThat(reminder.getList()).isEqualTo(target);
        }
    }
}
