/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class Assignment {

    private int id;
    private String name;
    private int weight;
    private String detail;
    private boolean final_assignment;
    private boolean status;
    private int subject_id;
    private String subject_code;

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

    public boolean isFinal_assignment() {
        return final_assignment;
    }

    public void setFinal_assignment(boolean final_assignment) {
        this.final_assignment = final_assignment;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    @Override
    public String toString() {
        return "Assignment{" + "id=" + id + ", name=" + name + ", weight=" + weight + ", detail=" + detail + ", final_assignment=" + final_assignment + ", status=" + status + ", subject_id=" + subject_id + ", subject_code=" + subject_code + '}';
    }
    

}
