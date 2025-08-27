package com.ra.model.entity;

import lombok.Getter;

@Getter
public enum EnrollmentStatus {
    WAITING("WAITING"),
    DENIED("DENIED"),
    CANCEL("CANCEL"),
    CONFIRM("CONFIRM");

    private final String displayName;

    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }

}
