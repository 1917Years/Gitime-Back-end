package capstone.gitime.domain.board.service.dto;

import capstone.gitime.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardListResponseDto {

    private String title;
    private String content;
    private String writer;
    private String writerField;
    private LocalDateTime writeTime;
    private Integer likeCount;
    private Integer commentCount;

    public static BoardListResponseDto of(Board board) {
        return new BoardListResponseDto(
                board.getTitle(),
                board.getContent(),
                board.getMemberTeam().getMember().getUserName(),
                board.getMemberTeam().getDevelopField().getField(),
                board.getCreatedAt(),
                board.getLikeCount(),
                board.getCommentList().size());
    }

}
