package toby.ai.tobyreminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toby.ai.tobyreminder.domain.ReminderList;

import java.util.List;

public interface ReminderListRepository extends JpaRepository<ReminderList, Long> {
    List<ReminderList> findAllByOrderByDisplayOrderAsc();
}
