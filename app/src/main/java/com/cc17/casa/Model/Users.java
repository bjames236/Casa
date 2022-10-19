package com.cc17.casa.Model;

public class Users {

    private String userId, email, password, phoneNumber, houseAddress, fullname, profileImage, status;

    public Users(){

    }

    public Users(String userId, String email, String password, String phoneNumber, String houseAddress,
                 String fullname, String profileImage, String status) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.houseAddress = houseAddress;
        this.fullname = fullname;
        this.profileImage = profileImage;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
