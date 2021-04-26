package com.assign.organization.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    int deleteByName(String name);

    @Query("SELECT m FROM Member as m LEFT OUTER JOIN m.team as t " +
            "WHERE m.name LIKE concat('%', :keyword, '%') " +
            "OR m.contact.businessCall LIKE concat('%', :keyword, '%') " +
            "OR m.contact.cellPhone LIKE concat('%', :keyword, '%') " +
            "OR t.name LIKE concat('%', :keyword, '%') " +
            "ORDER BY m.name ASC, t.name ASC")
    List<Member> findByLikeAllField(@Param(value = "keyword") String keyword);

    List<Member> findByNameContaining(String name);

}
