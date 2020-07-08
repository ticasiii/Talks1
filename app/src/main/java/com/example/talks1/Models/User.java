package com.example.talks1.Models;

 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Objects;



public class User {

    private String id;
    private String username;
    private String email;
    private String name;
    private String info;
    private String picture;
    private boolean speaker;
    private Integer points;
    //private Map<String, Object> talkList = new HashMap<>();
    private List<Talk> talkList;





    private Map<String, Object> _favouriteSpeakers = new HashMap<>();
    private Map<String, Object> _interestedTalks = new HashMap<>();
    private Map<String, Object> _playNextTalks = new HashMap<>();

    private Map<String, Object> _friendUsers = new HashMap<>();
    //Map<String, Object> _attendedLectures = new HashMap<>();


    private Map<String, Object> _myTalks = new HashMap<>();
    private Map<String, Object> _myPastTalks = new HashMap<>();


    public User() {

    }

    public User(String fullname,String username, int picture){


    }

    public List<Talk> getTalkList(){return talkList;}

    public void setTalkList(List<Talk> tl){
        this.talkList = tl;
    }

    public void addToTalkList(Talk t){
        this.talkList.add(t);
    }

 /**   public void setTalkList(Map<String, Object> map)
    {
        this.talkList = map;
    }
    public Map<String, Object> getTalkList()
    {
        return this.talkList;
    }**/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isSpeaker() {
        return this.speaker;
    }

    public void setSpeaker (boolean isSpeaker) {
        this.speaker = isSpeaker;
    }

    public Map<String, Object> get_favouriteSpeakers() {
        return _favouriteSpeakers;
    }

    public void set_favouriteSpeakers(Map<String, Object> _favouriteSpeakers) {
        this._favouriteSpeakers = _favouriteSpeakers;
    }

    public Map<String, Object> get_interestedTalks() {
        return _interestedTalks;
    }

    public void set_interestedTalks(Map<String, Object> _favouriteTalks) {
        this._interestedTalks = _favouriteTalks;
    }
	


    public Map<String, Object> get_playNextTalks() {
        return _playNextTalks;
    }
	
	public void set_playNextTalks(Map<String, Object> _playNextTalks) {
        this._playNextTalks = _playNextTalks;
    }

//    public Map<String, Object> get_attendedTalks() {
//        return _attendedTalks;
//    }
//
//    public void set_attendedTalks(Map<String, Object> _attendedTalks) {
//        this._attendedTlks = _attendedTalks;
//    }

    public Map<String, Object> get_myTalks() {
        return _myTalks;
    }

    public void set_myTalks(Map<String, Object> _myTalks) {
        this._myTalks = _myTalks;
    }

    public Map<String, Object> get_myPastTalks() {
        return _myPastTalks;
    }

    public void set_myPastTalks(Map<String, Object> _myPastTalks) {
        this._myPastTalks = _myPastTalks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Map<String, Object> get_friendUsers() {
        return _friendUsers;
    }

    public void set_friendUsers(Map<String, Object> _friendUsers) {
        this._friendUsers = _friendUsers;
    }

}
