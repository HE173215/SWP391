/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Do Duan
 */
public class Milestone {

    private int id;
    private String code;
    private String name;
    private int priority;
    private int weight;
    private String detail;
    private int finalMilestone;
    private Date endDate;
    private int status;
    private String assigment;
    private int assigmentId;
    private int max_eval_value;

    public int getMax_eval_value() {
        return max_eval_value;
    }

    public void setMax_eval_value(int max_eval_value) {
        this.max_eval_value = max_eval_value;
    }

    public String getAssigment() {
        return assigment;
    }

    public void setAssigment(String assigment) {
        this.assigment = assigment;
    }

    public int getAssigmentId() {
        return assigmentId;
    }

    public void setAssigmentId(int assigmentId) {
        this.assigmentId = assigmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getFinalMilestone() {
        return finalMilestone;
    }

    public void setFinalMilestone(int finalMilestone) {
        this.finalMilestone = finalMilestone;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
