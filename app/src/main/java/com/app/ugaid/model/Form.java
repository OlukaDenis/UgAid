package com.app.ugaid.model;

public class Form {
    private String id;
    private User user;
    private Symptom symptom;
    private String hospital;
    private double latitude;
    private double longitude;

    public Form() {
    }

    public Form(User user, Symptom symptom, String hospital, double latitude, double longitude) {
        this.user = user;
        this.symptom = symptom;
        this.hospital = hospital;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Symptom getSymptom() {
        return symptom;
    }

    public void setSymptom(Symptom symptom) {
        this.symptom = symptom;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
