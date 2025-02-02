package com.userservice.gameboard.repository;

import com.userservice.gameboard.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByGameSessionId(Long id);
}