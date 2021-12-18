package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.CreateBoardRequestDto;
import capstone.gitime.api.controller.dto.CreateCommentRequestDto;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.domain.board.service.BoardService;
import capstone.gitime.domain.board.service.CommentService;
import capstone.gitime.domain.board.service.dto.BoardDetailResponseDto;
import capstone.gitime.domain.board.service.dto.BoardListResponseDto;
import capstone.gitime.domain.board.service.dto.CommentListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/{teamName}/list")
    public ResultResponseDto<Page<BoardListResponseDto>> getAllBoardsToTeam(@Token Long memberId,
                                                                            @RequestParam(value = "page") Integer page,
                                                                            @PathVariable(value = "teamName") String teamName) {
        return new ResultResponseDto<>(200, "OK!", List.of(boardService.getAllBoard(teamName, page)));
    }

    @GetMapping("/{teamName}/{boardId}")
    public ResultResponseDto<BoardDetailResponseDto> getSpecificBoard(@Token Long memberId,
                                                                      @PathVariable(value = "teamName") String teamName,
                                                                      @PathVariable(value = "boardId") Long boardId) {
        return new ResultResponseDto<>(200, "OK!", List.of(boardService.getBoardContent(boardId)));
    }

    @PostMapping("/{teamName}/write")
    public ResultResponseDto<String> writeNewBoardToTeam(@Token Long memberId,
                                                         @RequestBody CreateBoardRequestDto requestDto,
                                                         @PathVariable("teamName") String teamName) {
        boardService.writeNewBoard(requestDto, memberId, teamName);
        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());

    }

    @GetMapping("/{teamName}/{boardId}/comment")
    public ResultResponseDto<CommentListResponseDto> getAllCommentToBoard(@PathVariable(value = "boardId") Long boardId) {
        return new ResultResponseDto<>(200, "OK!", commentService.getAllComment(boardId));
    }

    @PostMapping("/{teamName}/{boardId}/comment")
    public ResultResponseDto<String> writeNewCommentToBoard(@Token Long memberId,
                                                            @RequestBody CreateCommentRequestDto requestDto,
                                                            @PathVariable("teamName") String teamName,
                                                            @PathVariable("boardId") Long boardId) {
        commentService.writeNewComment(teamName, boardId, memberId, requestDto);

        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }


}
