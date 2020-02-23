package com.example.fatto7.gtsone.Controllers.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fatto7.gtsone.BusinessManager.Models.clsNotes;
import com.example.fatto7.gtsone.R;

import java.util.ArrayList;
import java.util.List;

public class NotesActivityListAdapter extends ArrayAdapter<clsNotes> {

    public Context mContext;
    public List<clsNotes> NotesList = new ArrayList<>();
    Typeface gillsansFont;
    String editable;

    public NotesActivityListAdapter(Context context, String editable, ArrayList<clsNotes> list) {
        super(context, 0, list);
        mContext = context;
        editable = editable;
        NotesList = list;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.notes_activity, parent, false);

        clsNotes NList = NotesList.get(position);


        TextView NotesActivity_NoteTypeTV = listItem.findViewById(R.id.NotesActivity_NoteTypeTV);
        EditText NotesActivity_NoteTextEt = listItem.findViewById(R.id.NotesActivity_NoteTextEt);
        CheckBox NotesActivityIncludesprint = listItem.findViewById(R.id.NoteActivityPerforemedTV);
        if (editable == "0") {
            NotesActivity_NoteTextEt.setEnabled(false);
            NotesActivityIncludesprint.setEnabled(false);
        } else {
            NotesActivity_NoteTextEt.setEnabled(true);
            NotesActivityIncludesprint.setEnabled(true);

        }
        if (NList.getIncludesprint() != null) {
            NotesActivityIncludesprint.setChecked(NList.getIncludesprint().contains("True") ? true : false);
        }
        NotesActivity_NoteTypeTV.setText(NList.getNotetype());
        NotesActivity_NoteTextEt.setText(NList.getMessage());
        gillsansFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/gillsans.ttf");
        NotesActivity_NoteTypeTV.setTypeface(gillsansFont);
        NotesActivity_NoteTextEt.setTypeface(gillsansFont);
        NotesActivityIncludesprint.setTypeface(gillsansFont);
        NotesActivityIncludesprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (position >= NotesList.size()) {
                    return;
                }
                NotesList.get(position).setIncludesprint(isChecked ? "True" : "false");
            }
        });
        NotesActivity_NoteTextEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (position >= NotesList.size()) {
                    return false;
                }
                NotesList.get(position).setMessage(String.valueOf(v.getText() == null ? "" : v.getText()));

                return true;
            }
        });


        return listItem;
    }
}