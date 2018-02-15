package com.ingreens.googlesignin;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by root on 12/2/18.
 */


public class DbInterface {
    MyDB dbms;

    public DbInterface(Context context) {
        MyDB d1 = new MyDB(context);
        this.dbms=d1;
    }

    public boolean insertUser(RegistrationModel user){
        return dbms.executeNonQuery("INSERT INTO "+
                AllKeys.DB_TBL_USER+"('"+
                AllKeys.DB_TBL_USER_NAME+"','"+
                AllKeys.DB_TBL_USER_NICKNAME+"','"+AllKeys.DB_ACCOUNT_ID+"') VALUES('"+
                user.getName() +"','"+
                user.getNickname() +"','"+user.getAccount_id() +"');");
    }

    public RegistrationModel getUser(int id){
        RegistrationModel user=new RegistrationModel();
        Cursor c=dbms.executeQuery("SELECT * FROM "+
                AllKeys.DB_TBL_USER+" WHERE "+AllKeys.DB_TBL_USER_ID+"="+
                id);
        c.moveToFirst();
        if(!c.isAfterLast()){
            user.setId(c.getInt(0));
            user.setAccount_id(c.getString(1));
            user.setName(c.getString(2));
            user.setNickname(c.getString(3));
        }
        return user;
    }

    public boolean isUserExists(String account_id){
        Cursor c=dbms.executeQuery("SELECT * FROM "+
                AllKeys.DB_TBL_USER+" WHERE "+AllKeys.DB_ACCOUNT_ID+"='"+
                account_id+"'");
        /*c.moveToFirst();
        if (!c.isAfterLast()){
            user.setAccount_id(c.getString(1));
            return true;
        }*/
        if(c.getCount()>0){
            return true;
        }
        return false;
    }


   /* public RegistrationModel getUser(String email, String password){
        RegistrationModel user=new RegistrationModel();
        Cursor c=dbms.executeQuery("SELECT * FROM "+
                AllKeys.DB_TBL_USER+" WHERE "+AllKeys.DB_TBL_USER_EMAIL+"='"+
                email+"' AND "+AllKeys.DB_TBL_USER_PASSWORD+"='"+password+"'");
        c.moveToFirst();
        if(!c.isAfterLast()){
            user.setId(c.getInt(0));
            user.setName(c.getString(1));
            user.setEmail(c.getString(2));
        }
        return user;
    }*/






}