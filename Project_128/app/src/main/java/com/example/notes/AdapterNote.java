package com.example.notes;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterNote extends ArrayAdapter<Note> {
    private int layoutResourceId;
    private Context context;
    private static final String LOG_TAG = "AdapterNote";

    public AdapterNote(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        layoutResourceId = textViewResourceId;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            Note item = getItem(position);
            View v = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                v = inflater.inflate(layoutResourceId, null);

            } else {
                v = convertView;
            }

            TextView date = (TextView) v.findViewById(R.id.display_date);
            TextView header = (TextView) v.findViewById(R.id.display_header);
            TextView text = (TextView) v.findViewById(R.id.display_text);

            ImageButton button = (ImageButton) v.findViewById(R.id.button_remove);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RemoveNote(item);
                }
            });

            date.setText(item.getDate());
            header.setText(item.getHeader());
            text.setText(item.getText());

            return v;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error", ex);
            return null;
        }
    }
    private void RemoveNote(Note note){
        String tableName = context.getString(R.string.db_table_name);
        String columnName1 = context.getString(R.string.db_table_column_name_1);
        // db
        SQLiteDatabase db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        // delete value
        db.execSQL("DELETE FROM " + tableName + " WHERE " + columnName1 + "='" + note.getDate() + "'");
        db.close();
        remove(note);
    }
}
