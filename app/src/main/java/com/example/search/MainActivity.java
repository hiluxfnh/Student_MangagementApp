package com.example.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ListView list;
    SearchView sv;
    Switch s;
    Button btn, deleteButton, exportButton;
    EditText name, deleteName;
    ArrayList<String> str = new ArrayList<>(Arrays.asList("Ethan", "Caleb", "Liam", "Mason", "Dylan", "Ryan", "Felix", "Owen", "Max", "Leo", "Ethan", "Logan", "Oliver"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sv = findViewById(R.id.sv);
        list = findViewById(R.id.list);
        s = findViewById(R.id.sw);
        btn = findViewById(R.id.send);
        name = findViewById(R.id.name);
        deleteButton = findViewById(R.id.delete_button);
        deleteName = findViewById(R.id.delete_name);
        exportButton = findViewById(R.id.export_button);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, str);
        list.setAdapter(adapter);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            adapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                    sv.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "Switch is OFF you can't search!", Toast.LENGTH_SHORT).show();
                    sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            return false;
                        }
                    });
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = name.getText().toString();
                str.add(newName);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "The name added is: " + newName, Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameToDelete = deleteName.getText().toString();
                boolean removed = str.remove(nameToDelete);

                if (removed) {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Name removed: " + nameToDelete, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Name not found: " + nameToDelete, Toast.LENGTH_SHORT).show();
                }
            }
        });

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportStudentsAsCSV();
            }
        });
    }

    private void exportStudentsAsCSV() {
        StringBuilder csvData = new StringBuilder();
        csvData.append("Name,Email,Phone,Address\n");

        for (String student : str) {
            csvData.append(student).append("\n");
        }

        try {
            File dir = getExternalFilesDir(null);
            File file = new File(dir, "students.csv");

            FileWriter writer = new FileWriter(file);
            writer.append(csvData.toString());
            writer.flush();
            writer.close();

            Toast.makeText(MainActivity.this, "Students exported as CSV: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
