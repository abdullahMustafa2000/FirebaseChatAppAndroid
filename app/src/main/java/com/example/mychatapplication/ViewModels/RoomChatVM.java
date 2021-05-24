package com.example.mychatapplication.ViewModels;
/*
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mychatapplication.pojo.AppDatabase;
import com.example.mychatapplication.pojo.DAOs.ChatListDao;
import com.example.mychatapplication.pojo.Tables.ChatsTables;

import java.util.zip.CRC32;

public class RoomChatVM extends AndroidViewModel {

    AppDatabase appDatabase;
    ChatListDao chatListDao;
    public RoomChatVM(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application);
        chatListDao = appDatabase.getChatDao();
    }

    public LiveData<ChatsTables> selectRoom(String roomId){
        return chatListDao.CHATS_TABLES_LIVE_DATA(roomId);
    }

    public void deleteRoom(String roomId){
       new DeleteAsyncTask(chatListDao).execute(roomId);
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {
        ChatListDao chatListDao;
        public DeleteAsyncTask(ChatListDao chatListDao) {
            this.chatListDao = chatListDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            chatListDao.deleteRoom(strings[0]);
            return null;
        }
    }
}


 */