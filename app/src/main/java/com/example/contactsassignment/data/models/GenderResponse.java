package com.example.contactsassignment.data.models;

public class GenderResponse {

    private int count;
    private String name;
    private String gender;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    private double probability;
}
