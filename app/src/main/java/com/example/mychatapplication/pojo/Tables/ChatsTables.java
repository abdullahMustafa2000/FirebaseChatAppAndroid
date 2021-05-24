package com.example.mychatapplication.pojo.Tables;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mychatapplication.Models.Chat;
import com.example.mychatapplication.pojo.DataConverters.ChatsArrayConverter;
import com.example.mychatapplication.pojo.DataConverters.ImageConverter;

import java.util.ArrayList;
/*
@Entity(tableName = "chatTable")
public class ChatsTables {

    @PrimaryKey
    @NonNull
    String roomId= "a";

    String hisName;
    @TypeConverters({ImageConverter.class})
    Uri roomImage;
    @TypeConverters({ChatsArrayConverter.class})
    ArrayList<Chat> ChatList;
}

 */
