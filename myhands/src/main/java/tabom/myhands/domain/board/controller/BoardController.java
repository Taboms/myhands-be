package tabom.myhands.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tabom.myhands.common.properties.ResponseProperties;
import tabom.myhands.common.response.DtoResponse;
import tabom.myhands.domain.board.dto.BoardResponse;
import tabom.myhands.domain.board.service.BoardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final ResponseProperties responseProperties;

    @GetMapping("/overview")
    public ResponseEntity<DtoResponse<BoardResponse.PostList>> overview (@RequestParam(defaultValue = "6") int size){
        BoardResponse.PostList response = boardService.overview(size);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(),response));
    }

    @GetMapping("/detail")
    public ResponseEntity<DtoResponse<BoardResponse.PostDetail>> detail(@RequestParam Long id){
        BoardResponse.PostDetail response = boardService.detail(id);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(),response));
    }

    @GetMapping("/list")
    public ResponseEntity<DtoResponse<BoardResponse.PostList>> list(
            @RequestParam int category,
            @RequestParam(required = false) Long lastId, // 마지막 ID
            @RequestParam(defaultValue = "10") int size
    ) {
        BoardResponse.PostList response = boardService.list(category, lastId, size);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(), response));
    }

}
