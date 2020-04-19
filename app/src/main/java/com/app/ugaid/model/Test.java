package com.app.ugaid.model;

public class Test {
    private String cough;
    private String cold;
    private String diarrhea;
    private String sore_throat;
    private String body_aches;
    private String headache;
    private String fever;
    private String breathing_difficulty;
    private String fatigue;
    private String travelled;
    private String travel_history;
    private String direct_contact;
    private String result;
    private double latitude;
    private double longitude;

    public Test() {
    }

    public Test(String cough, String cold, String diarrhea, String sore_throat, String body_aches, String headache, String fever, String breathing_difficulty, String fatigue, String travelled, String travel_history, String direct_contact) {
        this.cough = cough;
        this.cold = cold;
        this.diarrhea = diarrhea;
        this.sore_throat = sore_throat;
        this.body_aches = body_aches;
        this.headache = headache;
        this.fever = fever;
        this.breathing_difficulty = breathing_difficulty;
        this.fatigue = fatigue;
        this.travelled = travelled;
        this.travel_history = travel_history;
        this.direct_contact = direct_contact;
    }

    public String getCough() {
        return cough;
    }

    public void setCough(String cough) {
        this.cough = cough;
    }

    public String getCold() {
        return cold;
    }

    public void setCold(String cold) {
        this.cold = cold;
    }

    public String getDiarrhea() {
        return diarrhea;
    }

    public void setDiarrhea(String diarrhea) {
        this.diarrhea = diarrhea;
    }

    public String getSore_throat() {
        return sore_throat;
    }

    public void setSore_throat(String sore_throat) {
        this.sore_throat = sore_throat;
    }

    public String getBody_aches() {
        return body_aches;
    }

    public void setBody_aches(String body_aches) {
        this.body_aches = body_aches;
    }

    public String getHeadache() {
        return headache;
    }

    public void setHeadache(String headache) {
        this.headache = headache;
    }

    public String getFever() {
        return fever;
    }

    public void setFever(String fever) {
        this.fever = fever;
    }

    public String getBreathing_difficulty() {
        return breathing_difficulty;
    }

    public void setBreathing_difficulty(String breathing_difficulty) {
        this.breathing_difficulty = breathing_difficulty;
    }

    public String getFatigue() {
        return fatigue;
    }

    public void setFatigue(String fatigue) {
        this.fatigue = fatigue;
    }

    public String getTravelled() {
        return travelled;
    }

    public void setTravelled(String travelled) {
        this.travelled = travelled;
    }

    public String getTravel_history() {
        return travel_history;
    }

    public void setTravel_history(String travel_history) {
        this.travel_history = travel_history;
    }

    public String getDirect_contact() {
        return direct_contact;
    }

    public void setDirect_contact(String direct_contact) {
        this.direct_contact = direct_contact;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String info(){
        return "Cough: "+getCough()+ "\n" +
        "Cold: "+getCold()+ "\n" +
        "Diarrhea: "+getDiarrhea()+ "\n" +
        "Sore throat: "+getSore_throat()+ "\n" +
        "Headache: "+getHeadache()+ "\n" +
        "High fever: "+getFever()+ "\n" +
        "Fatiguge: "+getFatigue()+ "\n" +
        "Breathing difficulty: "+getBreathing_difficulty()+ "\n" +
        "Traveled anywhere: "+getTravelled()+ "\n" +
        "Travel history: "+getTravel_history()+ "\n" +
        "Direct contact: "+getDirect_contact()+ "\n";
    }
}
