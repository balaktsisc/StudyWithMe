package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;


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

    // Matched Users table
    public static final String TABLE_MAT_USERS = "matchedUsers";
    public static final String COL_MU_ID = "_muid";


    // Constructor
    public StorageHandler(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDY_REQUESTS_TABLE = "CREATE TABLE " +
                TABLE_REQUESTS + "(" +
                COL_RID + " INTEGER PRIMARY KEY," +
                COL_USERNAME + " TEXT," +
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

        String CREATE_MAT_USERS_TABLE = "CREATE TABLE " +
                TABLE_MAT_USERS + "(" +
                COL_MU_ID + " INTEGER PRIMARY KEY," +
                COL_RID + " INTEGER," +
                COL_UID + " TEXT," +
                "FOREIGN KEY " + "(" + COL_RID + ")" +
                " REFERENCES " + TABLE_REQUESTS + "(" + COL_RID + "), " +
                "FOREIGN KEY " + "(" + COL_UID + ")" +
                " REFERENCES " + TABLE_USERS + "(" + COL_UID + ") " + ")";

        db.execSQL(CREATE_STUDY_REQUESTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_MAT_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAT_USERS);
        onCreate(db);
    }


    // Methods for updating records of tables of the DB
    private int getIdFromQuery(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst())
            cursor.moveToFirst();
        db.close();
        return Integer.parseInt(cursor.getString(0));
    }
    public int getUid(String username) {
        return getIdFromQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USERNAME + " = '" + username + "'");
    }
    public int getRid(String username, String time) {
        return getIdFromQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USERNAME + " = '" + username + "' AND " + COL_TIME + " = '" + time + "'");
    }

    public void addUser(@NonNull User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_USERNAME, u.getUsername());
        values.put(COL_PASSWORD, u.getPassword());
        values.put(COL_EMAIL, u.getEmail());
        values.put(COL_FIRSTNAME, u.getFirstName());
        values.put(COL_LASTNAME, u.getLastName());
        values.put(COL_UNIVERSITY, u.getUniversity());
        values.put(COL_DEPARTMENT, u.getDepartment());

        db.insert(TABLE_USERS,null, values);
        db.close();
    }

    public void addStudyRequest(@NonNull StudyRequest sr, @NonNull User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_USERNAME,u.getUsername());
        values.put(COL_SUBJECT, sr.getSubject());
        values.put(COL_REASON, sr.getReason());
        values.put(COL_PLACE, sr.getPlace());
        values.put(COL_COMMENTS, sr.getComments());
        values.put(COL_TIME, sr.getTime().toString());
        values.put(COL_PERIOD, sr.getPeriod().name());
        values.put(COL_MAX, sr.getMaxMatches());

        db.insert(TABLE_REQUESTS,null, values);
        db.close();
    }

    public void addMatch(@NonNull StudyRequest sr, @NonNull User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_UID,getUid(u.getUsername()));
        values.put(COL_RID,getRid(u.getUsername(),sr.getTime().toString()));

        db.insert(TABLE_REQUESTS,null, values);
        db.close();
    }

}
