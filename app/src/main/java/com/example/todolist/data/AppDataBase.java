package com.example.todolist.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todolist.model.Note;

//база данных
@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    //позволяет получить доступ к data access object данной модели
    public abstract NoteDao noteDao();
}

// при создании класса AppDataBase библиотекой Room генерируется реализация интерфейса NoteDao
