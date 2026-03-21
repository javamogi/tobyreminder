package toby.ai.tobyreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.domain.ReminderList;
import toby.ai.tobyreminder.repository.ReminderListRepository;
import toby.ai.tobyreminder.repository.ReminderRepository;
import toby.ai.tobyreminder.dto.ReminderListRequest;
import toby.ai.tobyreminder.dto.ReminderListResponse;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderListService implements ReminderListService {

    private final ReminderListRepository reminderListRepository;
    private final ReminderRepository reminderRepository;

    @Override
    @Transactional
    public ReminderListResponse create(ReminderListRequest request) {
        int nextOrder = (int) reminderListRepository.count();
        ReminderList list = ReminderList.builder()
                .name(request.name())
                .color(request.color())
                .icon(request.icon())
                .displayOrder(nextOrder)
                .build();
        return ReminderListResponse.from(reminderListRepository.save(list));
    }

    @Override
    public List<ReminderListResponse> findAll() {
        return reminderListRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(list -> ReminderListResponse.from(list, reminderRepository.countByListIdAndCompletedFalse(list.getId())))
                .toList();
    }

    @Override
    public ReminderListResponse findById(Long id) {
        ReminderList list = getById(id);
        return ReminderListResponse.from(list);
    }

    @Override
    @Transactional
    public ReminderListResponse update(Long id, ReminderListRequest request) {
        ReminderList list = getById(id);
        list.update(request.name(), request.color(), request.icon());
        return ReminderListResponse.from(list);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ReminderList list = getById(id);
        reminderListRepository.delete(list);
    }

    private ReminderList getById(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리스트를 찾을 수 없습니다. id=" + id));
    }
}
