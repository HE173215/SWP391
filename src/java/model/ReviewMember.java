/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class ReviewMember {

    private int userId;
    private int councilId;
    private String role;

    // Constructor
    public ReviewMember(int userId, int councilId, String role) {
        this.userId = userId;
        this.councilId = councilId;
        this.role = role;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCouncilId() {
        return councilId;
    }

    public void setCouncilId(int councilId) {
        this.councilId = councilId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ReviewMember{"
                + "userId=" + userId
                + ", councilId=" + councilId
                + ", role='" + role + '\''
                + '}';
    }
}
