package com.assign.organization.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    int deleteByName(String name);

    @Query("SELECT m FROM Member as m WHERE m.name LIKE '%:keyword%' OR m.contact.businessCall = '%:keyword%' OR m.contact.cellPhone = '%:keyword%' ORDER BY m.name ASC")
    List<Member> findByLikeAllField(@Param("keyword") String keyword);

}
