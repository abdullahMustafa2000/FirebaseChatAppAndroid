package com.example.mychatapplication.pojo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mychatapplication.pojo.DAOs.UserDao;
import com.example.mychatapplication.pojo.DataConverters.ArrayConverter;
import com.example.mychatapplication.pojo.DataConverters.ChatsArrayConverter;
import com.example.mychatapplication.pojo.DataConverters.DataConverter;
import com.example.mychatapplication.pojo.DataConverters.ImageConverter;
import com.example.mychatapplication.pojo.Tables.UserDB;

@Database(entities = {UserDB.class},version = 2, exportSchema = false)
@TypeConverters({DataConverter.class, ArrayConverter.class, ImageConverter.class, ChatsArrayConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();
    //public abstract ChatListDao getChatDao();

    private static AppDatabase myAppDataBase = null;

    public static AppDatabase getInstance(Context context){
        if(myAppDataBase==null){
            myAppDataBase =
                    Room.databaseBuilder(context,
                            AppDatabase.class,
                            "my_data_base")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return myAppDataBase;
    }
}
