package com.example.chowi.goya;

import static android.R.attr.id;

/**
 * Created by chowi on 4/15/2017.
 */


public class AccountItem {
    private String name, email, password;


    public AccountItem() {

    }

    public AccountItem(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String title) {
        this.email= email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String description) {
        this.password= password;
    }

}