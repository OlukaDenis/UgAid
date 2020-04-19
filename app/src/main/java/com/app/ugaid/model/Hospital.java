package com.app.ugaid.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hospital")
public class Hospital {

    @ColumnInfo(name = "id")
    @PrimaryKey
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "city")
    private String city;

    public Hospital(long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
