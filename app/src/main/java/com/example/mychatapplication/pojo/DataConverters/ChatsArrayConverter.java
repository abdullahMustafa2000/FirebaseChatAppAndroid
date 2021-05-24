package com.example.mychatapplication.pojo.DataConverters;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.mychatapplication.Models.Chat;
import com.example.mychatapplication.Models.UsersModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChatsArrayConverter {

    @TypeConverter
    public static ArrayList<Chat> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Chat>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Chat> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
