package capstone.gitime.domain.board.entity;

import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_team_id", foreignKey = @ForeignKey(name = "comment_member_team_fk"))
    private MemberTeam memberTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "comment_board_fk"))
    private Board board;

    @Builder(builderMethodName = "createComment")
    public Comment(String content, MemberTeam memberTeam, Board board) {
        this.content = content;
        this.memberTeam = memberTeam;
        updateBoard(board);
    }

    public void updateBoard(Board board) {
        this.board = board;
        board.getCommentList().add(this);
    }
}
