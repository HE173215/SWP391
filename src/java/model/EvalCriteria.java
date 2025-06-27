/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vqman
 */
public class EvalCriteria {

    private int id;
    private String name;
    private String detail;
    private int weight;
    private int assignmentId;
    private boolean status;
    private String assignmentName;

    public EvalCriteria() {
    }

    public EvalCriteria(int id, String name, String detail, int weight, int assignmentId, boolean status, String assignmentName) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.weight = weight;
        this.assignmentId = assignmentId;
        this.status = status;
        this.assignmentName = assignmentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

}
