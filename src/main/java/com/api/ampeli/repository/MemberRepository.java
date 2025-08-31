package com.api.ampeli.repository;

import com.api.ampeli.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(Long userId);
    Optional<Member> findByEmail(String email);
    List<Member> findByFaithStage(Member.FaithStage faithStage);

    @Query("SELECT m FROM Member m WHERE m.interestAreas LIKE %:interest%")
    List<Member> findByInterestAreasContaining(@Param("interest") String interest);

    @Query("SELECT m FROM Member m WHERE m.volunteerArea LIKE %:area%")
    List<Member> findByVolunteerAreaContaining(@Param("area") String area);
}
