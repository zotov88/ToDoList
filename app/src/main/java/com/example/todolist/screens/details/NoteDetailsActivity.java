package com.example.todolist.screens.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.todolist.App;
import com.example.todolist.R;
import com.example.todolist.model.Note;
//экран создания/редактирование заметок
public class NoteDetailsActivity extends AppCompatActivity {

    //передаем через бандл всю заметку
    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";
    private Note note;
    private EditText editText;

    //вызов одного активити из другого с помощью метода
    public static void start(Activity caller, Note note) {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);
        if (note != null) {
            intent.putExtra(EXTRA_NOTE, note);
        }
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //приложение читает файл разметки и создает конкретные классы по описанию из activity_note_details
        //классы добавляются к активити и их можно использовать
        setContentView(R.layout.activity_note_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(getString(R.string.note_details_title));

        editText = findViewById(R.id.text);

        if (getIntent().hasExtra(EXTRA_NOTE)) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            editText.setText(note.text);
        } else {
            note = new Note();
        }
    }

    //создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //извлечение меню
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //обработка для событий
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //тап по стрелке назад
            case android.R.id.home:
                finish();
                break;
                //тап по save
            case R.id.action_save:
                Toast toast = Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT);
                if (editText.getText().length() > 0) {
                    note.text = editText.getText().toString();
                    note.done = false;
                    note.timestamp = System.currentTimeMillis();
                    //определяем новая заметка или редактирование
                    //произойдет изменение в БД, обновится LiveData и изменения отразятся на главном экране
                    if (getIntent().hasExtra(EXTRA_NOTE)) {
                        //редактирование
                        App.getInstance().getNoteDao().update(note);
                    } else {
                        //новая заметка
                        App.getInstance().getNoteDao().insert(note);
                    }
                    finish();
                }
                toast.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
