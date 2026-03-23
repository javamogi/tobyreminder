package toby.ai.tobyreminder.service.ports.in;

import toby.ai.tobyreminder.dto.ReminderRequest;
import toby.ai.tobyreminder.dto.ReminderResponse;

import java.util.List;

public interface ReminderService {

    ReminderResponse create(Long listId, ReminderRequest request);

    List<ReminderResponse> findByListId(Long listId);

    List<ReminderResponse> findByListId(Long listId, String sort);

    ReminderResponse findById(Long id);

    ReminderResponse update(Long id, ReminderRequest request);

    void delete(Long id);

    ReminderResponse toggleComplete(Long id);

    ReminderResponse toggleFlag(Long id);

    List<ReminderResponse> search(String keyword);

    void reorderReminders(List<Long> ids);
}
