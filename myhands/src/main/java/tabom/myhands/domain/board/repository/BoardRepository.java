package tabom.myhands.domain.board.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tabom.myhands.domain.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b ORDER BY b.createdAt DESC")
    List<Board> findTopBySize(Pageable pageable);

    Optional<Board> findByBoardId(Long boardId);

    @Query(value = "SELECT * FROM board WHERE category = :category ORDER BY created_at DESC LIMIT :size", nativeQuery = true)
    List<Board> findFirstPage(@Param("category") int category, @Param("size") int size);

    @Query(value = "SELECT * FROM board WHERE category = :category AND board_id < :lastId ORDER BY created_at DESC LIMIT :size", nativeQuery = true)
    List<Board> findByLastId(@Param("category") int category, @Param("lastId") Long lastId, @Param("size") int size);

}
