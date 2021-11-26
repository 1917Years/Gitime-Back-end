package capstone.gitime.domain.team.entity;

import capstone.gitime.domain.common.entity.BaseEntity;
import capstone.gitime.domain.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamNotice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_notice_id")
    private Long id;

    private String notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder(builderMethodName = "createTeamNotice")
    public TeamNotice(String notice, Team team) {
        updateTeam(team);
        this.notice = notice;
    }

    public void updateTeam(Team team) {
        this.team = team;
        team.getTeamNotices().add(this);
    }

}
