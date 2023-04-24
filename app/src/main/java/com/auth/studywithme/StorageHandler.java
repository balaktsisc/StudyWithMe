package com.auth.studywithme;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class StorageHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudyWithMe.db";

    // StudyRequests table
    public static final String TABLE_REQUESTS = "requests";
    public static final String COL_RID = "_rid";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_REASON = "reason";
    public static final String COL_PLACE = "place";
    public static final String COL_COMMENTS = "comments";
    public static final String COL_TIME = "time";
    public static final String COL_PERIOD = "period";
    public static final String COL_MAX = "maxMatches";

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COL_UID = "_uid";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_EMAIL = "email";
    public static final String COL_FIRSTNAME = "firstName";
    public static final String COL_LASTNAME = "lastName";
    public static final String COL_UNIVERSITY = "university";
    public static final String COL_DEPARTMENT = "department";


    // Requested Users table
    public static final String TABLE_REQ_USERS = "requestedUsers";
    public static final String COL_RU_ID = "_ruid";
    /* public static final String COL_UID = "_uid"; */
    /* public static final String COL_RID = "_rid"; */


    // Matched Users table
    public static final String TABLE_MAT_USERS = "matchedUsers";
    public static final String COL_MU_ID = "_muid";
    /* public static final String COL_UID = "_uid"; */
    /* public static final String COL_RID = "_rid"; */


    // Constructor
    public StorageHandler(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDY_REQUESTS_TABLE = "CREATE TABLE " +
                TABLE_REQUESTS + "(" +
                COL_RID + " INTEGER PRIMARY KEY," +
                COL_SUBJECT + " TEXT," +
                COL_REASON + " TEXT," +
                COL_PLACE + " TEXT," +
                COL_COMMENTS + " TEXT,"+
                COL_TIME + " DATETIME," +
                COL_PERIOD + " TEXT," +
                COL_MAX + " INTEGER" + ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "(" +
                COL_UID + " INTEGER PRIMARY KEY," +
                COL_USERNAME + " TEXT," +
                COL_PASSWORD + " TEXT," +
                COL_EMAIL + " TEXT," +
                COL_FIRSTNAME + " TEXT,"+
                COL_LASTNAME + " TEXT," +
                COL_UNIVERSITY + " TEXT," +
                COL_DEPARTMENT + " TEXT" + ")";

        String CREATE_REQ_USERS_TABLE = "CREATE TABLE " +
                TABLE_REQ_USERS + "(" +
                COL_RU_ID + " INTEGER PRIMARY KEY," +
                COL_RID + " INTEGER," +
                COL_UID + " INTEGER," +
                "FOREIGN KEY " + "(" + COL_RID + ")" +
                " REFERENCES " + TABLE_REQUESTS + "(" + COL_RID + "), " +
                "FOREIGN KEY " + "(" + COL_UID + ")" +
                " REFERENCES " + TABLE_USERS + "(" + COL_UID + ") " + ")";

        String CREATE_MAT_USERS_TABLE = "CREATE TABLE " +
                TABLE_MAT_USERS + "(" +
                COL_MU_ID + " INTEGER PRIMARY KEY," +
                COL_RID + " INTEGER," +
                COL_UID + " INTEGER," +
                "FOREIGN KEY " + "(" + COL_RID + ")" +
                " REFERENCES " + TABLE_REQUESTS + "(" + COL_RID + "), " +
                "FOREIGN KEY " + "(" + COL_UID + ")" +
                " REFERENCES " + TABLE_USERS + "(" + COL_UID + ") " + ")";

        db.execSQL(CREATE_STUDY_REQUESTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_REQ_USERS_TABLE);
        db.execSQL(CREATE_MAT_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQ_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAT_USERS);
        onCreate(db);
    }

    // Methods for updating records of tables of the DB

}
