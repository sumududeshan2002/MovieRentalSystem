package com.se1020.movierental.controller;

import com.se1020.movierental.model.RegularUser;
import com.se1020.movierental.model.User;
import com.se1020.movierental.model.AdminUser;
import com.se1020.movierental.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,30}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+()\\-\\s]{7,20}$");

    @Autowired
    private ServletContext servletContext;

    private final UserService userService;

    public UserController(ServletContext servletContext) {
        this.servletContext = servletContext;
        String userFilePath = servletContext.getRealPath("/WEB-INF/") + "../../resources/data/users.txt";
        this.userService = new UserService(userFilePath);
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String membershipType,
                               Model model) {
        username = safeTrim(username);
        password = safeTrim(password);
        email = safeTrim(email);
        phone = safeTrim(phone);
        membershipType = safeTrim(membershipType);

        if (!isValidUsername(username)) {
            model.addAttribute("error", "Username must be 3-30 characters and contain only letters, numbers, or underscore.");
            return "user/register";
        }
        if (!isValidPassword(password)) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "user/register";
        }
        if (!isValidEmail(email)) {
            model.addAttribute("error", "Please enter a valid email address.");
            return "user/register";
        }
        if (!isValidPhone(phone)) {
            model.addAttribute("error", "Please enter a valid phone number.");
            return "user/register";
        }
        if (!isValidMembershipType(membershipType)) {
            model.addAttribute("error", "Invalid membership type selected.");
            return "user/register";
        }

        String userId = userService.generateUserId();
        RegularUser user = new RegularUser(userId, username, password, email, phone, membershipType);

        if (!userService.registerUser(user)) {
            model.addAttribute("error", "Username already exists");
            return "user/register";
        }

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        username = safeTrim(username);
        password = safeTrim(password);
        if (username.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Username and password are required.");
            return "user/login";
        }

        User user = userService.login(username, password);
        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "user/login";
        }

        session.setAttribute("loggedInUser", user);
        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/edit-profile")
    public String showEditProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@RequestParam String email,
                              @RequestParam String phone,
                              @RequestParam String membershipType,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }
        email = safeTrim(email);
        phone = safeTrim(phone);
        membershipType = safeTrim(membershipType);
        if (!isValidEmail(email) || !isValidPhone(phone) || !isValidMembershipType(membershipType)) {
            model.addAttribute("error", "Please provide valid profile details.");
            model.addAttribute("user", user);
            return "user/edit-profile";
        }

        user.setEmail(email);
        user.setPhone(phone);
        if (user instanceof RegularUser regularUser) {
            regularUser.setMembershipType(membershipType);
        }

        userService.updateUser(user);
        session.setAttribute("loggedInUser", user);
        return "redirect:/users/profile";
    }

    @GetMapping("/delete-account")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            userService.deleteUser(user.getUserId());
        }
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin/list")
    public String adminUserList(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/admin/user-list";
    }

    @GetMapping("/admin/delete/{userId}")
    public String adminDeleteUser(@PathVariable String userId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        if (safeTrim(userId).isEmpty()) {
            return "redirect:/users/admin/list";
        }
        userService.deleteUser(userId);
        return "redirect:/users/admin/list";
    }

    @GetMapping("/admin/detail/{userId}")
    public String adminUserDetail(@PathVariable String userId, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return "redirect:/users/admin/list";
        }
        model.addAttribute("user", user);
        return "user/admin/user-detail";
    }

    @GetMapping("/admin/edit/{userId}")
    public String adminEditUserForm(@PathVariable String userId, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return "redirect:/users/admin/list";
        }
        model.addAttribute("user", user);
        return "user/admin/edit-user";
    }

    @PostMapping("/admin/edit/{userId}")
    public String adminEditUser(@PathVariable String userId,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam(required = false) String membershipType,
                                @RequestParam(required = false) Integer adminLevel,
                                HttpSession session,
                                Model model) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return "redirect:/users/admin/list";
        }
        email = safeTrim(email);
        phone = safeTrim(phone);
        membershipType = safeTrim(membershipType);
        if (!isValidEmail(email) || !isValidPhone(phone)) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Please provide valid email and phone.");
            return "user/admin/edit-user";
        }

        user.setEmail(email);
        user.setPhone(phone);
        if (user instanceof RegularUser regularUser && membershipType != null) {
            if (!isValidMembershipType(membershipType)) {
                model.addAttribute("user", user);
                model.addAttribute("error", "Invalid membership type selected.");
                return "user/admin/edit-user";
            }
            regularUser.setMembershipType(membershipType);
        }
        if (user instanceof AdminUser adminUser && adminLevel != null) {
            if (adminLevel < 1 || adminLevel > 10) {
                model.addAttribute("user", user);
                model.addAttribute("error", "Admin level must be between 1 and 10.");
                return "user/admin/edit-user";
            }
            adminUser.setAdminLevel(adminLevel);
        }
        userService.updateUser(user);
        return "redirect:/users/admin/list";
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        return user != null && "ADMIN".equals(user.getRole());
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.length() <= 72;
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    private boolean isValidMembershipType(String membershipType) {
        return "BASIC".equals(membershipType) || "PREMIUM".equals(membershipType);
    }
}
