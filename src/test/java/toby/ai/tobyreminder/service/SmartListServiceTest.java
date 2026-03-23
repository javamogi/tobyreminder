package toby.ai.tobyreminder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.domain.Priority;
import toby.ai.tobyreminder.dto.*;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;
import toby.ai.tobyreminder.service.ports.in.ReminderService;
import toby.ai.tobyreminder.service.ports.in.SmartListService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SmartListServiceTest {

    @Autowired
    private SmartListService smartListService;

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

    @Nested
    @DisplayName("today")
    class Today {

        @Test
        @DisplayName("오늘 마감인 미완료 리마인더를 조회한다")
        void todayReminders() {
            reminderService.create(listId, new ReminderRequest("오늘 할 일", null, LocalDate.now(), null, null, null));
            reminderService.create(listId, new ReminderRequest("내일 할 일", null, LocalDate.now().plusDays(1), null, null, null));
            reminderService.create(listId, new ReminderRequest("날짜 없음", null, null, null, null, null));

            List<ReminderResponse> result = smartListService.today();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).title()).isEqualTo("오늘 할 일");
        }
    }

    @Nested
    @DisplayName("scheduled")
    class Scheduled {

        @Test
        @DisplayName("마감일이 있는 미완료 리마인더를 날짜 오름차순으로 조회한다")
        void scheduledReminders() {
            reminderService.create(listId, new ReminderRequest("내일", null, LocalDate.now().plusDays(1), null, null, null));
            reminderService.create(listId, new ReminderRequest("오늘", null, LocalDate.now(), null, null, null));
            reminderService.create(listId, new ReminderRequest("날짜 없음", null, null, null, null, null));

            List<ReminderResponse> result = smartListService.scheduled();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).title()).isEqualTo("오늘");
            assertThat(result.get(1).title()).isEqualTo("내일");
        }
    }

    @Nested
    @DisplayName("all")
    class All {

        @Test
        @DisplayName("모든 미완료 리마인더를 조회한다")
        void allReminders() {
            reminderService.create(listId, new ReminderRequest("할 일 1", null, null, null, null, null));
            ReminderResponse completed = reminderService.create(listId, new ReminderRequest("완료됨", null, null, null, null, null));
            reminderService.toggleComplete(completed.id());
            reminderService.create(listId, new ReminderRequest("할 일 2", null, null, null, null, null));

            List<ReminderResponse> result = smartListService.all();

            assertThat(result).hasSize(2);
        }
    }

    @Nested
    @DisplayName("flagged")
    class Flagged {

        @Test
        @DisplayName("플래그된 미완료 리마인더를 조회한다")
        void flaggedReminders() {
            reminderService.create(listId, new ReminderRequest("플래그", null, null, null, null, true));
            reminderService.create(listId, new ReminderRequest("일반", null, null, null, null, false));

            List<ReminderResponse> result = smartListService.flagged();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).title()).isEqualTo("플래그");
        }
    }

    @Nested
    @DisplayName("completed")
    class Completed {

        @Test
        @DisplayName("완료된 리마인더를 조회한다")
        void completedReminders() {
            ReminderResponse r = reminderService.create(listId, new ReminderRequest("완료 예정", null, null, null, null, null));
            reminderService.toggleComplete(r.id());
            reminderService.create(listId, new ReminderRequest("미완료", null, null, null, null, null));

            List<ReminderResponse> result = smartListService.completed();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).title()).isEqualTo("완료 예정");
        }
    }

    @Nested
    @DisplayName("counts")
    class Counts {

        @Test
        @DisplayName("각 스마트 리스트의 카운트를 반환한다")
        void smartListCounts() {
            reminderService.create(listId, new ReminderRequest("오늘", null, LocalDate.now(), null, null, true));
            reminderService.create(listId, new ReminderRequest("내일", null, LocalDate.now().plusDays(1), null, null, null));
            ReminderResponse r = reminderService.create(listId, new ReminderRequest("완료", null, null, null, null, null));
            reminderService.toggleComplete(r.id());

            SmartListCountsResponse counts = smartListService.counts();

            assertThat(counts.today()).isEqualTo(1);
            assertThat(counts.scheduled()).isEqualTo(2);
            assertThat(counts.all()).isEqualTo(2);
            assertThat(counts.flagged()).isEqualTo(1);
            assertThat(counts.completed()).isEqualTo(1);
        }
    }
}
