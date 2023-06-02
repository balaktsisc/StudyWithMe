package com.auth.studywithme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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
        return matchedRequests == null || matchedRequests.size() > 0;
    }

    public boolean isFulfilled() { return matchedRequests.size() >= maxMatches; }

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

    public static double matchRequest(StudyRequest r1, StudyRequest r2){

        double subjectSim, placeSim, dateSim, reasonSim, periodSim;

        subjectSim = CalculateSimilarity(r1.getSubject(), r2.getSubject()) * 0.4;

        placeSim = CalculateSimilarity(r1.getPlace(), r2.getPlace()) * 0.2;

        if (datesDiff(r1.getDatetime(), r2.getDatetime()) <= 7)
            dateSim = 0.2;
        else
            dateSim = 0.1;

        if (Objects.equals(r1.getReason(), r2.getReason()))
            reasonSim = 0.1;
        else
            reasonSim = 0;

        if (r1.getPeriod() == r2.getPeriod())
            periodSim = 0.1;
        else
            periodSim = 0;

        return subjectSim + placeSim + dateSim + reasonSim + periodSim;

    }

    private static long datesDiff(Date startDate, Date endDate) {
        // Convert the Date objects to Calendar objects
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        long millisecondsPerDay = 24 * 60 * 60 * 1000;
        long startMillis = startCalendar.getTimeInMillis();
        long endMillis = endCalendar.getTimeInMillis();

        return (endMillis - startMillis) / millisecondsPerDay;
    }

    private static double CalculateSimilarity(String w1, String w2) {
        if (w1 == null || w2 == null) { return 0; }

        double maxLength = Math.max(w1.length(), w2.length());
        if (maxLength > 0) {

            int m = w1.length();
            int n = w2.length();

            int[][] T = new int[m + 1][];
            for (int i = 0; i < m + 1; ++i)
                T[i] = new int[n + 1];

            for (int i = 1; i <= m; i++)
                T[i][0] = i;

            for (int j = 1; j <= n; j++)
                T[0][j] = j;

            int cost;
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    cost = w1.charAt(i - 1) == w2.charAt(j - 1) ? 0 : 1;
                    T[i][j] = Math.min(Math.min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                            T[i - 1][j - 1] + cost);
                }
            }

            return (maxLength - T[m][n]) / maxLength;
        }
        return 1.0;
    }
}
