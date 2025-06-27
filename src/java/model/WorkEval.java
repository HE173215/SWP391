/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class WorkEval {

    private int id;
    private int reqId;
    private int milestoneId;
    private int complexityId;
    private int qualityId;
    private int teamMilestoneId;
    private boolean isFinal;
    private double ComplexityValue;
    private double QualityValue;
    private double Grade;
    private String quanlityName;
    private String complexityName;
    private String milestoneName;
    private String reqName;

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }
    
    

    public String getQuanlityName() {
        return quanlityName;
    }

    public void setQuanlityName(String quanlityName) {
        this.quanlityName = quanlityName;
    }

    public String getComplexityName() {
        return complexityName;
    }

    public void setComplexityName(String complexityName) {
        this.complexityName = complexityName;
    }

    public double getGrade() {
        return Grade;
    }

    public void setGrade(double Grade) {
        this.Grade = Grade;
    }

    public double getComplexityValue() {
        return ComplexityValue;
    }

    public void setComplexityValue(double ComplexityValue) {
        this.ComplexityValue = ComplexityValue;
    }

    public double getQualityValue() {
        return QualityValue;
    }

    public void setQualityValue(double QualityValue) {
        this.QualityValue = QualityValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public int getComplexityId() {
        return complexityId;
    }

    public void setComplexityId(int complexityId) {
        this.complexityId = complexityId;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public int getTeamMilestoneId() {
        return teamMilestoneId;
    }

    public void setTeamMilestoneId(int teamMilestoneId) {
        this.teamMilestoneId = teamMilestoneId;
    }

    public boolean isIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

}
