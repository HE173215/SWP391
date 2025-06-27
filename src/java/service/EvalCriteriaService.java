/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.EvalCriteriaDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Assignment;
import model.EvalCriteria;

/**
 *
 * @author vqman
 */
public class EvalCriteriaService {

    private final EvalCriteriaDAO criteriaDAO;
    // Lưu trữ thông báo lỗi
    private Map<String, String> messages = new HashMap<>();

    public EvalCriteriaService() {
        this.criteriaDAO = new EvalCriteriaDAO();
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public List<EvalCriteria> getEvalCriteriaByPage(int groupId, int limit, int offset) {
        return criteriaDAO.getEvalCriteriaByPage(groupId, limit, offset);
    }

    public int countEvalCriteriaByGroupId(int groupId) {
        return criteriaDAO.countEvalCriteria(groupId);
    }

    public boolean addEvalCriteria(EvalCriteria criteria) {
        messages.clear(); // Xóa thông báo lỗi trước đó
        boolean isValid = true; // Biến kiểm tra hợp lệ

        // Kiểm tra trùng lặp
        if (isDuplicateCriteria(criteria.getName(), criteria.getAssignmentId())) {
            messages.put("dup", "Tên tiêu chí đã tồn tại cho bài tập này.");
            isValid = false; // Đánh dấu là không hợp lệ
        }

        // Kiểm tra tên không được để trống
        if (criteria.getName() == null || criteria.getName().isEmpty()) {
            messages.put("name", "Tên của setting không được để trống.");
            isValid = false;
        }

        // Kiểm tra giới hạn độ dài của name
        if (criteria.getName().length() < 3 || criteria.getName().length() > 50) {
            messages.put("name", "Tên của setting phải nằm trong khoảng từ 3 đến 50 ký tự.");
            isValid = false;
        }

        // Kiểm tra weight trong khoảng 1-100
        if (criteria.getWeight() < 1 || criteria.getWeight() > 100) {
            messages.put("weight", "Weight phải nằm trong khoảng 1-100.");
            isValid = false;
        }

        // Nếu có bất kỳ lỗi nào, trả về false mà không thêm dữ liệu
        if (!isValid) {
            return false;
        }

        // Thực hiện thêm nếu tất cả điều kiện thỏa mãn
        return criteriaDAO.insertEvalCriteria(criteria);
    }

    public boolean updateEvalCriteria(EvalCriteria criteria) {
        messages.clear(); // Xóa thông báo lỗi trước đó
        boolean isValid = true;
        // Kiểm tra giới hạn độ dài của name
        if (criteria.getName().length() < 3 || criteria.getName().length() > 50) {
            messages.put("name", "Tên của setting phải nằm trong khoảng từ 3 đến 50 ký tự.");
            isValid = false;
        }

        // Kiểm tra weight trong khoảng 1-100
        if (criteria.getWeight() < 1 || criteria.getWeight() > 100) {
            messages.put("weight", "Weight trong khoảng 1-100.");
            isValid = false;
        }
        if (!isValid) {
            return false;
        }

        // Thực hiện cập nhật nếu tất cả điều kiện thỏa mãn
        return criteriaDAO.updateEvalCriteria(criteria);
    }

    public List<EvalCriteria> filterByStatus(boolean status, int limit, int offset) {
        return criteriaDAO.filterByStatus(status, limit, offset);
    }

    public List<EvalCriteria> searchByName(String name, int limit, int offset) {
        return criteriaDAO.searchByName(name, limit, offset);
    }

    public List<EvalCriteria> filterByAssignmentId(int assignmentId, int limit, int offset) {
        return criteriaDAO.filterByAssignmentId(assignmentId, limit, offset);
    }

    public List<EvalCriteria> filterByStatusAndAssignmentId(boolean status, int assignmentId, int limit, int offset) {
        return criteriaDAO.filterByStatusAndAssignmentId(status, assignmentId, limit, offset);
    }

    public List<Assignment> getAssignmentBySubject(int subjectId) {
        return criteriaDAO.getAssignmentBySubject(subjectId);
    }

    public EvalCriteria getCriteriaById(int id) {
        return criteriaDAO.getCriteriaById(id);
    }

    public boolean updateStatusById(int id, int newStatus) {
        return criteriaDAO.updateStatusById(id, newStatus);
    }

    public boolean isDuplicateCriteria(String name, int assignmentId) {
        // Gọi DAO để kiểm tra trùng lặp
        return criteriaDAO.isDuplicateCriteria(name, assignmentId);
    }

    public int countSearchByName(String name) {
        return criteriaDAO.countSearchByName(name);
    }

    public int countByStatus(boolean status) {
        return criteriaDAO.countByStatus(status);
    }

    public int countByAssignmentId(int assignmentId) {
        return criteriaDAO.countByAssignmentId(assignmentId);
    }

    public int countByStatusAndAssignmentId(boolean status, int assignmentId) {
        return criteriaDAO.countByStatusAndAssignmentId(status, assignmentId);
    }

}
