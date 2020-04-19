package com.app.ugaid.model;

public class Symptom {
    private String id;
    private String breathing_difficulty;
    private String chest_pressure;
    private String high_temperature;
    private String continuous_cough;


    public Symptom(String breathing_difficulty, String chest_pressure, String high_temperature, String continuous_cough) {
        this.breathing_difficulty = breathing_difficulty;
        this.chest_pressure = chest_pressure;
        this.high_temperature = high_temperature;
        this.continuous_cough = continuous_cough;
    }

    public String getBreathing_difficulty() {
        return breathing_difficulty;
    }

    public void setBreathing_difficulty(String breathing_difficulty) {
        this.breathing_difficulty = breathing_difficulty;
    }

    public String getChest_pressure() {
        return chest_pressure;
    }

    public void setChest_pressure(String chest_pressure) {
        this.chest_pressure = chest_pressure;
    }

    public String getHigh_temperature() {
        return high_temperature;
    }

    public void setHigh_temperature(String high_temperature) {
        this.high_temperature = high_temperature;
    }

    public String getContinuous_cough() {
        return continuous_cough;
    }

    public void setContinuous_cough(String continuous_cough) {
        this.continuous_cough = continuous_cough;
    }
}
