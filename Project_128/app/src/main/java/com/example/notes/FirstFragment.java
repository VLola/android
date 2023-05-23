package com.example.notes;

import static android.content.Context.MODE_PRIVATE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notes.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        loadDb();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void loadDb(){
        String tableName = getString(R.string.db_table_name);
        String columnName1 = getString(R.string.db_table_column_name_1);
        String columnName2 = getString(R.string.db_table_column_name_2);
        String columnName3 = getString(R.string.db_table_column_name_3);
        // db
        SQLiteDatabase db = getActivity().getApplicationContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        // create table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnName1 + " TEXT, "  + columnName2 + " TEXT, " + columnName3 + " TEXT, UNIQUE("+ columnName1 +"))");
        // read table
        Cursor query = db.rawQuery("SELECT * FROM " + tableName + ";", null);
        // write result
        ArrayList<Note> list = new ArrayList<>();

        while(query.moveToNext()){
            Note note = new Note();
            note.date = query.getString(0);
            note.header = query.getString(1);
            note.text = query.getString(2);
            list.add(note);
        }
        setList(list);
        // close connection
        query.close();
        db.close();
    }
    private void setList(List<Note> items){
        AdapterNote adapter = new AdapterNote(getActivity().getApplicationContext(), R.layout.note);
        adapter.addAll(items);
        binding.listView.setAdapter(adapter);
    }


}