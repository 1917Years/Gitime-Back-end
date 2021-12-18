package capstone.gitime.domain.board.service.dto;

import capstone.gitime.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BoardDetailResponseDto {

    private String title;
    private String content;
    private String writer;
    private String writerField;
    private LocalDateTime writeTime;
    private Integer likeCount;
    private List<CommentListResponseDto> comments;

    public static BoardDetailResponseDto of(Board board) {
        return new BoardDetailResponseDto(
                board.getTitle(),
                board.getContent(),
                board.getMemberTeam().getMember().getUserName(),
                board.getMemberTeam().getDevelopField().getField(),
                board.getCreatedAt(),
                board.getLikeCount(),
                board.getCommentList().stream().map(CommentListResponseDto::of)
                        .collect(Collectors.toList()));
    }
}
