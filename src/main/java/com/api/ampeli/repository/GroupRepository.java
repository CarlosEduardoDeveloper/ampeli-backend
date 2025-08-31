package com.api.ampeli.repository;

import com.api.ampeli.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByNameContainingIgnoreCase(String name);

    @Query("SELECT g FROM Group g WHERE g.description LIKE %:keyword%")
    List<Group> findByDescriptionContaining(@Param("keyword") String keyword);

    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.id = :memberId")
    List<Group> findByMemberId(@Param("memberId") Long memberId);
}
