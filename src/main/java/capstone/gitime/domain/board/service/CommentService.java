package capstone.gitime.domain.board.service;

import capstone.gitime.api.controller.dto.CreateCommentRequestDto;
import capstone.gitime.api.exception.exception.board.NotFoundBoardException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.domain.board.entity.Board;
import capstone.gitime.domain.board.entity.Comment;
import capstone.gitime.domain.board.repository.BoardRepository;
import capstone.gitime.domain.board.repository.CommentRepository;
import capstone.gitime.domain.board.service.dto.CommentListResponseDto;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.repository.MemberTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberTeamRepository memberTeamRepository;

    @Transactional
    public void writeNewComment(String teamName, Long boardId, Long memberId, CreateCommentRequestDto requestDto) {

        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        MemberTeam findMemberTeam = memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        Comment newComment = Comment.createComment()
                .content(requestDto.getComment())
                .board(findBoard)
                .memberTeam(findMemberTeam)
                .build();

        commentRepository.save(newComment);
    }

    public List<CommentListResponseDto> getAllComment(Long boardId) {
        return commentRepository.findAllByBoardId(boardId)
                .stream().map(CommentListResponseDto::of)
                .collect(Collectors.toList());
    }
}
