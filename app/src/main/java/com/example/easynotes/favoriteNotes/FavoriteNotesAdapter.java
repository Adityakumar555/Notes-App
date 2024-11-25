package com.example.easynotes.favoriteNotes;

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

import com.example.easynotes.R;
import com.example.easynotes.dataClass.Notes;
import com.example.easynotes.interfaces.NotesClickListener;

import java.util.ArrayList;

public class FavoriteNotesAdapter extends RecyclerView.Adapter<FavoriteNotesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Notes> notesList;
    private ArrayList<Notes> notesFilterList;
    private NotesClickListener clickListener;

    public FavoriteNotesAdapter(Context context, ArrayList<Notes> notesList, NotesClickListener clickListener) {
        this.context = context;
        this.notesList = notesList;
        this.notesFilterList = notesList;
        this.clickListener = clickListener;
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

    // show a item in ui
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get all notes and display in ui
        Notes notes = notesFilterList.get(position);
        holder.title.setText(notes.getTitle());
        holder.note.setText(notes.getNote());
        holder.date.setText(getMonths(notes.getMonth()) + " " + notes.getDate() + ", " + notes.getYear());

        // note click for update
        holder.note_card.setOnClickListener(v -> {
            clickListener.updateNote(notes);
        });

        // long press for delete note
        holder.note_card.setOnLongClickListener(v -> {
            clickListener.longPressClickForDeleteNote(notes.getId());
            return true;
        });


    }

    // filter the notes according search text
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    notesFilterList = notesList;
                } else {
                    ArrayList<Notes> filteredList = new ArrayList<>();
                    for (Notes item : notesList) {
                        if (item.getTitle().toLowerCase().contains(charString.toLowerCase()) || item.getNote().toLowerCase().contains(charString.toLowerCase()) || item.getDate().toLowerCase().contains(charString.toLowerCase()) || item.getMonth().toLowerCase().contains(charString.toLowerCase()) || item.getYear().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                    notesFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = notesFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notesFilterList = (ArrayList<Notes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    // get months from months number
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

    // size of list
    @Override
    public int getItemCount() {
        return notesFilterList.size();
    }


}
