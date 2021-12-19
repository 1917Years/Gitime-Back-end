package capstone.gitime.domain.todo.repository;

import capstone.gitime.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("select to from Todo to join to.team t join fetch to.developField df where t.teamName=:teamName")
    List<Todo> findAllByTeam(@Param("teamName") String teamName);

    @Query("select to from Todo to join to.team t where t.teamName = :teamName")
    List<Todo> findAllNotFetchByTeam(@Param("teamName") String teamName);

    @Query("select to from Todo to join to.developField df join to.team t where df.field=:field and t.teamName=:teamName" +
            " and to.working=:working")
    Optional<Todo> findByDevelopFieldAndTeamName(@Param("field") String field,
                                                 @Param("teamName") String teamName,
                                                 @Param("working") String working);

}
