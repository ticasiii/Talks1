package com.example.talks1.Models;

import java.util.HashMap;
import java.util.Map;

public class Speaker extends User {

    public Speaker(String fullname, int picture){

    }


    private float currentRate;
    private float ratingsCount;

    private Map<String, Object> _pastSpeeches = new HashMap<>();
    private Map<String, Object> _futureSpeeches = new HashMap<>();

    public void rateSpeaker(float rate)
    {
        this.currentRate = (ratingsCount*this.currentRate +rate) / ++ratingsCount;
    }
    public float getCurrentRate() {
        return this.currentRate;
    }
    public float getRatingsCount() {
        return this.ratingsCount;
    }
    public void setCurrentRate(float rate,int ratingsCount) {
        this.currentRate =rate;
        this.ratingsCount = ratingsCount;
    }

    public Map<String, Object> getPastSpeeches()
    {
        return _pastSpeeches;
    }

    public void setPastSpeeches(Map<String, Object> pastSpeeches) {
        this._pastSpeeches = pastSpeeches;
    }

    public Map<String, Object> getFutureSpeeches() {
        return _futureSpeeches;
    }

    public void setFutureSpeeches(Map<String, Object> futureSpeeches) {
        this._futureSpeeches = futureSpeeches;
    }
}
