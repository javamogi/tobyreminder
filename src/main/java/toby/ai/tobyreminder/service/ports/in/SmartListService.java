package toby.ai.tobyreminder.service.ports.in;

import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.dto.SmartListCountsResponse;

import java.util.List;

public interface SmartListService {

    List<ReminderResponse> today();

    List<ReminderResponse> scheduled();

    List<ReminderResponse> all();

    List<ReminderResponse> flagged();

    List<ReminderResponse> completed();

    SmartListCountsResponse counts();
}
