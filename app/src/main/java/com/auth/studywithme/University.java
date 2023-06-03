package com.auth.studywithme;

import android.content.Context;

import java.io.Serializable;

public enum University implements Serializable {
    AUTH, IHU, UOM;

    public static String getUniversityName(Context context, University uni) {
        switch (uni) {
            case AUTH:
                return context.getString(R.string.auth);
            case IHU:
                return context.getString(R.string.ihu);
            case UOM:
                return context.getString(R.string.uom);
            default:
                return null;
        }
    }

    public static University getUniversity(Context context, String uniName) {
        if(uniName.equals(context.getString(R.string.auth)))
            return University.AUTH;
        else if (uniName.equals(context.getString(R.string.ihu)))
            return University.IHU;
        else if (uniName.equals(context.getString(R.string.uom)))
            return University.UOM;
        else
            return null;
    }
}
