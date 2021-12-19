package capstone.gitime.domain.team.entity;

import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.common.entity.GitRepo;
import capstone.gitime.domain.developfield.entity.DevelopField;
import capstone.gitime.domain.endpoint.entity.EndPoint;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.team.service.dto.UpdateTeamInfoRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    private String chatUUID;

    private String teamDescription;

    @Enumerated(value = EnumType.STRING)
    private DevelopType developType;

    @Enumerated(value = EnumType.STRING)
    private TeamStatus teamStatus;

    // 양방향 매핑
    @OneToMany(mappedBy = "team")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    // 양방향 매핑
    @OneToMany(mappedBy = "team")
    private List<DevelopField> developFields = new ArrayList<>();

    // 양방향 매핑을 할 필요가 있을까?..
    @OneToOne(fetch = FetchType.LAZY)
    private GitRepo gitRepo;

    // 양방향 매핑
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    private List<TeamNotice> teamNotices = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private EndPoint endPoint;

    @Builder(builderMethodName = "createTeamEntity")
    public Team(String teamName, String chatUUID, String teamDescription, GitRepo gitRepo, DevelopType developType, TeamStatus teamStatus) {
        this.developType = developType;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.gitRepo = gitRepo;
        this.teamStatus = teamStatus;
        this.chatUUID = chatUUID;
        this.endPoint = null;
    }

    public void updateTeamInfo(UpdateTeamInfoRequestDto requestDto) {
        if (hasText(requestDto.getTeamName())) {
            this.teamName = requestDto.getTeamName();
        }

        if (hasText(requestDto.getTeamDescription())) {
            this.teamDescription = requestDto.getTeamDescription();
        }

        if (requestDto.getDevelopType() != null) {
            this.developType = requestDto.getDevelopType();
        }
    }

    public void updateTeamStatus(TeamStatus teamStatus) {
        this.teamStatus = teamStatus;
    }

    public void updateEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

}
