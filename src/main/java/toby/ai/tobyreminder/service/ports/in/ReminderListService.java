package toby.ai.tobyreminder.service.ports.in;

import toby.ai.tobyreminder.dto.ReminderListRequest;
import toby.ai.tobyreminder.dto.ReminderListResponse;

import java.util.List;

public interface ReminderListService {

    ReminderListResponse create(ReminderListRequest request);

    List<ReminderListResponse> findAll();

    ReminderListResponse findById(Long id);

    ReminderListResponse update(Long id, ReminderListRequest request);

    void delete(Long id);
}
