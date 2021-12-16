package capstone.gitime.domain.board.service.dto;

import capstone.gitime.domain.board.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentListResponseDto {

    private String comment;
    private String writer;
    private LocalDateTime writeTime;

    public static CommentListResponseDto of(Comment comment) {
        return new CommentListResponseDto(
                comment.getContent(),
                comment.getMemberTeam().getMember().getUserName(),
                comment.getCreatedAt());
    }

}
