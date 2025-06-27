/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author Do Duan
 */
public class CommonService {

    public String uploadFile(Part filePart, String uploadPath) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Đường dẫn lưu file trên server
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Lưu file vào thư mục trên server
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);  // Ghi file vào server
        return fileName;  // Trả về tên file (đường dẫn tương đối)

    }

    // Paging by Limit - offset
    public int getOffset(int page, int pageSize) {  // 
        // Phân trang

        // Tính offset (điểm bắt đầu lấy dữ liệu)
        int offset = (page - 1) * pageSize;
        return offset;
    }

    public int getPageNumber(String page_raw) {
        int page = 1;
        if (page_raw != null) {
            try {
                page = Integer.parseInt(page_raw);
            } catch (NumberFormatException e) {
                page = 1; // Nếu không hợp lệ, mặc định là trang 1
            }
        }
        return page;
    }

    public int getTotalPage(int totalRecords, int pageSize) {
        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        return totalPages;
    }
}
