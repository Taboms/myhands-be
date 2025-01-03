package tabom.myhands.domain.board.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tabom.myhands.domain.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b ORDER BY b.createdAt DESC")
    List<Board> findTopBySize(Pageable pageable);

    Optional<Board> findByBoardId(Long boardId);
}
