package com.app.ugaid.model;

public class Donation {
    private String type;
    private String quantity;
    private String name;
    private String contact;
    private String Description;

    public Donation() {
    }

    public Donation(String type, String quantity, String name, String contact, String description) {
        this.type = type;
        this.quantity = quantity;
        this.name = name;
        this.contact = contact;
        Description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
