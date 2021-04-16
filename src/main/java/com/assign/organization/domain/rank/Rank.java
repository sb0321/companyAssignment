package com.assign.organization.domain.rank;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class Rank {

    @Id
    @GeneratedValue
    @Column(name = "RANK_ID")
    private Long id;

    private String name;

    @Builder
    public Rank(String name) {
        this.name = name;
    }
}