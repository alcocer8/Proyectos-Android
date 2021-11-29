package com.example.tiendadbx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {

    LoginFragment fragmentLogin;
    RegistroFragment fragmentRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragments();
    }

    private void fragments() {
        fragmentLogin = new LoginFragment();
        fragmentRegistro = new RegistroFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedor_frament, fragmentLogin).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.menu_registro){
            Toast.makeText(getBaseContext(), "Registro Menu", Toast.LENGTH_SHORT).show();
            transaction.replace(R.id.contenedor_frament, fragmentRegistro);
        }else if(id == R.id.menu_login){
            transaction.replace(R.id.contenedor_frament, fragmentLogin);
        }
        transaction.commit();

        return super.onOptionsItemSelected(item);
    }
}