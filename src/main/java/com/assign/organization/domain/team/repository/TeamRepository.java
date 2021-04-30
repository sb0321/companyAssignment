package com.assign.organization.domain.team.repository;

import com.assign.organization.domain.team.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long>, CustomTeamRepository {

}
