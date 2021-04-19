package com.assign.organization.domain.ranked;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class Ranked {

    @Id
    @GeneratedValue
    @Column(name = "RANKED_ID")
    private Long id;

    private String name;

    public void updateRankedName(String name) {
        this.name = name;
    }

    @Builder
    public Ranked(String name) {
        this.name = name;
    }
}