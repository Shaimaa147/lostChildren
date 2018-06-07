package com.iti.jets.lostchildren.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoundChild {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private Object lastName;
    @SerializedName("motherName")
    @Expose
    private Object motherName;
    @SerializedName("imageUrl")
    @Expose
    private Object imageUrl;
    @SerializedName("fromAge")
    @Expose
    private Object fromAge;
    @SerializedName("toAge")
    @Expose
    private Object toAge;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("foundDate")
    @Expose
    private Object foundDate;
    @SerializedName("foundLocation")
    @Expose
    private Object foundLocation;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("returned")
    @Expose
    private Object returned;
    @SerializedName("currentLocation")
    @Expose
    private Object currentLocation;
    @SerializedName("foundUserId")
    @Expose
    private User foundUserId;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getMotherName() {
        return motherName;
    }

    public void setMotherName(Object motherName) {
        this.motherName = motherName;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getFromAge() {
        return fromAge;
    }

    public void setFromAge(Object fromAge) {
        this.fromAge = fromAge;
    }

    public Object getToAge() {
        return toAge;
    }

    public void setToAge(Object toAge) {
        this.toAge = toAge;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Object foundDate) {
        this.foundDate = foundDate;
    }

    public Object getFoundLocation() {
        return foundLocation;
    }

    public void setFoundLocation(Object foundLocation) {
        this.foundLocation = foundLocation;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getReturned() {
        return returned;
    }

    public void setReturned(Object returned) {
        this.returned = returned;
    }

    public Object getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Object currentLocation) {
        this.currentLocation = currentLocation;
    }

    public User getFoundUserId() {
        return foundUserId;
    }

    public void setFoundUserId(User foundUserId) {
        this.foundUserId = foundUserId;
    }

}