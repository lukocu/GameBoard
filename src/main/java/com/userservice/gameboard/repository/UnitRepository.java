package com.userservice.gameboard.repository;

import com.userservice.gameboard.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findById(long id);

    void delete(Unit unit);

    Unit save(Unit unit);

    List<Unit> findByGameSessionId(Long gameSessionId);
}
