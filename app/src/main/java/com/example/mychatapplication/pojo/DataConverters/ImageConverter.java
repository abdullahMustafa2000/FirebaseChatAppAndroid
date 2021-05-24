package com.example.mychatapplication.pojo.DataConverters;

import android.net.Uri;

import androidx.room.TypeConverter;

public class ImageConverter {
    @TypeConverter
    public static Uri fromUri(String s){
        return s== null?null : Uri.parse(s);
    }

    @TypeConverter
    public static String toUri(Uri uri){
        return uri == null? null : uri.toString();
    }
}
