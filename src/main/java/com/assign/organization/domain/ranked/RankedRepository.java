package com.assign.organization.domain.ranked;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RankedRepository extends JpaRepository<Ranked, Long> {

    void deleteById(Long id);
}
