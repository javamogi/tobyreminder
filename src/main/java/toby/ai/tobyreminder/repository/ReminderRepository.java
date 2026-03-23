package toby.ai.tobyreminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import toby.ai.tobyreminder.domain.Reminder;

import java.time.LocalDate;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByListIdAndCompletedFalseOrderByDisplayOrderAsc(Long listId);

    long countByListIdAndCompletedFalse(Long listId);

    List<Reminder> findByDueDateAndCompletedFalseOrderByDisplayOrderAsc(LocalDate dueDate);

    List<Reminder> findByDueDateIsNotNullAndCompletedFalseOrderByDueDateAscDisplayOrderAsc();

    List<Reminder> findByCompletedFalseOrderByDisplayOrderAsc();

    List<Reminder> findByFlaggedTrueAndCompletedFalseOrderByDisplayOrderAsc();

    List<Reminder> findByCompletedTrueOrderByCompletedAtDesc();

    long countByDueDateAndCompletedFalse(LocalDate dueDate);

    long countByDueDateIsNotNullAndCompletedFalse();

    long countByCompletedFalse();

    long countByFlaggedTrueAndCompletedFalse();

    long countByCompletedTrue();

    @Query("SELECT r FROM Reminder r WHERE r.completed = false AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))) ORDER BY r.displayOrder ASC")
    List<Reminder> searchByKeyword(String keyword);
}
