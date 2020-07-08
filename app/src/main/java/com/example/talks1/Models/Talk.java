package com.example.talks1.Models;

import java.util.HashMap;
import java.util.Map;

public class Talk {




    private String id;
    private String title;
    private String category;
    private String speaker;
    private String picture;
    private String description;
    private String address;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private double lat;
    private double lng;

    Map<String, Object> attendance = new HashMap<>();

    private Boolean past;

    private String _user;

    public Talk() {
    }

    public Talk(String title, String speaker) {
        this.title = title;
        this.speaker = speaker;
    }

    public Talk(String title, String speaker, int picture) {
        this.title = title;
        this.speaker = speaker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Map<String, Object> getAttendance() {
        return attendance;
    }

    public void setAttendance(Map<String, Object> attendance) {
        this.attendance = attendance;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public void setDescription (String desc){
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_user() {
        return _user;
    }

    public void set_user(String _user) {
        this._user = _user;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getPast() {
        return past;
    }

    public void setPast(Boolean past) {
        this.past = past;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String composeDateString() {
        String dateString = year + "-" + month + "-"
                + day + " " + hour + ":"
                + minute;
        return  dateString;
    }
}
