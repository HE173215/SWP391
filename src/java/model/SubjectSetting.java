/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vqman
 */
public class SubjectSetting {

    private int id;
    private String name;
    private String type;
    private int value;
    private boolean status;
    private int subjectId;
    private String subjectName;

    public SubjectSetting() {
    }

    public SubjectSetting(int id, String name, String type, int value, boolean status, int subjectId, String subjectName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.status = status;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "SubjectSetting{"
                + "name='" + name + '\''
                + ", type='" + type + '\''
                + ", value=" + value
                + ", status=" + status
                + ", subjectId=" + subjectId
                + '}';
    }

}
