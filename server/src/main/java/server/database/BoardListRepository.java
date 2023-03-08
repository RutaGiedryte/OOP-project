package server.database;

import commons.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardListRepository extends JpaRepository<BoardList, Integer> {
    @Modifying
    @Query("UPDATE BoardList b SET b.title = :title WHERE b.id = :id")
    public void updateListById(@Param("id") Integer id, @Param("title") String title);
}
