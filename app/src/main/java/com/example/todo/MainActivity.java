package com.example.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> item;
    ArrayAdapter<String> itemAdapter;
    ListView lvItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItem = (ListView) findViewById(R.id.lvItem);
        item = new ArrayList<>();
        readItems();
        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, item);
        lvItem.setAdapter(itemAdapter);
        //item.add("first item");
        //item.add("second item");

        setupListViewListener();

    }
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemAdapter.add(itemText);
        etNewItem.setText("");
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
        writeItems();
    }

    private void setupListViewListener() {
        lvItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                item.remove(position);
                itemAdapter.notifyDataSetChanged();
                Log.i("MainActivity","Removed Item " + position);
                writeItems();
                return true;
            }
        });
    }

    // returns file in which data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // read the item from the file system
    private void readItems() {
        try {
            // create the array using the content in the file
            item = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
            // just load an empty list
            item = new ArrayList<>();
        }
    }

    // write the items to the filesystem
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), item);
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
        }
    }
}
