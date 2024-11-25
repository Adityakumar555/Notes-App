package com.example.easynotes.favoriteNotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.easynotes.addUpdateNotes.AddUpdateNotesActivity;
import com.example.easynotes.viewModel.NotesViewModel;
import com.example.easynotes.R;
import com.example.easynotes.databinding.FragmentFavoriteNoteBinding;
import com.example.easynotes.dataClass.Notes;
import com.example.easynotes.interfaces.NotesClickListener;
import com.example.easynotes.sqlDB.SqlHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rejowan.cutetoast.CuteToast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FavoriteNoteFragment extends Fragment implements NotesClickListener {

    FragmentFavoriteNoteBinding binding;
    FavoriteNotesAdapter favoriteNotesAdapter;
    ArrayList<Notes> notesList;
    SqlHelper sqlHelper;
    NotesViewModel notesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteNoteBinding.inflate(inflater, container, false);

        // initialize the notesViewModel
        notesViewModel = new ViewModelProvider(requireActivity()).get(NotesViewModel.class);

        // initialize the SqlHelper class to get method to create and update the notes
        sqlHelper = new SqlHelper(getContext());

        // initialize the notesList
        notesList = new ArrayList<>();

        // fetch notes
        fetchNotes();

        return binding.getRoot();
    }

    // filter all Favorite notes
    public static Predicate<Notes> filteringAll(boolean isFavorite) {
        return p -> (p.isFavorite() && isFavorite);
    }


    private void fetchNotes() {
        // fetch notes from sqlHelper class
        notesList = sqlHelper.getNotes();
        // filter all Favorite notes and save in a new list
        List<Notes> listOfFilteredNotes = notesList.stream().filter(filteringAll(true)).collect(Collectors.toList());
        // initialize the favoriteNotesAdapter
        favoriteNotesAdapter = new FavoriteNotesAdapter(getContext(), (ArrayList<Notes>) listOfFilteredNotes, this);
        // for refresh the adapter
        favoriteNotesAdapter.notifyDataSetChanged();
        // set recyclerview layout example - linear or grid
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        // set data in adapter
        binding.recyclerView.setAdapter(favoriteNotesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // fetch notes for refresh note on update notes
        fetchNotes();
        // filter note using search
        // this observe the current search text
        notesViewModel.getSearchNoteText().observe(requireActivity(), c -> {
            // filter notes in adapter
            favoriteNotesAdapter.getFilter().filter(c.toString());
            favoriteNotesAdapter.notifyDataSetChanged();
        });
    }


    // update the note from the favorite fragment
    @Override
    public void updateNote(Notes notes) {
        Intent intent = new Intent(getActivity(), AddUpdateNotesActivity.class);
        intent.putExtra("isFev", notes.isFavorite());
        intent.putExtra("notes", notes);
        startActivity(intent);
    }

    // delete note on long press
    @Override
    public void longPressClickForDeleteNote(int notesId) {
        deleteNote(notesId);
    }

    private void deleteNote(int notesId) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext()).setTitle("Delete Notes").setMessage("Are you sure to delete this notes.").setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // delete note
                sqlHelper.deleteNote(notesId);
                CuteToast.ct(getContext(), "Note Delete Successful.", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                // refresh the notes after the delete notes
                fetchNotes();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss the dialog on cancel Button click
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        // for change button color
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                //dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
            }
        });
        dialog.show();
    }

    @Override
    public void addNoteClick() {}
}