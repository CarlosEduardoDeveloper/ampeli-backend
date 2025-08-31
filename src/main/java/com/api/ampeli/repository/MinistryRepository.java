package com.api.ampeli.repository;

import com.api.ampeli.model.Ministry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinistryRepository extends JpaRepository<Ministry, Long> {
    List<Ministry> findByNameContainingIgnoreCase(String name);

    @Query("SELECT m FROM Ministry m WHERE m.description LIKE %:keyword%")
    List<Ministry> findByDescriptionContaining(@Param("keyword") String keyword);

    @Query("SELECT m FROM Ministry m JOIN m.members mb WHERE mb.id = :memberId")
    List<Ministry> findByMemberId(@Param("memberId") Long memberId);
}
