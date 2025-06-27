//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import dao.UserDAO;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.net.URLEncoder;
//import model.GoogleAccount;
//import model.User;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.fluent.Form;
//import org.apache.http.client.fluent.Request;
//import service.Iconstant;
//
//@WebServlet("/LoginGoogleHandler")
//public class LoginGoogleHandler extends HttpServlet {
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String code = request.getParameter("code");
//        System.out.println("Received code: " + code);
//
//        try {
//            String accessToken = getToken(code);
//            System.out.println("Access token received: " + accessToken);
//
//            GoogleAccount googleAccount = getUserInfo(accessToken);
//            System.out.println("Google account info: " + googleAccount);
//
//            // Lưu email vào session
//            String email = googleAccount.getEmail();
//            request.getSession().setAttribute("email", email);
//
//            UserDAO userDAO = new UserDAO();
//            User user = userDAO.getUserByEmail(email);
//
//            if (user == null) {
//                // Nếu người dùng chưa tồn tại, tạo mới
//                user = new User();
//                user.setUserName(googleAccount.getName());
//                user.setEmail(email);
//                user.setFullName(googleAccount.getName());
//                user.setMobile(googleAccount.getPhoneNumber()); // Kiểm tra Google có trả về số điện thoại không
//                user.setPassword(""); // Mật khẩu có thể để trống hoặc mã hóa nếu cần
//                user.setRoleID(1); // Giả sử role mặc định là 1
//                user.setStatus(1); // Giả sử trạng thái mặc định là 1
//
//                userDAO.createUser(user);
//                System.out.println("New user created: " + user);
//            } else {
//                // Nếu người dùng đã tồn tại
//                System.out.println("User already exists: " + user);
//            }
//
//            // Chuyển hướng đến trang registerGG.jsp với email
//            response.sendRedirect("WEB-INF/view/login/registerGG.jsp?email=" + URLEncoder.encode(email, "UTF-8"));
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request to Google API.");
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while communicating with Google API.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
//        }
//    }
//
//    public static String getToken(String code) throws ClientProtocolException, IOException {
//        String response = Request.Post(Iconstant.GOOGLE_LINK_GET_TOKEN)
//                .bodyForm(
//                        Form.form()
//                                .add("client_id", Iconstant.GOOGLE_CLIENT_ID)
//                                .add("client_secret", Iconstant.GOOGLE_CLIENT_SECRET)
//                                .add("redirect_uri", Iconstant.GOOGLE_REDIRECT_URI)
//                                .add("code", code)
//                                .add("grant_type", Iconstant.GOOGLE_GRANT_TYPE)
//                                .build()
//                )
//                .execute().returnContent().asString();
//
//        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
//        return jobj.get("access_token").getAsString();
//    }
//
//    public static GoogleAccount getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
//        String link = Iconstant.GOOGLE_LINK_GET_USER_INFO + accessToken;
//        String response = Request.Get(link).execute().returnContent().asString();
//        System.out.println("Response from user info request: " + response);
//        return new Gson().fromJson(response, GoogleAccount.class);
//    }
//}
