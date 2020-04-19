package com.app.ugaid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class State {

    @SerializedName("name")
    private String name;


    public State() {
    }

    public State(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
