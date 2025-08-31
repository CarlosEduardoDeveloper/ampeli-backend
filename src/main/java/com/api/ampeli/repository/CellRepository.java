package com.api.ampeli.repository;

import com.api.ampeli.model.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {
    List<Cell> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM Cell c WHERE c.description LIKE %:keyword%")
    List<Cell> findByDescriptionContaining(@Param("keyword") String keyword);

    @Query("SELECT c FROM Cell c JOIN c.members m WHERE m.id = :memberId")
    List<Cell> findByMemberId(@Param("memberId") Long memberId);
}
