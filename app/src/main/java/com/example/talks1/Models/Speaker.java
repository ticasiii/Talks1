package com.example.talks1.Models;

public class Speaker {


    private String Name;
    private String Category ;
    private String Description ;
    private int Cover ;

    public Speaker() {
    }

    public Speaker(String name, int cover) {
        Name = name;
        Cover = cover;
    }


    public String getName() {
        return Name;
    }

    public String getCategory() {
        return Category;
    }

    public String getDescription() {
        return Description;
    }

    public int getCover() {
        return Cover;
    }


    public void setName(String name) {
        Name = name;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setCover(int cover) {
        Cover = cover;
    }
}
