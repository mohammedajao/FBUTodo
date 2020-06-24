package com.example.fbutodo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d(TAG, "Single click at position: " + position);
                // Create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // Pass data being added
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // Display new activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                // Removes item from our model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"You removed an item", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();
                // Add item to the model
                items.add(todoItem);
                // Update Adapter that an item was added
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");

                // Embellishment to notify the user about their action
                Toast.makeText(getApplicationContext(), "Item was added, ", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });


    }

    // Handle result of edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve updated text
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract original position from the edited item from the position key
            int itemPosition = data.getExtras().getInt(KEY_ITEM_POSITION);
            // UPDATE MODEL
            items.set(itemPosition, itemText);
            // NOTIFY ADAPTER
            itemsAdapter.notifyItemChanged(itemPosition);
            // Persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item has been updated!", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Mistmatched code!");
        }
    }

    private File getDataFile() {
        // Gets the directory of app and creates a data.txt file
        return new File(getFilesDir(), "data.txt");
    }

    // Load items by reading every line of the data file

    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e(TAG, "Error: Reading items from data.txt", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e(TAG, "Error: Saving items to data.txt", e);
        }
    }
}