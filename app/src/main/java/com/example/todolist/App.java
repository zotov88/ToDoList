package com.example.todolist;

import android.app.Application;

import androidx.room.Room;

import com.example.todolist.data.AppDataBase;
import com.example.todolist.data.NoteDao;

public class App extends Application { //создаем базу данных при старте приложения и живет. Указали класс в манифесте. По проекту синглтон

    private AppDataBase database;
    private NoteDao noteDao;
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        database = Room.databaseBuilder(getApplicationContext(), // создание базы данных
                AppDataBase.class, "dataBase")
                .allowMainThreadQueries()
                .build();
        noteDao = database.noteDao(); //получаем созданный DAO
    }

    public AppDataBase getDatabase() {
        return database;
    }

    public void setDatabase(AppDataBase database) {
        this.database = database;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public void setNoteDao(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
}
