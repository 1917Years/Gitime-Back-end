package capstone.gitime.domain.memberteam.entity;

import capstone.gitime.domain.developfield.entity.DevelopField;
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

    @Enumerated(value = EnumType.STRING)
    private TeamMemberStatus teamMemberStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "develop_field_id", foreignKey = @ForeignKey(name = "member_team_develop_field_fk"))
    private DevelopField developField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_member_team_fk"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "team_member_team_fk"))
    private Team team;

    @Builder(builderMethodName = "createMemberTeamEntity")
    public MemberTeam(TeamAuthority teamAuthority, Member member, Team team, TeamMemberStatus teamMemberStatus) {
        this.developField = null;
        this.teamAuthority = teamAuthority;
        this.member = member;
        this.teamMemberStatus = teamMemberStatus;
        addTeam(team);
    }

    public void addTeam(Team team) {
        this.team = team;
        team.getMemberTeams().add(this);
    }

    public void changeRole(TeamAuthority teamAuthority) {
        this.teamAuthority = teamAuthority;
    }

    public void updateDevelopField(DevelopField developField) {
        this.developField = developField;
    }

    public void updateTeamInvite(TeamMemberStatus teamMemberStatus) {
        this.teamMemberStatus = teamMemberStatus;
    }

    public void deleteMemberTeam() {
        this.teamMemberStatus = TeamMemberStatus.DELETE;
    }

}
