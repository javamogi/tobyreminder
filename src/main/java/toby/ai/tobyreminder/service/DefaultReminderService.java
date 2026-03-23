package toby.ai.tobyreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.domain.Reminder;
import toby.ai.tobyreminder.domain.ReminderList;
import toby.ai.tobyreminder.dto.ReminderRequest;
import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.repository.ReminderListRepository;
import toby.ai.tobyreminder.repository.ReminderRepository;
import toby.ai.tobyreminder.service.ports.in.ReminderService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderService implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderListRepository reminderListRepository;

    @Override
    @Transactional
    public ReminderResponse create(Long listId, ReminderRequest request) {
        ReminderList list = getListById(listId);
        int nextOrder = (int) reminderRepository.countByListIdAndCompletedFalse(listId);
        Reminder reminder = Reminder.builder()
                .list(list)
                .title(request.title())
                .notes(request.notes())
                .dueDate(request.dueDate())
                .dueTime(request.dueTime())
                .priority(request.priority())
                .flagged(request.flagged())
                .displayOrder(nextOrder)
                .build();
        return ReminderResponse.from(reminderRepository.save(reminder));
    }

    @Override
    public List<ReminderResponse> findByListId(Long listId) {
        getListById(listId);
        return reminderRepository.findByListIdAndCompletedFalseOrderByDisplayOrderAsc(listId).stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public ReminderResponse findById(Long id) {
        return ReminderResponse.from(getReminderById(id));
    }

    @Override
    @Transactional
    public ReminderResponse update(Long id, ReminderRequest request) {
        Reminder reminder = getReminderById(id);
        reminder.update(request.title(), request.notes(), request.dueDate(),
                request.dueTime(), request.priority(), request.flagged());
        return ReminderResponse.from(reminder);
    }

    @Override
    @Transactional
    public ReminderResponse toggleFlag(Long id) {
        Reminder reminder = getReminderById(id);
        reminder.toggleFlag();
        return ReminderResponse.from(reminder);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reminder reminder = getReminderById(id);
        reminderRepository.delete(reminder);
    }

    @Override
    @Transactional
    public ReminderResponse toggleComplete(Long id) {
        Reminder reminder = getReminderById(id);
        reminder.toggleComplete();
        return ReminderResponse.from(reminder);
    }

    private ReminderList getListById(Long listId) {
        return reminderListRepository.findById(listId)
                .orElseThrow(() -> new NoSuchElementException("리스트를 찾을 수 없습니다. id=" + listId));
    }

    private Reminder getReminderById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리마인더를 찾을 수 없습니다. id=" + id));
    }
}
