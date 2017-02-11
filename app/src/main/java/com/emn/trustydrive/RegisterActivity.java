package com.emn.trustydrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view) {
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText confirmationEditText = (EditText) findViewById(R.id.confirmationEditText);
        String password = passwordEditText.getText().toString();
        String confirmation = confirmationEditText.getText().toString();
        if(!password.equals(confirmation)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            passwordEditText.setText("");
            confirmationEditText.setText("");
        } else {
            //TODO: enregistrer le mdp quelque part
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
