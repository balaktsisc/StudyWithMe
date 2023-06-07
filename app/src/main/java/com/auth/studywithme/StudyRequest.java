package com.auth.studywithme;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * The StudyRequest class represents a StudyRequest; an request posted by users
 * that contains information about a subject to study, a place, a reason, a frequency
 * and the maximun number of people they want to study with.
 */
public class StudyRequest implements Serializable {
    private long id;
    private String subject, place, comments;
    private ReasonOfStudy reason;
    private long requestedUserId;
    private Date datetime;
    private PeriodOfStudy period;
    private int maxMatches;

    /**
     * Default constructor for the StudyRequest class.
     */
    public StudyRequest() { }

    /**
     * Parameterized constructor for the StudyRequest class.
     *
     * @param subject        the subject of the study request
     * @param reason         the reason for the study request
     * @param place          the place of the study request
     * @param comments       additional comments for the study request
     * @param datetime       the date and time of the study request
     * @param period         the period of study for the request
     * @param maxMatches     the maximum number of matches allowed for the study request
     */
    public StudyRequest(String subject, ReasonOfStudy reason, String place, String comments, Date datetime, PeriodOfStudy period, int maxMatches) {
        this.subject = subject;
        this.reason = reason;
        this.place = place;
        this.comments = comments;
        this.datetime = datetime;
        this.period = period;
        this.maxMatches = maxMatches;
    }

    /**
     * Retrieves the subject of the study request.
     *
     * @return the subject of the study request
     */
    public String getSubject() { return subject; }

    /**
     * Sets the subject of the study request.
     *
     * @param subject the subject of the study request
     */
    public void setSubject(String subject) { this.subject = subject; }

    /**
     * Retrieves the reason for the study request.
     *
     * @return the reason for the study request
     */
    public ReasonOfStudy getReason() { return reason; }

    /**
     * Sets the reason for the study request.
     *
     * @param reason the reason for the study request
     */
    public void setReason(ReasonOfStudy reason) { this.reason = reason; }

    /**
     * Retrieves the place of the study request.
     *
     * @return the place of the study request
     */
    public String getPlace() { return place; }

    /**
     * Sets the place of the study request.
     *
     * @param place the place of the study request
     */
    public void setPlace(String place) { this.place = place; }

    /**
     * Retrieves the additional comments for the study request.
     *
     * @return the additional comments for the study request
     */
    public String getComments() { return comments; }

    /**
     * Sets the additional comments for the study request.
     *
     * @param comments the additional comments for the study request
     */
    public void setComments(String comments) { this.comments = comments; }

    /**
     * Retrieves the ID of the user who requested the study.
     *
     * @return the ID of the user who requested the study
     */
    public long getRequestedUserId() { return requestedUserId; }

    /**
     * Sets the ID of the user who requested the study.
     *
     * @param requestedUserId the ID of the user who requested the study
     */
    public void setRequestedUserId(long requestedUserId) { this.requestedUserId = requestedUserId; }

    /**
     * Retrieves the date and time of the study request.
     *
     * @return the date and time of the study request
     */
    public Date getDatetime() { return datetime; }

    /**
     * Sets the date and time of the study request.
     *
     * @param datetime the date and time of the study request
     */
    public void setDatetime(Date datetime) { this.datetime = datetime; }

    /**
     * Retrieves the period of study for the request.
     *
     * @return the period of study for the request
     */
    public PeriodOfStudy getPeriod() { return period; }

    /**
     * Sets the period of study for the request.
     *
     * @param period the period of study for the request
     */
    public void setPeriod(PeriodOfStudy period) { this.period = period; }

    /**
     * Retrieves the maximum number of matches allowed for the study request.
     *
     * @return the maximum number of matches allowed for the study request
     */
    public int getMaxMatches() { return maxMatches; }

    /**
     * Sets the maximum number of matches allowed for the study request.
     *
     * @param maxMatches the maximum number of matches allowed for the study request
     */
    public void setMaxMatches(int maxMatches) { this.maxMatches = maxMatches; }

    /**
     * Retrieves the ID of the study request.
     *
     * @return the ID of the study request
     */
    public long getId() { return id; }

    /**
     * Sets the ID of the study request.
     *
     * @param id the ID of the study request
     */
    public void setId(long id) { this.id = id; }

    /**
     * Calculates the similarity between two study requests.
     *
     * @param r1 the first study request
     * @param r2 the second study request
     * @return the similarity score between the study requests
     */
    public static double matchRequest(StudyRequest r1, StudyRequest r2) {

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

    /**
     * Calculates the difference in days between two dates.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return the difference in days between the dates
     */
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

    /**
     * Calculates the similarity between two strings using the Levenshtein distance algorithm.
     *
     * @param w1 the first string
     * @param w2 the second string
     * @return the similarity score between the strings
     */
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
