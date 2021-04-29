package com.assign.organization.domain.team;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long>, QuerydslPredicateExecutor<Team> {

}
