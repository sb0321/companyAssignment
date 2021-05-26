package com.assign.organization.domain.duplication;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Duplication {

    @Id
    private String objectName;

    @Column(nullable = false)
    private Long duplicationCount = 0L;

    public Duplication(String objectName) {
        this.objectName = objectName;
    }

    public void increaseCount() {
        duplicationCount++;
    }
}
