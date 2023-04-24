package com.auth.studywithme;

import java.util.ArrayList;

public class User {
    private String username, email, password;
    private String firstName, lastName;
    private String university, department;
    private ArrayList<StudyRequest> requests;

    /* Main Constructor */
    public User(String username, String password, String email, String firstName, String lastName, String university, String department) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.university = university;
        this.department = department;
        this.requests = new ArrayList<>();
    }

    /* Setters and Getters */
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public ArrayList<StudyRequest> getRequests() { return requests; }
    public void setRequests(ArrayList<StudyRequest> requests) { this.requests = requests; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


}
