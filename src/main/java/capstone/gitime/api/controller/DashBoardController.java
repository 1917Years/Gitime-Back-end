package capstone.gitime.api.controller;

import capstone.gitime.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/dashboard")
public class DashBoardController {
    private final TeamRepository teamRepository;

}
