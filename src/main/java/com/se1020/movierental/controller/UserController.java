package com.se1020.movierental.controller;

import com.se1020.movierental.model.RegularUser;
import com.se1020.movierental.model.User;
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

@Controller
@RequestMapping("/users")
public class UserController {

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
                              HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
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
        userService.deleteUser(userId);
        return "redirect:/users/admin/list";
    }

    @GetMapping("/admin/detail/{userId}")
    public String adminUserDetail(@PathVariable String userId, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        model.addAttribute("user", userService.getUserById(userId));
        return "user/admin/user-detail";
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        return user != null && "ADMIN".equals(user.getRole());
    }
}
