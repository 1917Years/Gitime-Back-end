package capstone.gitime.domain.board.repository;

import capstone.gitime.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select c from Comment c join c.board b on b.id = :boardId")
    List<Comment> findAllByBoardId(@Param("boardId") Long boardId);

}
