package toby.ai.tobyreminder.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import toby.ai.tobyreminder.dto.ReminderRequest;
import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.service.ports.in.ReminderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping("/api/lists/{listId}/reminders")
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse create(@PathVariable Long listId, @RequestBody @Valid ReminderRequest request) {
        return reminderService.create(listId, request);
    }

    @GetMapping("/api/lists/{listId}/reminders")
    public List<ReminderResponse> findByListId(@PathVariable Long listId) {
        return reminderService.findByListId(listId);
    }

    @GetMapping("/api/reminders/{id}")
    public ReminderResponse findById(@PathVariable Long id) {
        return reminderService.findById(id);
    }

    @PutMapping("/api/reminders/{id}")
    public ReminderResponse update(@PathVariable Long id, @RequestBody @Valid ReminderRequest request) {
        return reminderService.update(id, request);
    }

    @DeleteMapping("/api/reminders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderService.delete(id);
    }

    @PatchMapping("/api/reminders/{id}/complete")
    public ReminderResponse toggleComplete(@PathVariable Long id) {
        return reminderService.toggleComplete(id);
    }

    @PatchMapping("/api/reminders/{id}/flag")
    public ReminderResponse toggleFlag(@PathVariable Long id) {
        return reminderService.toggleFlag(id);
    }
}
