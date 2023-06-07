package com.auth.studywithme;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a user in the application; it's comprised of specific
 * personal details, such as first/last names, username and email,
 * and academic details, such as university and depertment they study at.
 */
public class User implements Serializable {
    private long id;
    private String username, email, password;
    private String firstName, lastName;
    private University university;
    private String department;

    /* Constructors */

    /**
     * Default constructor for the User class.
     */
    public User() { }

    /**
     * Parameterized constructor for the User class.
     *
     * @param username   The username of the user.
     * @param password   The password of the user.
     * @param email      The email of the user.
     * @param firstName  The first name of the user.
     * @param lastName   The last name of the user.
     * @param university The university of the user.
     * @param department The department of the user.
     */
    public User(String username, String password, String email, String firstName, String lastName, University university, String department) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.university = university;
        this.department = department;
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
    public University getUniversity() { return university; }
    public void setUniversity(University university) { this.university = university; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return firstName + " " + lastName; }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
