/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Do Duan
 */
public class Requirement {

    private int id;
    private String tittle;
    private String description;
    private int status;
    private String team;
    private int teamID;
    private int complexityID;
    private String complexity;
    private int complexValue;
    private String owner;
    private int ownerID;
    
    private int creatorID;
    private String creator;
    
    private String milestoneID;
    private String milestone;

    public int getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    

    public String getMilestoneID() {
        return milestoneID;
    }

    public void setMilestoneID(String milestoneID) {
        this.milestoneID = milestoneID;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }
    
    

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
    
    

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public int getComplexityID() {
        return complexityID;
    }

    public void setComplexityID(int complexityID) {
        this.complexityID = complexityID;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public int getComplexValue() {
        return complexValue;
    }

    public void setComplexValue(int complexValue) {
        this.complexValue = complexValue;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Requirement{" + "id=" + id + ", tittle=" + tittle + ", description=" + description + ", status=" + status + ", team=" + team + ", teamID=" + teamID + ", complexityID=" + complexityID + ", complexity=" + complexity + ", complexValue=" + complexValue + ", owner=" + owner + ", ownerID=" + ownerID + '}';
    }

    
}
