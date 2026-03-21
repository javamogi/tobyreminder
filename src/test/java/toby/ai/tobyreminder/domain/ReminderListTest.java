package toby.ai.tobyreminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import toby.ai.tobyreminder.repository.ReminderListRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReminderListTest {

    @Autowired
    private ReminderListRepository reminderListRepository;

    @Nested
    @DisplayName("생성자")
    class Constructor {

        @Test
        @DisplayName("모든 필드를 지정하여 생성한다")
        void createWithAllFields() {
            ReminderList list = ReminderList.builder()
                    .name("업무")
                    .color("#FF3B30")
                    .icon("briefcase")
                    .displayOrder(1)
                    .build();

            assertThat(list.getName()).isEqualTo("업무");
            assertThat(list.getColor()).isEqualTo("#FF3B30");
            assertThat(list.getIcon()).isEqualTo("briefcase");
            assertThat(list.getDisplayOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("이름만 지정하면 나머지는 기본값이 설정된다")
        void createWithNameOnly() {
            ReminderList list = ReminderList.builder()
                    .name("개인")
                    .build();

            assertThat(list.getName()).isEqualTo("개인");
            assertThat(list.getColor()).isEqualTo("#007AFF");
            assertThat(list.getIcon()).isEqualTo("list.bullet");
            assertThat(list.getDisplayOrder()).isZero();
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("이름, 색상, 아이콘을 모두 변경한다")
        void updateAllFields() {
            ReminderList list = ReminderList.builder().name("개인").build();

            list.update("업무", "#FF3B30", "briefcase");

            assertThat(list.getName()).isEqualTo("업무");
            assertThat(list.getColor()).isEqualTo("#FF3B30");
            assertThat(list.getIcon()).isEqualTo("briefcase");
        }

        @Test
        @DisplayName("색상과 아이콘이 null이면 기존 값을 유지한다")
        void updateWithNullKeepsExisting() {
            ReminderList list = ReminderList.builder()
                    .name("개인")
                    .color("#FF9500")
                    .icon("star")
                    .build();

            list.update("개인 수정", null, null);

            assertThat(list.getName()).isEqualTo("개인 수정");
            assertThat(list.getColor()).isEqualTo("#FF9500");
            assertThat(list.getIcon()).isEqualTo("star");
        }

        @Test
        @DisplayName("displayOrder를 변경한다")
        void updateDisplayOrder() {
            ReminderList list = ReminderList.builder().name("개인").build();

            list.updateDisplayOrder(5);

            assertThat(list.getDisplayOrder()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Auditing - createdAt, updatedAt 자동 등록")
    class Auditing {

        @Test
        @DisplayName("저장 시 createdAt과 updatedAt이 자동으로 설정된다")
        void createdAtAndUpdatedAtAreSet() {
            ReminderList list = ReminderList.builder().name("개인").build();

            ReminderList saved = reminderListRepository.save(list);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getCreatedAt()).isNotNull();
            assertThat(saved.getUpdatedAt()).isNotNull();
            assertThat(saved.getCreatedAt()).isEqualTo(saved.getUpdatedAt());
        }

        @Test
        @DisplayName("수정 시 updatedAt이 갱신되고 createdAt은 유지된다")
        void updatedAtChangesOnModification() {
            ReminderList list = ReminderList.builder().name("개인").build();
            ReminderList saved = reminderListRepository.saveAndFlush(list);

            saved.update("업무", "#FF3B30", "briefcase");
            ReminderList updated = reminderListRepository.saveAndFlush(saved);

            assertThat(updated.getCreatedAt()).isEqualTo(saved.getCreatedAt());
            assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(saved.getCreatedAt());
        }
    }
}
