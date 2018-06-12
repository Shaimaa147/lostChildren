package com.iti.jets.lostchildren.service;

import com.iti.jets.lostchildren.pojos.User;

/**
 * @author eslam java
 */

public class LogInDataDto {

    private String status;
    private User user;

    public LogInDataDto() {}

    public LogInDataDto(String status, User user) {
        this.status = status;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
