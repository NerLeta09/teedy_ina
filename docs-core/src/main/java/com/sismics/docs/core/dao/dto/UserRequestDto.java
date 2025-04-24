package com.sismics.docs.core.dao.dto;

import java.util.Date;

/**
 * Data Transfer Object for UserRequest.
 * This class is used to transfer user request data between layers.
 */
public class UserRequestDto {
    private String id;
    private String username;
    private Date createDate;
    private String status;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}