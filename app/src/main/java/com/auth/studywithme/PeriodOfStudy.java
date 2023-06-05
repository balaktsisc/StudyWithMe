package com.auth.studywithme;

import java.io.Serializable;
import java.util.Objects;

/**
 * The PeriodOfStudy enumeration represents the different periods of study for a study request.
 * Each period has a display name that can be used for user interface purposes.
 */
public enum PeriodOfStudy implements Serializable {
    ONCE("Once"),
    TWICE("Twice"),
    SOMETIMES("Sometimes/Often"),
    ONE_WEEK("One week"),
    TWO_WEEKS("Two weeks"),
    SEMESTER("Whole semester"),
    OTHER("Other");

    private final String displayName;

    /**
     * Constructs a PeriodOfStudy enum constant with the specified display name.
     *
     * @param displayName the display name of the period of study
     */
    PeriodOfStudy(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the period of study.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the PeriodOfStudy enum constant that matches the specified display name.
     *
     * @param displayName the display name to match
     * @return the PeriodOfStudy enum constant, or null if no match is found
     */
    public static PeriodOfStudy getPeriodOfStudy(String displayName) {
        for (PeriodOfStudy p : PeriodOfStudy.values()) {
            if (Objects.equals(p.displayName, displayName))
                return p;
        }
        return null;
    }
}
