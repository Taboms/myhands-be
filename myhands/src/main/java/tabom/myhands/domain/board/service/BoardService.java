package tabom.myhands.domain.board.service;

import tabom.myhands.domain.board.dto.BoardResponse;

public interface BoardService {
    BoardResponse.PostList overview(Integer size);
    BoardResponse.PostDetail detail(Long id);
    BoardResponse.PostList list(int category, Long lastId, int size);
}
