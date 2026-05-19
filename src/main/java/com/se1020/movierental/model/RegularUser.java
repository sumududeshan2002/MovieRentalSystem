package com.se1020.movierental.model;

public class RegularUser extends User {

    private String membershipType;

    public RegularUser(String userId, String username, String password, String email, String phone,
                       String membershipType) {
        super(userId, username, password, email, phone);
        this.membershipType = membershipType;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    @Override
    public String getRole() {
        return "USER";
    }

    @Override
    public String toString() {
        return super.toString() + "," + getRole() + "," + membershipType;
    }

    public static RegularUser fromString(String line) {
        String[] parts = line.split(",");
        return new RegularUser(parts[0], parts[1], parts[2], parts[3], parts[4], parts[6]);
    }
}
