package com.auth.studywithme;

import android.content.Context;

import java.io.Serializable;

public enum ReasonOfStudy implements Serializable {
    TEST, EXAM, REGULAR, PROJECT;

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

    public static ReasonOfStudy getReason(Context context, String r) {
        if(r.equals(context.getString(R.string.test)))
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
