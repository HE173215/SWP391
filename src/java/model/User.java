package model;

import java.util.Date; // Import thư viện Date

/**
 *
 * @author Do Duan
 */
public class User {

    private int id;
    private String userName;
    private String fullName;
    private String email;
    private String mobile;
    private String password;
    private int status;
    private int roleID;
    private String image;
    private String notes;
    private String otp;
    private Date otp_expiry; // Sử dụng kiểu dữ liệu Date cho thời gian hết hạn OTP
    private String roleName;
    private String role;
    private int allocationId; // Include allocation_id
    private String settingName;

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;

        switch (roleID) {
            case 1:
                this.role = "Student";
                break;
            case 2:
                this.role = "Teacher";
                break;
            case 4:
                this.role = "Subject Manager";
                break;
            case 5:
                this.role = "Dept Manager";
                break;
            case 6:
                this.role = "Admin";
                break;
            default:
                break;
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getOtp_expiry() {
        return otp_expiry;
    }

    public void setOtp_expiry(Date otp_expiry) {
        this.otp_expiry = otp_expiry;
    }

}
