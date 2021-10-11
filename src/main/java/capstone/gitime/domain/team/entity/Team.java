package capstone.gitime.domain.team.entity;

import capstone.gitime.domain.common.entity.BaseEntity;
import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.common.entity.GitRepo;
import capstone.gitime.domain.memberTeam.entity.MemberTeam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    // 양방향 매핑
    @OneToMany(mappedBy = "team")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    // 양방향 매핑을 할 필요가 있을까?..
    @OneToOne(fetch = FetchType.LAZY)
    private GitRepo gitRepo;

    @Builder(builderMethodName = "createTeamEntity")
    public Team(String teamName, GitRepo gitRepo, MemberTeam memberTeam) {
        this.teamName = teamName;
        this.gitRepo = gitRepo;
        if (memberTeam != null) {
            addMemberTeam(memberTeam);
        }
    }

    public void addMemberTeam(MemberTeam memberTeam) {
        this.memberTeams.add(memberTeam);
        memberTeam.updateTeam(this);
    }
}
