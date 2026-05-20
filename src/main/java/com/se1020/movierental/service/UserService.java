package com.se1020.movierental.service;

import com.se1020.movierental.model.AdminUser;
import com.se1020.movierental.model.RegularUser;
import com.se1020.movierental.model.User;
import com.se1020.movierental.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final String userFilePath;

    public UserService(String userFilePath) {
        this.userFilePath = userFilePath;
    }

    public List<User> getAllUsers() {
        List<String> lines = FileUtils.readAllLines(userFilePath);
        List<User> users = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 6) {
                if ("USER".equals(parts[5])) {
                    users.add(new RegularUser(parts[0], parts[1], parts[2], parts[3], parts[4], parts[6]));
                } else if ("ADMIN".equals(parts[5])) {
                    users.add(new AdminUser(parts[0], parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[6])));
                }
            }
        }

        return users;
    }

    public User getUserById(String userId) {
        for (User user : getAllUsers()) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByUsername(String username) {
        for (User user : getAllUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean registerUser(RegularUser user) {
        if (getUserByUsername(user.getUsername()) != null) {
            return false;
        }
        FileUtils.appendLine(userFilePath, user.toString());
        return true;
    }

    public User login(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void updateUser(User user) {
        FileUtils.updateLine(userFilePath, user.getUserId(), user.toString());
    }

    public void deleteUser(String userId) {
        FileUtils.deleteLine(userFilePath, userId);
    }

    public String generateUserId() {
        int nextId = getAllUsers().size() + 1;
        return String.format("U%03d", nextId);
    }

    public boolean usernameExists(String username) {
        return getUserByUsername(username) != null;
    }
}
