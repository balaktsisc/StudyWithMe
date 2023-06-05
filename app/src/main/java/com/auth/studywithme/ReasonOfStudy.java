package com.auth.studywithme;

import android.content.Context;

import java.io.Serializable;

/**
 * The ReasonOfStudy enumeration represents the different reasons for studying in a study request.
 */
public enum ReasonOfStudy implements Serializable {
    TEST,
    EXAM,
    REGULAR,
    PROJECT;

    /**
     * Returns the localized name of the reason of study based on the current context.
     *
     * @param context the context used for retrieving the localized string
     * @param r the reason of study
     * @return the localized name of the reason of study
     */
    public static String getReasonName(Context context, ReasonOfStudy r) {
        switch (r) {
            case TEST:
                return context.getString(R.string.test);
            case EXAM:
                return context.getString(R.string.exam);
            case PROJECT:
                return context.getString(R.string.project);
            case REGULAR:
                return context.getString(R.string.regular);
            default:
                return null;
        }
    }

    /**
     * Returns the ReasonOfStudy enum constant based on the localized reason string.
     *
     * @param context the context used for retrieving the localized string
     * @param r the localized reason string
     * @return the ReasonOfStudy enum constant, or null if no match is found
     */
    public static ReasonOfStudy getReason(Context context, String r) {
        if (r.equals(context.getString(R.string.test)))
            return ReasonOfStudy.TEST;
        else if (r.equals(context.getString(R.string.exam)))
            return ReasonOfStudy.EXAM;
        else if (r.equals(context.getString(R.string.project)))
            return ReasonOfStudy.PROJECT;
        else if (r.equals(context.getString(R.string.regular)))
            return ReasonOfStudy.REGULAR;
        else
            return null;
    }
}
