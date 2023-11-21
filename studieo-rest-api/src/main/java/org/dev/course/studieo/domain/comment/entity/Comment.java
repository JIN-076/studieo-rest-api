package org.dev.course.studieo.domain.comment.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Getter
public class Comment {

    private final UUID commentId;
    private final UUID userId;
    private final UUID postId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Comment(
            UUID commentId,
            UUID userId,
            UUID postId,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
