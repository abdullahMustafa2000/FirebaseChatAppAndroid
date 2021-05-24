package com.example.mychatapplication.ViewModels;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mychatapplication.pojo.AppDatabase;
import com.example.mychatapplication.pojo.DAOs.UserDao;
import com.example.mychatapplication.pojo.Tables.UserDB;

public class UserViewModel extends AndroidViewModel {

    AppDatabase appDatabase;
    UserDao userDao;

    public UserViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application);
        userDao = appDatabase.getUserDao();
    }


    public void updateName(String name, String Uid){
        appDatabase = AppDatabase.getInstance(getApplication());
        userDao = appDatabase.getUserDao();
        userDao.updateName(name, Uid);
    }

    public void updateEmail(String email, String Uid){
        appDatabase = AppDatabase.getInstance(getApplication());
        userDao = appDatabase.getUserDao();
        userDao.updateEmail(email, Uid);
    }

    public void updatePhone(String phone, String Uid){
        appDatabase = AppDatabase.getInstance(getApplication());
        userDao = appDatabase.getUserDao();
        userDao.updatePhone(phone, Uid);
    }

    public void updateImage(Uri image, String Uid){
        appDatabase = AppDatabase.getInstance(getApplication());
        userDao = appDatabase.getUserDao();
        userDao.updateImage(image, Uid);
    }




    public void insertUser(UserDB userDB){
        new InsertAsyncTask(userDao).execute(userDB);
    }

    public static class InsertAsyncTask extends AsyncTask<UserDB, Void, Void>{

        UserDao userDao;

        public InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserDB... userDBS) {
            userDao.insertUser(userDBS[0]);
            return null;
        }
    }

    public LiveData<UserDB> getUserData(String userId){
        return userDao.selectUser(userId);
    }

    public void updateUser(UserDB userDB){
        new UpdateAsyncTask(userDao).execute(userDB);
    }

    public static class UpdateAsyncTask extends AsyncTask<UserDB, Void, Void>{

        UserDao userDao;

        public UpdateAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserDB... userDBS) {
            userDao.updateUser(userDBS[0]);
            return null;
        }
    }
}
