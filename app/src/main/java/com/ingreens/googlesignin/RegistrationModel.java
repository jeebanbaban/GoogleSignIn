package com.ingreens.googlesignin;

/**
 * Created by root on 12/2/18.
 */

public class RegistrationModel {
    int id;
    String account_id;
    String name;
    String nickname;

    public RegistrationModel(int id, String account_id, String name, String nickname) {
        this.id = id;
        this.account_id = account_id;
        this.name = name;
        this.nickname = nickname;
    }
    public RegistrationModel() {
        this.id = 0;
        this.account_id = "";
        this.name = "";
        this.nickname = "";
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
