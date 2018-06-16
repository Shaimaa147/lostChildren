package com.iti.jets.lostchildren;

/**
 * Created by Ahmed Ali on 6/11/2018.
 */

public class Information {
    private  String name ;
    private  String phone ;
    private  int photo;
  public  Information(){


    }
    public Information(String name, String phone, int photo) {
        this.name = name;
        this.phone = phone;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
