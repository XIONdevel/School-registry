package com.example.school.entity.user;

import java.io.Serializable;
import java.time.LocalDate;


public abstract class UserData implements Serializable {

    public abstract Long getId();

    public abstract String getFirstname();

    public abstract String getLastname();

    public abstract String getPhone();

    public abstract LocalDate getDob();

    public abstract User getUser();

    public abstract void setId(Long id);

    public abstract void setFirstname(String firstname);

    public abstract void setLastname(String lastname);

    public abstract void setPhone(String phone);

    public abstract void setDob(LocalDate dob);

    public abstract void setUser(User user);

}
