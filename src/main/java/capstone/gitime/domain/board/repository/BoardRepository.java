package capstone.gitime.domain.board.repository;

import capstone.gitime.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {


    @Query(value = "select b from Board b join b.team t on t.teamName=:teamName")
    List<Board> findAllByTeamName(@Param("teamName") String teamName);

    @Query(value = "select b from Board b join fetch b.memberTeam mt join b.team t where t.teamName=:teamName",
    countQuery = "select count(b) from Board b join b.team t where t.teamName=:teamName")
    Page<Board> findFetchMemberTeamAllByTeamName(@Param("teamName") String teamName, Pageable pageable);

    @Query(value = "select b from Board b join fetch b.memberTeam where b.id=:boardId")
    Optional<Board> findBoardById(@Param("boardId") Long boardId);

}
