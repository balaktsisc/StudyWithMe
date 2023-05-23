package com.auth.studywithme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class StudyRequest  implements Serializable {
    private int id;
    private String subject, reason, place, comments;
    private ArrayList<StudyRequest> matchedRequests;
    private User requestedUser;
    private Date datetime;
    private PeriodOfStudy period;
    private int maxMatches;

    /* Constructors */
    public StudyRequest() { }

    public StudyRequest(String subject, String reason, String place, String comments, Date datetime, PeriodOfStudy period, int maxMatches) {
        this.subject = subject;
        this.reason = reason;
        this.place = place;
        this.comments = comments;
        this.matchedRequests = new ArrayList<>();
        this.datetime = datetime;
        this.period = period;
        this.maxMatches = maxMatches;
    }

    public boolean isMatched() {
        return true ;//matchedRequests.size() > 0;
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
    public ArrayList<StudyRequest> getMatchedRequests() { return matchedRequests; }
    public void setMatchedRequests(ArrayList<StudyRequest> matchedRequests) { this.matchedRequests = matchedRequests; }
    public Date getDatetime() { return datetime; }
    public void setDatetime(Date datetime) { this.datetime = datetime; }
    public PeriodOfStudy getPeriod() { return period; }
    public void setPeriod(PeriodOfStudy period) { this.period = period; }
    public int getMaxMatches() { return maxMatches; }
    public void setMaxMatches(int maxMatches) { this.maxMatches = maxMatches; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
