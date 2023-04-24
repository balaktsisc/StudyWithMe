package com.auth.studywithme;



// StudyRequest {requested-users[], matched-users[], subject, reason, place, time, PeriodOfStudy, max-num-of-matches}

import java.util.ArrayList;
import java.util.Date;

public class StudyRequest {
    private String subject, reason, place, comments;
    private ArrayList<User> requestedUsers, matchedUsers;
    private Date time;
    private PeriodOfStudy period;
    private int maxMatches;

    /* Main Constructor */
    public StudyRequest(String subject, String reason, String place, String comments, ArrayList<User> requestedUsers, ArrayList<User> matchedUsers, Date time, PeriodOfStudy period, int maxMatches) {
        this.subject = subject;
        this.reason = reason;
        this.place = place;
        this.comments = comments;
        this.requestedUsers = requestedUsers;
        this.matchedUsers = matchedUsers;
        this.time = time;
        this.period = period;
        this.maxMatches = maxMatches;
    }

    /* Setters and Getters */
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public ArrayList<User> getRequestedUsers() { return requestedUsers; }
    public void setRequestedUsers(ArrayList<User> requestedUsers) { this.requestedUsers = requestedUsers; }
    public ArrayList<User> getMatchedUsers() { return matchedUsers; }
    public void setMatchedUsers(ArrayList<User> matchedUsers) { this.matchedUsers = matchedUsers; }
    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }
    public PeriodOfStudy getPeriod() { return period; }
    public void setPeriod(PeriodOfStudy period) { this.period = period; }
    public int getMaxMatches() { return maxMatches; }
    public void setMaxMatches(int maxMatches) { this.maxMatches = maxMatches; }
}
