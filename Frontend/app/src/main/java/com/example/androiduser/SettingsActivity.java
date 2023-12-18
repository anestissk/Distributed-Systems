package com.example.androiduser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androiduser.R;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SettingsActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextIpAddress;
    private EditText editTextPortNumber;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextIpAddress = findViewById(R.id.editTextIpAddress);
        editTextPortNumber = findViewById(R.id.editTextPortNumber);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        loadSettings();

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private void loadSettings() {
        String username = sharedPreferences.getString("username", "");
        String ipAddress = sharedPreferences.getString("ipAddress", "");
        int portNumber = sharedPreferences.getInt("portNumber", 0);

        editTextUsername.setText(username);
        editTextIpAddress.setText(ipAddress);
        editTextPortNumber.setText(String.valueOf(portNumber));
    }

    private void saveSettings() {
        String username = editTextUsername.getText().toString();
        String ipAddress = editTextIpAddress.getText().toString();
        int portNumber = Integer.parseInt(editTextPortNumber.getText().toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("ipAddress", ipAddress);
        editor.putInt("portNumber", portNumber);
        editor.apply();
    }
}