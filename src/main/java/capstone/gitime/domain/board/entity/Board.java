package capstone.gitime.domain.board.entity;

import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.team.entity.Team;
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
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;

    private Integer likeCount;

    // TODO: 2021/12/16 File Upload Entity Adding

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "board_team_fk"))
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_team_id", foreignKey = @ForeignKey(name = "board_member_team_fk"))
    private MemberTeam memberTeam;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board")
    private List<Comment> commentList = new ArrayList<>();

    @Builder(builderMethodName = "createBoard")
    public Board(String title, String content, MemberTeam memberTeam, Team team) {
        this.team = team;
        this.title = title;
        this.content = content;
        this.memberTeam = memberTeam;
        this.likeCount = 0;
    }
}
