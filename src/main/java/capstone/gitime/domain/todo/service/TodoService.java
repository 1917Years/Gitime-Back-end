package capstone.gitime.domain.todo.service;

import capstone.gitime.api.controller.dto.CreateTodoRequestDto;
import capstone.gitime.api.exception.exception.developfield.NotFoundDevelopFieldException;
import capstone.gitime.api.exception.exception.global.NotAccessException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.developfield.repository.DevelopFieldRepository;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.repository.MemberTeamRepository;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.repository.TeamRepository;
import capstone.gitime.domain.todo.entity.Todo;
import capstone.gitime.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final DevelopFieldRepository developFieldRepository;
    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;

    public void createTodo(CreateTodoRequestDto requestDto, Long memberId, String teamName) {
        memberDevelopAccessCheck(requestDto.getField(), memberId, teamName);

        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        DevelopField findDevelopField = developFieldRepository.findByField(requestDto.getField(), teamName)
                .orElseThrow(() -> new NotFoundDevelopFieldException());

        Todo newTodo = Todo.createTodo()
                .team(findTeam)
                .developField(findDevelopField)
                .working(requestDto.getWorking())
                .build();

        todoRepository.save(newTodo);
    }

    private void memberDevelopAccessCheck(String field, Long memberId, String teamName) {
        MemberTeam findMemberTeam = memberTeamRepository.findFetchDevelopFieldByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        if (!findMemberTeam.getDevelopField().getField().equals(field)) {
            throw new NotAccessException();
        }
    }

}
