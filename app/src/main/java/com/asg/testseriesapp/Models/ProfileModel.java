package com.asg.testseriesapp.Models;

public class ProfileModel {
    private String name;
    private String email;
    private String phone;
    private String year;
    private String branch;
    private String semester;
    private int bookmarksCount;

    public ProfileModel(String name, String email, String phone, int bookmarksCount, String year, String branch, String semester) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bookmarksCount = bookmarksCount;
        this.year = year;
        this.branch = branch;
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getBookmarksCount() {
        return bookmarksCount;
    }

    public void setBookmarksCount(int bookmarksCount) {
        this.bookmarksCount = bookmarksCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
