package com.example.todolist.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.model.Note;



import java.util.List;

@Dao
public interface NoteDao { // осуществляется доступ к базе данных (data access object)

    @Query("SELECT * FROM Note")
    List<Note> getAll();

    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllLiveData();

    @Query("SELECT * FROM Note WHERE uid IN (:noteIds)")
    List<Note> loadAllByIds(int[] noteIds);

    @Query("SELECT * FROM Note WHERE uid = :uid LIMIT 1")
    Note findById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //если вставить в БД заметку с существующим ID, то будет замена на новую
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

}
