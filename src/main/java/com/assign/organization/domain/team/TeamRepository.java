package com.assign.organization.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long> {

    void deleteById(Long id);

    int deleteByName(String name);

    @Query("SELECT count(m.name) from Member as m where m.name = :name")
    int findNameExist(@Param("name") String name);
}
