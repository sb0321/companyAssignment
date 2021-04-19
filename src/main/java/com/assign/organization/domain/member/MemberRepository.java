package com.assign.organization.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    void deleteById(Long id);
    int deleteByName(String name);

}
