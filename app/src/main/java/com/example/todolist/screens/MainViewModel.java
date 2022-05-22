package com.example.todolist.screens;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.App;
import com.example.todolist.model.Note;
import java.util.List;

public class MainViewModel extends ViewModel {

    //предоставляем доступ к данным о списке заметок для отображения на главном экране
    private LiveData<List<Note>> noteLiveData = App.getInstance().getNoteDao().getAllLiveData();

    public LiveData<List<Note>> getNoteLiveData() {
        return noteLiveData;
    }
}
