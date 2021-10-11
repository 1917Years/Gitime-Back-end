package capstone.gitime.domain.memberTeam.entity;

import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.team.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_team_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TeamAuthority teamAuthority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_member_team_fk"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "team_member_team_fk"))
    private Team team;

    @Builder(builderMethodName = "createMemberTeamEntity")
    public MemberTeam(TeamAuthority teamAuthority, Member member, Team team) {
        this.teamAuthority = TeamAuthority.ROLE_NOT_CHOOSE;
        this.member = member;
        this.team = team;
    }

    public void changeRole(TeamAuthority teamAuthority) {
        this.teamAuthority = teamAuthority;
    }

    public void updateTeam(Team team) {
        this.team = team;
    }
}
