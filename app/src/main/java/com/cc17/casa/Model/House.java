package com.cc17.casa.Model;

public class House {

    public String status, lid, date, time, image, monthlyRent, houseAddress, houseDetails, houseLocation, houseContactNumber, houseOwner, userId, search;

    public House(){}

    public House(String status, String lid, String date, String time, String image, String monthlyRent, String houseAddress, String houseDetails, String houseLocation, String houseContactNumber, String houseOwner, String userId, String search) {
        this.status = status;
        this.lid = lid;
        this.date = date;
        this.time = time;
        this.image = image;
        this.monthlyRent = monthlyRent;
        this.houseAddress = houseAddress;
        this.houseDetails = houseDetails;
        this.houseLocation = houseLocation;
        this.houseContactNumber = houseContactNumber;
        this.houseOwner = houseOwner;
        this.userId = userId;
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(String monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getHouseDetails() {
        return houseDetails;
    }

    public void setHouseDetails(String houseDetails) {
        this.houseDetails = houseDetails;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public String getHouseContactNumber() {
        return houseContactNumber;
    }

    public void setHouseContactNumber(String houseContactNumber) {
        this.houseContactNumber = houseContactNumber;
    }

    public String getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(String houseOwner) {
        this.houseOwner = houseOwner;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
