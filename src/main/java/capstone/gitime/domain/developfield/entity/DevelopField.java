package capstone.gitime.domain.developfield.entity;

import capstone.gitime.domain.team.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DevelopField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private String field;

    @Builder(builderMethodName = "createDevelopField")
    public DevelopField(Team team, String field) {
        setTeam(team);
        this.field = field;
    }

    public void setTeam(Team team){
        this.team = team;
        team.getDevelopFields().add(this);
    }
}
