package toby.ai.tobyreminder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.dto.SmartListCountsResponse;
import toby.ai.tobyreminder.service.ports.in.SmartListService;

import java.util.List;

@RestController
@RequestMapping("/api/smart")
@RequiredArgsConstructor
public class SmartListController {

    private final SmartListService smartListService;

    @GetMapping("/today")
    public List<ReminderResponse> today() {
        return smartListService.today();
    }

    @GetMapping("/scheduled")
    public List<ReminderResponse> scheduled() {
        return smartListService.scheduled();
    }

    @GetMapping("/all")
    public List<ReminderResponse> all() {
        return smartListService.all();
    }

    @GetMapping("/flagged")
    public List<ReminderResponse> flagged() {
        return smartListService.flagged();
    }

    @GetMapping("/completed")
    public List<ReminderResponse> completed() {
        return smartListService.completed();
    }

    @GetMapping("/counts")
    public SmartListCountsResponse counts() {
        return smartListService.counts();
    }
}
