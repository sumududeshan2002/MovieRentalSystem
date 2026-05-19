package com.se1020.movierental.model;

public class AdminUser extends User {

    private int adminLevel;

    public AdminUser(String userId, String username, String password, String email, String phone, int adminLevel) {
        super(userId, username, password, email, phone);
        this.adminLevel = adminLevel;
    }

    public int getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public String toString() {
        return super.toString() + "," + getRole() + "," + adminLevel;
    }

    public static AdminUser fromString(String line) {
        String[] parts = line.split(",");
        return new AdminUser(parts[0], parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[6]));
    }
}
