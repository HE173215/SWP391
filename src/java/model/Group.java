/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Do Duan
 */
public class Group {
    private int id;
    private String code;
    private String name;
    private String detail;
    private int status; // 0-false, 1-true
    private String type;

    public Group() {
    }

    public Group(int id, String code, String name, String detail, int status, String type) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.detail = detail;
        this.status = status;
        this.type = type;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Group{" + "id=" + id + ", code=" + code + ", name=" + name + ", detail=" + detail + ", status=" + status + ", type=" + type + '}';
    }
    
    
}
