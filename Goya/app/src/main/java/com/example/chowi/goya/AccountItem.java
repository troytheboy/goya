package com.example.chowi.goya;

import static android.R.attr.id;
import static android.R.attr.password;
import static android.R.attr.userVisible;

/**
 * Created by chowi on 4/15/2017.
 */


public class AccountItem {
    private String username;
    private String email;
    private String password;


    public AccountItem() {

    }

    public AccountItem(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

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