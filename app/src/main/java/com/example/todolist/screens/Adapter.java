package com.example.todolist.screens;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.todolist.App;
import com.example.todolist.model.Note;
import com.example.todolist.R;
import com.example.todolist.screens.details.NoteDetailsActivity;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {

    //для автоматических анимаций при сортировки заданий по признаку готовности
    //он будет автоматически определять изменения внутр себя и выдавать команды RecyclerView и он проигрывает анимации
    private SortedList<Note> sortedList;

    public Adapter() {

        sortedList = new SortedList<>(Note.class, new SortedList.Callback<Note>() {
            //сравниваем заметки по времени их создания
            @Override
            public int compare(Note o1, Note o2) {
                if (!o2.done && o1.done) {
                    return 1;
                }
                if (o2.done && !o1.done) {
                    return -1;
                }
                return (int) (o2.timestamp - o1.timestamp);
            }

            //метод вызывается, когда элемент меняет позицию и сообщаем это RecyclerView
            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            //содержимое может быть разное, но ID одинаковое (заметка после обновления)
            @Override
            public boolean areContentsTheSame(Note oldItem, Note newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Note item1, Note item2) {
                return item1.uid == item2.uid;
            }

            //делегируем напрямую в адаптер
            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    //создание и возврат ViewHolder
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false));
    }

    //привязка заметки к ViewHolder
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    //передаем обновленный список заметок, чтобы тот отобразился в адаптере
    //SortedList определит все изменения и вызовет соответствующий методы для обновления конкретных элементов
    public void setItems(List<Note> notes) {
        sortedList.replaceAll(notes);
    }

    //класс для отдельного элемента. Хранит ссылки на View, обеспечивает переиспользование
    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteText;
        CheckBox completed;
        View delete;
        Note note;
        //чтобы тихо задать значение в чекбокс
        boolean silentUpdate;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);

            noteText = itemView.findViewById(R.id.text_note);
            completed = itemView.findViewById(R.id.completed);
            delete = itemView.findViewById(R.id.delete);

            //вызов активити для редактирования заметки
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoteDetailsActivity.start((Activity) itemView.getContext(), note);
                }
            });

            //удаление заметки
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.getInstance().getNoteDao().delete(note);
                }
            });

            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (!silentUpdate) {
                        note.done = checked;
                        App.getInstance().getNoteDao().update(note);
                    }
                    updateStrokeOut();
                }
            });
        }

        //отображает значения полей заметки на View
        public void bind(Note note) {
            this.note = note;
            noteText.setText(note.text);
            updateStrokeOut();
            //при первоначальном задании значения заметки обработчик completed.setOnCheckedChangeListener не выполнится
            silentUpdate = true;
            completed.setChecked(note.done);
            silentUpdate = false;
        }

        //зачеркивание
        private void updateStrokeOut() {
            if (note.done) {
                //побитовое двоичное или. Флаг устанавливается в true
                noteText.setPaintFlags(noteText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                //побитовое и с обратным флагом
                noteText.setPaintFlags(noteText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }
}
