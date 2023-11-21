package org.dev.course.studieo.domain.board.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
public class Board {

    private final UUID boardId;
    private final String title;
    private final String description;

    public Board(UUID boardId, String title, String description) {
        this.boardId = boardId;
        this.title = title;
        this.description = description;
    }
}
