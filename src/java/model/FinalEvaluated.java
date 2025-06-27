/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class FinalEvaluated {

    private int teamId;                  
    private String teamName;             
    private int milestoneId;             
    private String milestoneName;        
    private int requirementId;           
    private double complexityValue;      
    private double qualityValue;         
    private double milestoneWeight;      

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public int getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(int requirementId) {
        this.requirementId = requirementId;
    }

    public double getComplexityValue() {
        return complexityValue;
    }

    public void setComplexityValue(double complexityValue) {
        this.complexityValue = complexityValue;
    }

    public double getQualityValue() {
        return qualityValue;
    }

    public void setQualityValue(double qualityValue) {
        this.qualityValue = qualityValue;
    }

    public double getMilestoneWeight() {
        return milestoneWeight;
    }

    public void setMilestoneWeight(double milestoneWeight) {
        this.milestoneWeight = milestoneWeight;
    }
    
    
}
