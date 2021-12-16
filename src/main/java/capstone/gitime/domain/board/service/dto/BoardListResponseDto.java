package capstone.gitime.domain.board.service.dto;

import capstone.gitime.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BoardListResponseDto {

    private String title;
    private String content;
    private String writer;
    private LocalDateTime writeTime;
    private Integer likeCount;
    private List<CommentListResponseDto> comments;

    public static BoardListResponseDto of(Board board) {
        return new BoardListResponseDto(
                board.getTitle(),
                board.getContent(),
                board.getMemberTeam().getMember().getUserName(),
                board.getCreatedAt(),
                board.getLikeCount(),
                board.getCommentList().stream().map(CommentListResponseDto::of)
                        .collect(Collectors.toList()));
    }

}
