package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


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
    public static final String TABLE_MATCHES = "matchedUsers";
    public static final String COL_MUID = "_muid";


    // Constructor
    public StorageHandler(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
        // Remove commenting for the following if the dbs' schema has changed. REMEMBER TO COMMENT IT OUT AGAIN!
//        onUpgrade(this.getReadableDatabase(),1,1);
    }

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

    public boolean addUser(User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result = -1;

        values.put(COL_USERNAME, u.getUsername());
        values.put(COL_PASSWORD, u.getPassword());
        values.put(COL_FIRSTNAME, u.getFirstName());
        values.put(COL_LASTNAME, u.getLastName());
        values.put(COL_EMAIL, u.getEmail());
        values.put(COL_UNIVERSITY, u.getUniversity());
        values.put(COL_DEPARTMENT, u.getDepartment());

        boolean flag = true;
        for(String v : values.keySet()) { if (values.get(v) == null || values.get(v).equals("")) flag = false; }

        if (flag) result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1 ;
    }

    public boolean updateUser(int ouId, User nu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result = 0;

        values.put(COL_USERNAME, nu.getUsername());
        values.put(COL_PASSWORD, nu.getPassword());
        values.put(COL_FIRSTNAME, nu.getFirstName());
        values.put(COL_LASTNAME, nu.getLastName());
        values.put(COL_EMAIL, nu.getEmail());
        values.put(COL_UNIVERSITY, nu.getUniversity());
        values.put(COL_DEPARTMENT, nu.getDepartment());

        boolean flag = true;
        for(String v : values.keySet()) { if (values.get(v) == null || values.get(v).equals("")) flag = false; }

        if (flag) result = db.update(TABLE_USERS, values,COL_UID + " = ?", new String[] { String.valueOf(ouId) });
        db.close();

        return result > 0 ;
    }

    public boolean deleteUser(String username) {
        User u = fetchUserByUsername(username);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_USERS, COL_UID + " = ?",
                new String[] { String.valueOf(u.getId()) });
        db.close();
        return result > 0;
    }

    @SuppressLint("SimpleDateFormat")
    public void addStudyRequest(StudyRequest sr, User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_UID,u.getId());
        values.put(COL_SUBJECT, sr.getSubject());
        values.put(COL_REASON, sr.getReason());
        values.put(COL_PLACE, sr.getPlace());
        values.put(COL_COMMENTS, sr.getComments());
        values.put(COL_TIME, (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(sr.getDatetime()));
        values.put(COL_PERIOD, sr.getPeriod().getDisplayName());
        values.put(COL_MAX, sr.getMaxMatches());

        boolean flag = true;
        for(String v : values.keySet()) { if (values.get(v) == null || (values.get(v).equals("") && !v.equals("comments") )) flag = false; }

        if (flag) db.insert(TABLE_REQUESTS,null, values);

        Cursor cursor = db.rawQuery("SELECT MAX(" + COL_RID + ") FROM " + TABLE_REQUESTS, null);
        if (cursor.moveToFirst()) {
            sr.setId(cursor.getInt(0));
            FindMatches(sr);
        }
        cursor.close();
        db.close();
    }

    private void FindMatches(StudyRequest sr) {
        ArrayList<StudyRequest> studyRequests = fetchAllStudyRequests();
        for (StudyRequest request : studyRequests) {
            if (sr.getRequestedUser().getId() != request.getRequestedUser().getId()
                    && StudyRequest.matchRequest(sr,request) >= MatchesListActivity.SIMILARITY_THRESHOLD
                    && !sr.isFulfilled() && !request.isFulfilled()) {
                addMatch(sr,request);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void updateStudyRequest(StudyRequest sr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_SUBJECT, sr.getSubject());
        values.put(COL_REASON, sr.getReason());
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

        if (flag) db.update(TABLE_REQUESTS, values, COL_RID + " = ?", new String[]{String.valueOf(sr.getId())});
        db.close();

        FindMatches(sr);
    }

    public void deleteStudyRequest(StudyRequest sr) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REQUESTS, COL_RID + " = ?",
                new String[]{String.valueOf(sr.getId())});
        db.delete(TABLE_MATCHES, COL_RID +"1" + " = ?",
                new String[]{String.valueOf(sr.getId())});
        db.close();
    }


    public void addMatch(StudyRequest s1, StudyRequest s2) {
        ContentValues values = new ContentValues();

        if(fetchMatchesOfStudyRequest(s1).size() < s1.getMaxMatches() && fetchMatchesOfStudyRequest(s2).size() < s2.getMaxMatches()) {
            SQLiteDatabase db = this.getWritableDatabase();
            values.put(COL_RID + "1", s1.getId());
            values.put(COL_RID + "2", s2.getId());
            db.insert(TABLE_MATCHES,null, values);
            db.close();
        }
    }

    private User fetchUserById(int id) {
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
            u.setUniversity(cursor.getString(6));
            u.setDepartment(cursor.getString(7));
           // u.setRequests(fetchStudyRequestsOfUser(u));
            cursor.close();
        } else {
            u = null;
        }
        db.close();

        return u;
    }

    /* To be used for the authentication process */
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
            u.setUniversity(cursor.getString(6));
            u.setDepartment(cursor.getString(7));
            u.setRequests(fetchStudyRequestsOfUser(u));
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
            u.setUniversity(cursor.getString(6));
            u.setDepartment(cursor.getString(7));
            u.setRequests(fetchStudyRequestsOfUser(u));
            cursor.close();
        } else {
            u = null;
        }
        db.close();

        return u;
    }


    public ArrayList<User> fetchAllUsers() {
        String query = "SELECT " + COL_UID + " FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<User> users = new ArrayList<>();
        try { while (cursor.moveToNext()) { users.add(fetchUserById(Integer.parseInt(cursor.getString(0)))); } }
        finally { cursor.close(); }

        db.close();
        return users;
    }

    @SuppressLint("SimpleDateFormat")
    public StudyRequest fetchStudyRequestById(int id) {
        String query = "SELECT * FROM " + TABLE_REQUESTS + " WHERE " +
                COL_RID + " = '" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        StudyRequest sr = new StudyRequest();
        try {
            if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            sr.setId(Integer.parseInt(cursor.getString(0)));
            sr.setRequestedUser(fetchUserById(Integer.parseInt(cursor.getString(1))));
            sr.setSubject(cursor.getString(2));
            sr.setReason(cursor.getString(3));
            sr.setPlace(cursor.getString(4));
            sr.setComments(cursor.getString(5));
            sr.setDatetime((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).parse(cursor.getString(6)));
            sr.setPeriod(PeriodOfStudy.valueOf(cursor.getString(7)));
            sr.setMaxMatches(Integer.parseInt(cursor.getString(8)));
//            sr.setMatchedRequests(fetchMatchesOfStudyRequest(sr));
        } } catch (Exception ignored) { } finally { cursor.close(); }

        db.close();
        return sr;
    }

    @SuppressLint("SimpleDateFormat")
    private ArrayList<StudyRequest> fetchAllStudyRequests() {
        String query = "SELECT * FROM " + TABLE_REQUESTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<StudyRequest> srs = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                StudyRequest sr = new StudyRequest();
                sr.setId(Integer.parseInt(cursor.getString(0)));
                sr.setRequestedUser(fetchUserById(Integer.parseInt(cursor.getString(1))));
                sr.setSubject(cursor.getString(2));
                sr.setReason(cursor.getString(3));
                sr.setPlace(cursor.getString(4));
                sr.setComments(cursor.getString(5));
                sr.setDatetime((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).parse(cursor.getString(6)));
                sr.setPeriod(PeriodOfStudy.getPeriodOfStudy(cursor.getString(7)));
                sr.setMaxMatches(Integer.parseInt(cursor.getString(8)));
                sr.setMatchedRequests(fetchMatchesOfStudyRequest(sr));
                srs.add(sr);
            }
        } catch (Exception ignored) { } finally { cursor.close(); }

        db.close();
        return srs;
    }

    @SuppressLint("SimpleDateFormat")
    public ArrayList<StudyRequest> fetchStudyRequestsOfUser(User user) {
        String query = "SELECT * FROM " + TABLE_REQUESTS + " WHERE " +
                COL_UID + " = '" + user.getId() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<StudyRequest> srs = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                StudyRequest sr = new StudyRequest();
                sr.setId(Integer.parseInt(cursor.getString(0)));
                sr.setRequestedUser(user);
                sr.setSubject(cursor.getString(2));
                sr.setReason(cursor.getString(3));
                sr.setPlace(cursor.getString(4));
                sr.setComments(cursor.getString(5));
                sr.setDatetime((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).parse(cursor.getString(6)));
                sr.setPeriod(PeriodOfStudy.getPeriodOfStudy(cursor.getString(7)));
                sr.setMaxMatches(Integer.parseInt(cursor.getString(8)));
                sr.setMatchedRequests(fetchMatchesOfStudyRequest(sr));
                srs.add(sr);
            }
        } catch (Exception ignored) { } finally { cursor.close(); }

        db.close();
        return srs;
    }

    public ArrayList<StudyRequest> fetchMatchesOfStudyRequest(StudyRequest studyRequest) {
        String query = "SELECT DISTINCT " + COL_RID + "1" + " , " + COL_RID + "2" + " FROM " + TABLE_MATCHES + " WHERE " +
                COL_RID + "1" + " = '" + studyRequest.getId() + "' OR " + COL_RID + "2" + " = '" + studyRequest.getId() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<StudyRequest> requests = new ArrayList<>();
        try { while (cursor.moveToNext()) {
            requests.add(fetchStudyRequestById(Integer.parseInt(cursor.getString(0))));
            requests.add(fetchStudyRequestById(Integer.parseInt(cursor.getString(1))));
        } }
        catch (Exception ignored) { }
        finally { cursor.close(); }

        db.close();
        return requests;
    }

}
