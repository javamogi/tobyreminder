package toby.ai.tobyreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.dto.SmartListCountsResponse;
import toby.ai.tobyreminder.repository.ReminderRepository;
import toby.ai.tobyreminder.service.ports.in.SmartListService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultSmartListService implements SmartListService {

    private final ReminderRepository reminderRepository;

    @Override
    public List<ReminderResponse> today() {
        return reminderRepository.findByDueDateAndCompletedFalseOrderByDisplayOrderAsc(LocalDate.now()).stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> scheduled() {
        return reminderRepository.findByDueDateIsNotNullAndCompletedFalseOrderByDueDateAscDisplayOrderAsc().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> all() {
        return reminderRepository.findByCompletedFalseOrderByDisplayOrderAsc().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> flagged() {
        return reminderRepository.findByFlaggedTrueAndCompletedFalseOrderByDisplayOrderAsc().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> completed() {
        return reminderRepository.findByCompletedTrueOrderByCompletedAtDesc().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public SmartListCountsResponse counts() {
        return SmartListCountsResponse.builder()
                .today(reminderRepository.countByDueDateAndCompletedFalse(LocalDate.now()))
                .scheduled(reminderRepository.countByDueDateIsNotNullAndCompletedFalse())
                .all(reminderRepository.countByCompletedFalse())
                .flagged(reminderRepository.countByFlaggedTrueAndCompletedFalse())
                .completed(reminderRepository.countByCompletedTrue())
                .build();
    }
}
