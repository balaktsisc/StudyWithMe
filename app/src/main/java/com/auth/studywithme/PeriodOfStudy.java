package com.auth.studywithme;

import java.io.Serializable;
import java.util.Objects;

public enum PeriodOfStudy implements Serializable {
    ONCE("Once"), TWICE("Twice"), SOMETIMES("Sometimes/Often"), ONE_WEEK("One week"),
    TWO_WEEKS("Two weeks"), SEMESTER("Whole semester"), OTHER("Other");
    private final String displayName;

    PeriodOfStudy(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PeriodOfStudy getPeriodOfStudy(String displayName) {
        for (PeriodOfStudy p : PeriodOfStudy.values()) {
            if (Objects.equals(p.displayName, displayName))
                return p;
        }
        return null;
    }
}
