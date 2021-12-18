package capstone.gitime.domain.board.service;

import capstone.gitime.api.controller.dto.CreateBoardRequestDto;
import capstone.gitime.api.exception.exception.board.NotFoundBoardException;
import capstone.gitime.api.exception.exception.memberteam.NotFoundMemberTeamException;
import capstone.gitime.api.exception.exception.team.NotFoundTeamException;
import capstone.gitime.domain.board.entity.Board;
import capstone.gitime.domain.board.repository.BoardRepository;
import capstone.gitime.domain.board.repository.CommentRepository;
import capstone.gitime.domain.board.service.dto.BoardDetailResponseDto;
import capstone.gitime.domain.board.service.dto.BoardListResponseDto;
import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.repository.MemberTeamRepository;
import capstone.gitime.domain.team.entity.Team;
import capstone.gitime.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final MemberTeamRepository memberTeamRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final TeamRepository teamRepository;


    public Page<BoardListResponseDto> getAllBoard(String teamName, Integer page) {

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.Direction.ASC);

        Page<Board> boards = boardRepository.findFetchMemberTeamAllByTeamName(teamName,pageRequest);

        return boards.map(BoardListResponseDto::of);
    }

    public BoardDetailResponseDto getBoardContent(Long boardId) {
        Board findBoard = boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        return BoardDetailResponseDto.of(findBoard);
    }

    @Transactional
    public void writeNewBoard(CreateBoardRequestDto requestDto, Long memberId, String teamName) {
        Team findTeam = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new NotFoundTeamException());

        MemberTeam findMemberTeam = memberTeamRepository.findByTeamNameAndMember(memberId, teamName)
                .orElseThrow(() -> new NotFoundMemberTeamException());

        Board newBoard = Board.createBoard()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .team(findTeam)
                .memberTeam(findMemberTeam)
                .build();

        boardRepository.save(newBoard);
    }
}
