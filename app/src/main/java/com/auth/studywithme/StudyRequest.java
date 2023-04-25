package com.auth.studywithme;

import java.util.ArrayList;
import java.util.Date;

public class StudyRequest {
    private int id;
    private String subject, reason, place, comments;
    private ArrayList<User> matchedUsers;
    private User requestedUser;
    private Date datetime;
    private PeriodOfStudy period;
    private int maxMatches;

    /* Constructors */
    public StudyRequest() { }

    public StudyRequest(String subject, String reason, String place, String comments, User requestedUser, ArrayList<User> matchedUsers, Date datetime, PeriodOfStudy period, int maxMatches) {
        this.subject = subject;
        this.reason = reason;
        this.place = place;
        this.comments = comments;
        this.requestedUser = requestedUser;
        this.matchedUsers = matchedUsers;
        this.datetime = datetime;
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
    public User getRequestedUser() { return requestedUser; }
    public void setRequestedUser(User requestedUser) { this.requestedUser = requestedUser; }
    public ArrayList<User> getMatchedUsers() { return matchedUsers; }
    public void setMatchedUsers(ArrayList<User> matchedUsers) { this.matchedUsers = matchedUsers; }
    public Date getDatetime() { return datetime; }
    public void setDatetime(Date datetime) { this.datetime = datetime; }
    public PeriodOfStudy getPeriod() { return period; }
    public void setPeriod(PeriodOfStudy period) { this.period = period; }
    public int getMaxMatches() { return maxMatches; }
    public void setMaxMatches(int maxMatches) { this.maxMatches = maxMatches; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
