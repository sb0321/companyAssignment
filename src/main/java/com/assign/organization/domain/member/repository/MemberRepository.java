package com.assign.organization.domain.member.repository;

import com.assign.organization.domain.member.Member;
import org.springframework.data.repository.CrudRepository;


public interface MemberRepository extends CrudRepository<Member, Long>, MemberRepositoryCustom {
}
