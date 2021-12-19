package capstone.gitime.domain.todo.service;

import capstone.gitime.api.controller.dto.CreateTodoRequestDto;
import capstone.gitime.api.controller.dto.ModifyTodoRequestDto;
import capstone.gitime.api.exception.exception.developfield.NotFoundDevelopFieldException;
import capstone.gitime.api.exception.exception.global.NotAccessException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.api.exception.exception.todo.NotFoundTodoException;
import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.developfield.repository.DevelopFieldRepository;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.repository.MemberTeamRepository;
import capstone.gitime.domain.notification.entity.NotificationImportance;
import capstone.gitime.domain.notification.entity.NotificationType;
import capstone.gitime.domain.notification.service.NotificationService;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.repository.TeamRepository;
import capstone.gitime.domain.todo.entity.Todo;
import capstone.gitime.domain.todo.repository.TodoRepository;
import capstone.gitime.domain.todo.service.dto.TodoListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final DevelopFieldRepository developFieldRepository;
    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final NotificationService notificationService;

    @Transactional
    public void createTodo(CreateTodoRequestDto requestDto, Long memberId, String teamName) {
//        memberDevelopAccessCheck(requestDto.getField(), memberId, teamName);

        MemberTeam findMemberTeam = memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        DevelopField findDevelopField = developFieldRepository.findByField(requestDto.getField(), teamName)
                .orElseThrow(() -> new NotFoundDevelopFieldException());

        String[] splitDate = requestDto.getUntilDate().split("/");

        Todo newTodo = Todo.createTodo()
                .team(findMemberTeam.getTeam())
                .developField(findDevelopField)
                .working(requestDto.getWorking())
                .isFinish(false)
                .untilDate(LocalDate.of(
                        Integer.parseInt(splitDate[0]),
                        Integer.parseInt(splitDate[1]),
                        Integer.parseInt(splitDate[2])))
                .build();

        todoRepository.save(newTodo);

        notificationService.pushNotificationToTeam(findMemberTeam, NotificationType.TODO, NotificationImportance.NORMAL,
                LocalDate.of(
                        Integer.parseInt(splitDate[0]),
                        Integer.parseInt(splitDate[1]),
                        Integer.parseInt(splitDate[2])));

    }

    @Transactional
    public void modifyFinishTodo(ModifyTodoRequestDto requestDto, String teamName, Long memberId) {

        memberDevelopAccessCheck(requestDto.getFieldName(), memberId, teamName);

        Todo findTodo = todoRepository.findByDevelopFieldAndTeamName(requestDto.getFieldName(), teamName, requestDto.getWorking())
                .orElseThrow(() -> new NotFoundTodoException());

        findTodo.updateTodoFinish();
    }

    public List<TodoListResponseDto> getAllTodo(String teamName) {
        return todoRepository.findAllByTeam(teamName)
                .stream().map(TodoListResponseDto::of)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getDevelopProgress(String teamName) {
        Map<String, Integer> result = new ConcurrentHashMap<>();
        Map<String, Integer> resultCount = new ConcurrentHashMap<>();
        Map<String, Double> finalResult = new ConcurrentHashMap<>();

        /**
         * DevelopField : 백엔드 , 프론트, UI
         * Map<String,Double>
         *     { [백엔드,33.02], [프론트,22,43], [UI,72.32] }
         */

        List<Todo> allTodos = todoRepository.findAllByTeam(teamName);

        for (Todo item : allTodos) {
            Integer cntTmp;
            if (resultCount.get(item.getDevelopField().getField()) == null) {
                resultCount.put(item.getDevelopField().getField(), 1);
            } else {
                cntTmp = resultCount.get(item.getDevelopField().getField());
                resultCount.put(item.getDevelopField().getField(), cntTmp + 1);
            }
        }

        resultCount.keySet()
                .forEach((key) -> {
                    result.put(key, 0);
                });

        for (Todo item : allTodos){
            Integer cntTmp;
            if (item.getIsFinish()) {
                cntTmp = result.get(item.getDevelopField().getField());
                result.put(item.getDevelopField().getField(), cntTmp + 1);
            }
        }

        resultCount.keySet()
                .stream().forEach((item) -> {
                    Integer completeCount = result.get(item);
                    Integer totalCount = resultCount.get(item);
                    finalResult.put(item, (completeCount / Double.valueOf(totalCount)) * 100);
                });

        return finalResult;
    }

    private void memberDevelopAccessCheck(String field, Long memberId, String teamName) {
        MemberTeam findMemberTeam = memberTeamRepository.findFetchDevelopFieldByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        if (!findMemberTeam.getDevelopField().getField().equals(field)) {
            throw new NotAccessException();
        }
    }

}
