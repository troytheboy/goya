package com.example.chowi.goya;

import static android.R.attr.id;
import static android.R.attr.password;

/**
 * Created by chowi on 4/15/2017.
 */


public class AccountItem {
    private String name;
    private String email;
    private String password;


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

    public void setEmail(String email) {
        this.email= email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password= password;
    }

}