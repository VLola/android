package com.example.notes;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notes.databinding.FragmentSecondBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String header = binding.editTextTextPersonName.getText().toString();
                String text = binding.editTextTextMultiLine.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                if(header.length() > 0 && text.length() > 0){
                    Result(formatter.format(date), header, text);
                }
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void Result(String value1, String value2, String value3){

        String tableName = getString(R.string.db_table_name);
        String columnName1 = getString(R.string.db_table_column_name_1);
        String columnName2 = getString(R.string.db_table_column_name_2);
        String columnName3 = getString(R.string.db_table_column_name_3);
        // value
        ContentValues values = new ContentValues();
        values.put(columnName1, value1);
        values.put(columnName2, value2);
        values.put(columnName3, value3);
        // db
        SQLiteDatabase db = getActivity().getApplicationContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        // create table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnName1 + " TEXT, " + columnName2 + " TEXT, " + columnName3 + " TEXT, UNIQUE("+ columnName1 +"))");
        // insert value
        db.insert(tableName, null, values);

        db.close();
    }

}