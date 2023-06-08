package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * The StorageHandler class represents a DB handler that implements
 * the CRUD functionalities affecting the tables and their records.
 */
public class StorageHandler extends SQLiteOpenHelper {
    private Context context;
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
    public static final String TABLE_MATCHES = "matches";
    public static final String COL_MUID = "_muid";


    // Constructor
    public StorageHandler(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
        this.context = context;
    }

    //region Initialization::db
    @Override
    public void onCreate(SQLiteDatabase db) {
      String CREATE_STUDY_REQUESTS_TABLE = "CREATE TABLE " +
                TABLE_REQUESTS + "(" +
                COL_RID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_UID + " INTEGER," +
                COL_SUBJECT + " TEXT," +
                COL_REASON + " TEXT," +
                COL_PLACE + " TEXT," +
                COL_COMMENTS + " TEXT,"+
                COL_TIME + " DATETIME," +
                COL_PERIOD + " TEXT," +
                COL_MAX + " INTEGER" + ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "(" +
                COL_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT," +
                COL_PASSWORD + " TEXT," +
                COL_FIRSTNAME + " TEXT,"+
                COL_LASTNAME + " TEXT," +
                COL_EMAIL + " TEXT," +
                COL_UNIVERSITY + " TEXT," +
                COL_DEPARTMENT + " TEXT" + ")";

        String CREATE_MATCHES_TABLE = "CREATE TABLE " +
                TABLE_MATCHES + "(" +
                COL_MUID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_RID + "1" + " INTEGER," +
                COL_RID + "2" + " TEXT," +
                "FOREIGN KEY " + "(" + COL_RID + "2" + ")" +
                " REFERENCES " + TABLE_REQUESTS + "(" + COL_RID + "), " +
                "FOREIGN KEY " + "(" + COL_RID + "2" + ")" +
                " REFERENCES " + TABLE_REQUESTS + "(" + COL_RID + ") " + ")";

        db.execSQL(CREATE_STUDY_REQUESTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_MATCHES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        onCreate(db);
    }
    //endregion

    //region User::data functionalities
    public boolean addUser(User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result = -1;

        values.put(COL_USERNAME, u.getUsername());
        values.put(COL_PASSWORD, u.getPassword());
        values.put(COL_FIRSTNAME, u.getFirstName());
        values.put(COL_LASTNAME, u.getLastName());
        values.put(COL_EMAIL, u.getEmail());
        values.put(COL_UNIVERSITY, University.getUniversityName(context,u.getUniversity()));
        values.put(COL_DEPARTMENT, u.getDepartment());

        boolean flag = true;
        for (String v : values.keySet()) {
            if (values.get(v) == null || values.get(v).equals("")) flag = false;
        }

        if (flag) result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    public boolean updateUser(long ouId, User nu){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result = 0;

        values.put(COL_USERNAME, nu.getUsername());
        values.put(COL_PASSWORD, nu.getPassword());
        values.put(COL_FIRSTNAME, nu.getFirstName());
        values.put(COL_LASTNAME, nu.getLastName());
        values.put(COL_EMAIL, nu.getEmail());
        values.put(COL_UNIVERSITY, University.getUniversityName(context,nu.getUniversity()));
        values.put(COL_DEPARTMENT, nu.getDepartment());

        boolean flag = true;
        for (String v : values.keySet()) {
            if (values.get(v) == null || values.get(v).equals("")) flag = false;
        }

        if (flag)
            result = db.update(TABLE_USERS, values, COL_UID + " = ?", new String[]{String.valueOf(ouId)});
        db.close();

        return result > 0;
    }

    public boolean deleteUser(String username){
        User u = fetchUserByUsername(username);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_USERS, COL_UID + " = ?",
                new String[]{String.valueOf(u.getId())});
        db.close();

        if (result > 0)
           for(Integer i : fetchStudyRequestsOfUser(u.getId()))
               deleteStudyRequest(i);

        return result > 0;
    }

    public User fetchUserById(long id) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_UID + " = '" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        User u = new User();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            u.setId(Integer.parseInt(cursor.getString(0)));
            u.setUsername(cursor.getString(1));
            u.setPassword(cursor.getString(2));
            u.setFirstName(cursor.getString(3));
            u.setLastName(cursor.getString(4));
            u.setEmail(cursor.getString(5));
            u.setUniversity(University.getUniversity(context,cursor.getString(6)));
            u.setDepartment(cursor.getString(7));
            cursor.close();
        } else {
            u = null;
        }
        db.close();

        return u;
    }

    public User fetchUserByUsername(String username) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USERNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        User u = new User();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            u.setId(Integer.parseInt(cursor.getString(0)));
            u.setUsername(cursor.getString(1));
            u.setPassword(cursor.getString(2));
            u.setFirstName(cursor.getString(3));
            u.setLastName(cursor.getString(4));
            u.setEmail(cursor.getString(5));
            u.setUniversity(University.getUniversity(context,cursor.getString(6)));
            u.setDepartment(cursor.getString(7));
            cursor.close();
        } else {
            u = null;
        }
        db.close();

        return u;
    }

    public User fetchUserByCredentials(String username, String password) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USERNAME + " = '" + username + "' AND " +
                COL_PASSWORD + " = '" + password + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        User u = new User();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            u.setId(Integer.parseInt(cursor.getString(0)));
            u.setUsername(cursor.getString(1));
            u.setPassword(cursor.getString(2));
            u.setFirstName(cursor.getString(3));
            u.setLastName(cursor.getString(4));
            u.setEmail(cursor.getString(5));
            u.setUniversity(University.getUniversity(context,cursor.getString(6)));
            u.setDepartment(cursor.getString(7));
            cursor.close();
        } else {
            u = null;
        }
        db.close();

        return u;
    }

    //endregion

    //region StudyRequest::data functionalities
    @SuppressLint("SimpleDateFormat")
    public void addStudyRequest(StudyRequest sr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_UID, sr.getRequestedUserId());
        values.put(COL_SUBJECT, sr.getSubject());
        values.put(COL_REASON, ReasonOfStudy.getReasonName(context,sr.getReason()));
        values.put(COL_PLACE, sr.getPlace());
        values.put(COL_COMMENTS, sr.getComments());
        values.put(COL_TIME, (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(sr.getDatetime()));
        values.put(COL_PERIOD, sr.getPeriod().getDisplayName());
        values.put(COL_MAX, sr.getMaxMatches());

        boolean flag = true;
        for(String v : values.keySet()) { if (values.get(v) == null || (values.get(v).equals("") && !v.equals("comments") )) flag = false; }

        if (flag) db.insert(TABLE_REQUESTS,null, values);
        db.close();

        ArrayList<Integer> tmp = fetchStudyRequestsOfUser(sr.getRequestedUserId());
        FindMatches(tmp.get(tmp.size() - 1));
    }

    @SuppressLint("SimpleDateFormat")
    public void updateStudyRequest(long osrId, StudyRequest sr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_SUBJECT, sr.getSubject());
        values.put(COL_REASON, ReasonOfStudy.getReasonName(context,sr.getReason()));
        values.put(COL_PLACE, sr.getPlace());
        values.put(COL_COMMENTS, sr.getComments());
        values.put(COL_TIME, (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(sr.getDatetime()));
        values.put(COL_PERIOD, sr.getPeriod().getDisplayName());
        values.put(COL_MAX, sr.getMaxMatches());

        boolean flag = true;
        for (String v : values.keySet()) {
            if (values.get(v) == null || (values.get(v).equals("") && !v.equals(COL_COMMENTS)))
                flag = false;
        }

        if (flag) db.update(TABLE_REQUESTS, values, COL_RID + " = ?", new String[]{String.valueOf(osrId)});
        db.close();

        FindMatches(osrId);
    }

    public void deleteStudyRequest(long srId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REQUESTS, COL_RID + " = ?",
                new String[]{String.valueOf(srId)});
        db.delete(TABLE_MATCHES, COL_RID + "1" + " = ? OR " + COL_RID + "2" + " =? ",
                new String[]{String.valueOf(srId)});
        db.close();
    }

    @SuppressLint("SimpleDateFormat")
    public StudyRequest fetchStudyRequestById(long id) {
        String query = "SELECT * FROM " + TABLE_REQUESTS + " WHERE " +
                COL_RID + " = '" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        StudyRequest sr = new StudyRequest();
        try {
            if(cursor.moveToFirst()) {
            sr.setId(Integer.parseInt(cursor.getString(0)));
            sr.setRequestedUserId(Integer.parseInt(cursor.getString(1)));
            sr.setSubject(cursor.getString(2));
            sr.setReason(ReasonOfStudy.getReason(context,cursor.getString(3)));
            sr.setPlace(cursor.getString(4));
            sr.setComments(cursor.getString(5));
            sr.setDatetime((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).parse(cursor.getString(6)));
            sr.setPeriod(PeriodOfStudy.getPeriodOfStudy(cursor.getString(7)));
            sr.setMaxMatches(Integer.parseInt(cursor.getString(8)));
        } } catch (Exception ignored) { } finally { cursor.close(); }

        db.close();
        return sr;
    }

    @SuppressLint("SimpleDateFormat")
    private ArrayList<Integer> fetchAllStudyRequests() {
        String query = "SELECT " + COL_RID + " FROM " + TABLE_REQUESTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<Integer> requestsIds = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                requestsIds.add(Integer.parseInt(cursor.getString(0)));
            }
        } catch (Exception ignored) { } finally { cursor.close(); }

        db.close();
        return requestsIds;
    }

    @SuppressLint("SimpleDateFormat")
    public ArrayList<Integer> fetchStudyRequestsOfUser(long uId) {
        String query = "SELECT * FROM " + TABLE_REQUESTS + " WHERE " +
                COL_UID + " = '" + uId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<Integer> srs = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                srs.add(Integer.parseInt(cursor.getString(0)));
            }
        } catch (Exception ignored) { }
        finally { cursor.close(); }

        db.close();
        return srs;
    }
    //endregion

    //region Matches::data functionalities
    public void addMatch(long srId1, long srId2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_RID + "1", srId1);
        values.put(COL_RID + "2", srId2);
        db.insert(TABLE_MATCHES,null, values);
        db.close();
    }

    public ArrayList<Integer> fetchMatchesOfStudyRequest(long srId) {
        String query = "SELECT DISTINCT " + COL_RID + "1" + " , " + COL_RID + "2" + " FROM " + TABLE_MATCHES + " WHERE " +
                COL_RID + "1" + " = '" + srId + "' OR " + COL_RID + "2" + " = '" + srId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<Integer> requestsIds = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                requestsIds.add(Integer.parseInt(cursor.getString(0)));
                requestsIds.add(Integer.parseInt(cursor.getString(1)));
            }
        }
        catch (Exception ignored) { }
        finally { cursor.close(); }

        db.close();
        return requestsIds;
    }

    public boolean isStudyRequestMatched(long srId) {
        return fetchMatchesOfStudyRequest(srId) != null && fetchMatchesOfStudyRequest(srId).size() > 0;
    }

    public boolean isStudyRequestFulfilled(long srId) {
        return isStudyRequestMatched(srId) && fetchStudyRequestById(srId).getMaxMatches() <= fetchMatchesOfStudyRequest(srId).size();
    }

    private void FindMatches(long srId) {
        StudyRequest s1 = fetchStudyRequestById(srId);

        ArrayList<StudyRequest> allStudyRequests = new ArrayList<>();
        for (Integer id : fetchAllStudyRequests()) {
            allStudyRequests.add(fetchStudyRequestById(id));
        }

        for (StudyRequest s2 : allStudyRequests) {
            if (s1.getRequestedUserId() != s2.getRequestedUserId()
                    && StudyRequest.matchRequest(s1,s2) >= MatchesListActivity.SIMILARITY_THRESHOLD
                    && !isStudyRequestFulfilled(s1.getId()) && !isStudyRequestMatched(s2.getId())) {
                addMatch(s1.getId(),s2.getId());
            }
        }
    }
    //endregion
}
