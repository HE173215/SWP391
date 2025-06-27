package service;

import dao.TeamDAO;
import dao.UserDAO;
import model.Team;
import model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamService {

    private TeamDAO teamDAO;
    private UserDAO userDAO;

    public TeamService() {
        teamDAO = new TeamDAO(); // Khởi tạo đối tượng TeamDAO
        userDAO = new UserDAO(); // Khởi tạo đối tượng UserDAO
    }

    // Lấy danh sách tất cả các team
    public List<Team> getAllTeams() {
        try {
            return teamDAO.getAllTeams();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Trả về danh sách rỗng trong trường hợp có lỗi
        }
    }

    // Lấy danh sách team theo milestone ID
    public List<Team> getTeamsByMilestone(int milestoneId) {
        try {
            return teamDAO.getTeamsByMilestone(milestoneId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
        }
    }

    public List<Team> getTeamsByUser(int userId, int userRole, String searchQuery, String classId, String teamSearchQuery) throws SQLException {
        if (userRole == 1) {
            // Fetch teams where the user is a direct team member (role 1)
            return teamDAO.getTeamsByUserRole1(userId, searchQuery, classId, teamSearchQuery);
        } else if (userRole == 2 || userRole == 6) {
            // Fetch teams where the user is a class member (role 2 or 6)
            return teamDAO.getTeamsByUserRole2(userId, searchQuery, classId, teamSearchQuery);
        } else {
            throw new IllegalArgumentException("Invalid user role: " + userRole);
        }
    }

    // Lấy thông tin team theo ID
    public Team getTeamById(int teamId) {
        try {
            return teamDAO.getTeamById(teamId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }

    public List<Team> getTeamsByClassId(String classId, String searchQuery) {
        try {
            return teamDAO.getTeamsByClassId(classId, searchQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of error
        }
    }

    // Cập nhật thông tin team
    public boolean updateTeam(Team team) {
        try {
            teamDAO.updateTeam(team);
            return true; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    // Tạo team mới
    public boolean createTeam(Team team) {
        try {
            // Gọi phương thức DAO để thêm team vào cơ sở dữ liệu
            teamDAO.createTeam(team);  // Chỉ cần truyền đối tượng team, không cần userId nếu không thêm vào bảng team_member
            return true; // Trả về true nếu thêm team thành công
        } catch (SQLException e) {
            // In ra lỗi nếu có vấn đề trong quá trình thực hiện
            System.err.println("Error creating team: " + e.getMessage());
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    // Thêm team vào milestone
    public boolean addTeamToMilestone(int teamId, int milestoneId) {
        try {
            teamDAO.addTeamToMilestone(teamId, milestoneId);
            return true; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    // Xóa team khỏi milestone
    public boolean removeTeamFromMilestone(int teamId, int milestoneId) {
        try {
            teamDAO.removeTeamFromMilestone(teamId, milestoneId);
            return true; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    // Kiểm tra nếu team thuộc về milestone
    public boolean isTeamInMilestone(int teamId, int milestoneId) {
        try {
            return teamDAO.isTeamInMilestone(teamId, milestoneId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    // Kiểm tra sự tồn tại của team
    public boolean checkTeamExists(int teamId) {
        return getTeamById(teamId) != null;
    }

    public List<User> getTeamMembers(int teamId) {
        try {
            return teamDAO.getTeamMembersByTeamId(teamId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
        }
    }

    public boolean addMemberToTeam(int teamId, int userId) {
        try {
            return teamDAO.addMemberToTeam(teamId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeMemberFromTeam(int teamId, int userId) {
        try {
            return teamDAO.removeMemberFromTeam(teamId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUserIdByEmail(String email) {
        UserDAO userDAO = new UserDAO(); // Khởi tạo đối tượng UserDAO
        try {
            User user = userDAO.getUserByEmail(email);
            return user != null ? user.getId() : -1; // Trả về ID của người dùng nếu tìm thấy, nếu không trả về -1
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Kiểm tra sự hợp lệ của thông tin tạo mới team
    public String validateTeamInfo(String name, String detail, String topic, int classId) {
        StringBuilder errorMessages = new StringBuilder();

        // Kiểm tra các trường có bị bỏ trống không
        if (name == null || name.isEmpty()) {
            errorMessages.append("Tên của đội không được để trống! ");
        }
        if (detail == null || detail.isEmpty()) {
            errorMessages.append("Chi tiết của đội không được để trống! ");
        }
        if (topic == null || topic.isEmpty()) {
            errorMessages.append("Chủ đề của đội không được để trống! ");
        }

        // Kiểm tra chiều dài của các trường
        if (name.length() > 45) {
            errorMessages.append("Tên của đội không được dài quá 45 ký tự! ");
        }

        if (detail.length() > 255) {
            errorMessages.append("Chi tiết của đội không được dài quá 255 ký tự! ");
        }

        if (topic.length() > 45) {
            errorMessages.append("Chủ đề của đội không được dài quá 45 ký tự! ");
        }

        // Kiểm tra giá trị classId (phải là số dương)
        if (classId <= 0) {
            errorMessages.append("Class ID không hợp lệ! ");
        }

        return errorMessages.toString();
    }

    public boolean isTeamNameOrTopicExists(String name, String topic, int classId) {
        try {
            return teamDAO.isTeamNameOrTopicExists(name, topic, classId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    public boolean isTeamNameExists(String name, int classId) {
        try {
            return teamDAO.isTeamNameExists(name, classId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    public boolean deleteTeam(int teamId) {
        try {
            return teamDAO.deleteTeam(teamId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getMembersByClassId(int classId) {
        try {
            return userDAO.getMembersByClassId(classId); // Assumes method exists in DAO
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of error
        }
    }

    public void updateTeamMemberRole(int userIdToPromote, int teamId) {
        try {
            // Call the DAO method to update roles in the database
            teamDAO.updateTeamMemberRole(userIdToPromote, teamId);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately, e.g., log it or throw a custom exception
            throw new RuntimeException("Error updating team member role: " + e.getMessage(), e);
        }
    }
    
     public Team getTeam(int classID, int userID) {
        try {
            return teamDAO.getTeam(classID, userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
     
     public int getTeamAllocation(int userID, int teamID){
//        int userID = Integer.parseInt(userID_raw);
//        int teamID = Integer.parseInt(teamID_raw);
        try {
           return teamDAO.getTeamAllocation(userID, teamID);
        } catch (SQLException e) {
        }
        return 0;
    }
}
