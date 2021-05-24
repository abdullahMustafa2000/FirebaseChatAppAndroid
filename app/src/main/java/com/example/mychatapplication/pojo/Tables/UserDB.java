package com.example.mychatapplication.pojo.Tables;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mychatapplication.pojo.DataConverters.ImageConverter;

@Entity(tableName = "userTable")
public class UserDB {

    @PrimaryKey
    @NonNull
    private String Uid;
    @ColumnInfo
    private String Name, Email, phone;
    @TypeConverters({ImageConverter.class})
    private Uri image;
    public String token;


    public UserDB() {
    }

    @Ignore
    public UserDB(String uid, String name, String email, String phone, Uri image) {
        Uid = uid;
        Name = name;
        Email = email;
        this.phone = phone;
        this.image = image;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
