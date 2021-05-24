package com.example.mychatapplication.pojo.DataConverters;

import androidx.room.TypeConverter;

import com.example.mychatapplication.Models.UsersModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayConverter {

    @TypeConverter
    public static ArrayList<UsersModel> fromString(String value) {
        Type listType = new TypeToken<ArrayList<UsersModel>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<UsersModel> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
