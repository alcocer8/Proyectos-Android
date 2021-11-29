package com.example.tiendadbx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.ic_save) {
            intent = new Intent(getBaseContext(), ActivityCreate.class);
            startActivity(intent);
        } else if (id == R.id.ic_update) {
            intent = new Intent(getBaseContext(), ActivityRUD.class);
            startActivity(intent);
        } else if (id == R.id.menu_login) {
            FirebaseAuth.getInstance().signOut();
        }
        return super.onOptionsItemSelected(item);
    }
}