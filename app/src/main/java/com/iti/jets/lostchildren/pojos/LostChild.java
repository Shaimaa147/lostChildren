package com.iti.jets.lostchildren.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LostChild {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("imageUrl")
    @Expose
    private Object imageUrl;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("orginalAddress")
    @Expose
    private Object orginalAddress;
    @SerializedName("phone")
    @Expose
    private Object phone;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("lostDate")
    @Expose
    private Object lostDate;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("returned")
    @Expose
    private Object returned;
    @SerializedName("lostLocation")
    @Expose
    private Object lostLocation;
    @SerializedName("lastName")
    @Expose
    private Object lastName;
    @SerializedName("motherName")
    @Expose
    private Object motherName;
    @SerializedName("lostUserId")
    @Expose
    private User lostUserId;

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

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Object getOrginalAddress() {
        return orginalAddress;
    }

    public void setOrginalAddress(Object orginalAddress) {
        this.orginalAddress = orginalAddress;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getLostDate() {
        return lostDate;
    }

    public void setLostDate(Object lostDate) {
        this.lostDate = lostDate;
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

    public Object getLostLocation() {
        return lostLocation;
    }

    public void setLostLocation(Object lostLocation) {
        this.lostLocation = lostLocation;
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

    public User getLostUserId() {
        return lostUserId;
    }

    public void setLostUserId(User lostUserId) {
        this.lostUserId = lostUserId;
    }

}