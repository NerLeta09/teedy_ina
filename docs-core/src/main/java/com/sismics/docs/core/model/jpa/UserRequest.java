package com.sismics.docs.core.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "T_USER_REQUEST")
public class UserRequest implements Loggable {

    @Id
    @Column(name = "REQ_ID_C", nullable = false)
    private String id;

    @Column(name = "REQ_USERNAME_C", nullable = false)
    private String username;

    @Column(name = "REQ_PASSWORD_C", nullable = false)
    private String password;

    @Column(name = "REQ_STATUS_C", nullable = false)
    private String status = "PENDING";

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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public Date getDeleteDate() {
        return null;
    }

    @Override
    public String toMessage() {
        return id;
    }    
}
