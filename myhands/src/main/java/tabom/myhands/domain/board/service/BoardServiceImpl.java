package tabom.myhands.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tabom.myhands.domain.board.dto.BoardResponse;
import tabom.myhands.domain.board.entity.Board;
import tabom.myhands.domain.board.repository.BoardRepository;
import tabom.myhands.error.errorcode.BoardErrorCode;
import tabom.myhands.error.exception.BoardApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public BoardResponse.PostList overview(Integer size) {
        if(size <= 0){
            throw new BoardApiException(BoardErrorCode.INVALID_SIZE_PARAMETER);
        }
        List<Board> boards = boardRepository.findTopBySize(PageRequest.of(0, size));
        return BoardResponse.PostList.build(boards);
    }

    @Override
    public BoardResponse.PostDetail detail(Long id) {
        Board board = boardRepository.findByBoardId(id)
                .orElseThrow(() -> new BoardApiException(BoardErrorCode.BOARD_ID_NOT_FOUND));

        return BoardResponse.PostDetail.build(board);
    }

    @Override
    public BoardResponse.PostList list(int category, Long lastId, int size) {
        if(lastId != null) {
            boardRepository.findByBoardId(lastId).orElseThrow(() -> new BoardApiException(BoardErrorCode.BOARD_ID_NOT_FOUND));
        }

        List<Board> boards = (lastId == null)
                ? boardRepository.findFirstPage(category, size)
                : boardRepository.findByLastId(category, lastId, size);
        return BoardResponse.PostList.build(boards);
    }

    @Override
    public BoardResponse.PostList search(int category, String word, Long lastId, int size) {
        if(lastId != null) {
            boardRepository.findByBoardId(lastId).orElseThrow(() -> new BoardApiException(BoardErrorCode.BOARD_ID_NOT_FOUND));
        }
        List<Board> boards = (lastId == null)
                ? boardRepository.findWordFirstPage(category, word, size)
                : boardRepository.findWordLastId(category, word, lastId, size);

        return BoardResponse.PostList.build(boards);
    }
}
