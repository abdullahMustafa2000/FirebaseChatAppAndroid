package com.example.mychatapplication.pojo.DAOs;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mychatapplication.pojo.Tables.UserDB;

@Dao
public interface UserDao {

    @Insert
    void insertUser(UserDB userDB);

    @Update
    void updateUser(UserDB userDB);

    @Query("Select * from userTable where Uid=:userId")
    LiveData<UserDB> selectUser(String userId);

    @Query("Update userTable set Name = :name where Uid= :userId")
    void updateName(String name, String userId);

    @Query("Update userTable set Email = :email where Uid= :userId")
    void updateEmail(String email, String userId);

    @Query("Update userTable set phone = :phone where Uid= :userId")
    void updatePhone(String phone, String userId);

    @Query("Update userTable set image = :image where Uid= :userId")
    void updateImage(Uri image, String userId);
}
