package org.dev.course.studieo.domain.post.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Getter
public class Post {

    private final UUID postId;
    private final UUID userId;
    private final UUID boardId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Post(
            UUID postId,
            UUID userId,
            UUID boardId,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.postId = postId;
        this.userId = userId;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
