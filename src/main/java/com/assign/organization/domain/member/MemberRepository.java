package com.assign.organization.domain.member;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

}
