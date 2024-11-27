package com.example.easynotes.showAllNotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easynotes.R;
import com.example.easynotes.dataClass.Notes;
import com.example.easynotes.interfaces.NotesClickListener;

import java.util.ArrayList;
import java.util.Iterator;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Notes> notesList;
    private ArrayList<Notes> notesFilterList;
    private NotesClickListener clickListener;

    public NotesAdapter(Context context, ArrayList<Notes> notesList, NotesClickListener clickListener) {
        this.context = context;
        this.notesList = notesList;
        this.notesFilterList = notesList;
        this.clickListener = clickListener;
    }

    // update the list after change
    public void updateData(ArrayList<Notes> notes) {
        notesList.clear();
        notesList.addAll(notes);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView add_image;
        CardView add_note_card;
        CardView note_card;
        TextView title;
        TextView note;
        TextView date;

        // find id for perform action on views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            add_image = itemView.findViewById(R.id.add_note_icon);
            add_note_card = itemView.findViewById(R.id.add_note_card);
            note_card = itemView.findViewById(R.id.note_card);
            title = itemView.findViewById(R.id.title);
            note = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        // if the position is 0 then show the add notes item
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_note_card_item, parent, false);
        }
        // if the position is greater then 0 then show the all notes item
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // If the position is 0 set the add note item
        if (position == 0) {
            Glide.with(context).load(R.drawable.add_notes_icon).into(holder.add_image);
            holder.add_note_card.setOnClickListener(v -> {
                clickListener.addNoteClick();
            });

        } else {
            // set data on item
            Notes notes = notesFilterList.get(position - 1);
            holder.title.setText(notes.getTitle());
            holder.note.setText(notes.getNote());
            holder.date.setText(getMonths(notes.getMonth()) + " " + notes.getDate() + ", " + notes.getYear());

            holder.note_card.setOnClickListener(v -> {
                clickListener.updateNote(notes);
            });

            holder.note_card.setOnLongClickListener(v -> {
                clickListener.longPressClickForDeleteNote(notes.getId());
                return true;
            });
        }
    }


    ArrayList<Notes> getFilter(String string){

         notesFilterList = new ArrayList<>();

         for(Notes notes: notesList){
             if (notes.getTitle().toLowerCase().contains(string.toLowerCase())
                     || notes.getNote().toLowerCase().contains(string.toLowerCase())
                     || notes.getDate().toLowerCase().contains(string.toLowerCase())
                     || notes.getMonth().toLowerCase().contains(string.toLowerCase())
                     || notes.getYear().toLowerCase().contains(string.toLowerCase())) {
                 notesFilterList.add(notes);
             }
         }
         return notesFilterList;
    }


    String getMonths(String month) {
        switch (month) {
            case "1":
                return "Jan";
            case "2":
                return "Fev";
            case "3":
                return "Mar";
            case "4":
                return "Apr";
            case "5":
                return "May";
            case "6":
                return "Jun";
            case "7":
                return "Jul";
            case "8":
                return "Aug";
            case "9":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return notesFilterList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else {
            return 1;
        }
    }


}
