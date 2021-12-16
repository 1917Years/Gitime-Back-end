package capstone.gitime.domain.todo.entity;

import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.team.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "todo_team_fk"))
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "develop_field_id", foreignKey = @ForeignKey(name = "develop_field_fk"))
    private DevelopField developField;

    private String working;

    private Boolean isFinish;

    private LocalDate untilDate;

    @Builder(builderMethodName = "createTodo")
    public Todo(Team team, DevelopField developField, String working, Boolean isFinish, LocalDate untilDate) {
        this.team = team;
        this.developField = developField;
        this.working = working;
        this.isFinish = isFinish;
        this.untilDate = untilDate;
    }

    public void updateTodoFinish() {
        this.isFinish = !isFinish;
    }
}
