package tabom.myhands.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tabom.myhands.domain.board.entity.Board;

import java.time.LocalDateTime;
import java.util.List;

public class BoardResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PostItem {
        private Long boardId;
        private String title;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PostList {
        private List<PostItem> overviewList;

        public static PostList build(List<Board> boards) {
            List<PostItem> postItems = boards.stream()
                    .map(board -> PostItem.builder()
                            .boardId(board.getBoardId())
                            .title(board.getTitle())
                            .createdAt(board.getCreatedAt())
                            .build())
                    .toList();
            return PostList.builder()
                    .overviewList(postItems)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PostDetail{
        private Long boardId;
        private String title;
        private String content;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        public static PostDetail build(Board board){
            return PostDetail.builder()
                    .boardId(board.getBoardId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdAt(board.getCreatedAt())
                    .build();
        }
    }
}
