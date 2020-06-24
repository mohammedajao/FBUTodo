package com.example.fbutodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    Button btnSave;
    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnSave = findViewById(R.id.btnSave);
        etText = findViewById(R.id.etText);

        // Embellishment to notify user they're editing an item
        getSupportActionBar().setTitle("Item Edit");
        etText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        // Save user edits on click
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create an intent containing the results
                Intent i = new Intent(EditActivity.this, MainActivity.class);
                // Pass the data which will be the result of the edit
                i.putExtra(MainActivity.KEY_ITEM_TEXT, etText.getText().toString());
                i.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                // Set the result of the intent
                setResult(RESULT_OK, i);
                // Finish the activity, close the screen, and go back
                finish();
            }
        });
    }
}