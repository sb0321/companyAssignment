package com.assign.organization.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    void deleteById(Long id);

    int deleteByName(String name);

    boolean findNameExist(String name);
}
