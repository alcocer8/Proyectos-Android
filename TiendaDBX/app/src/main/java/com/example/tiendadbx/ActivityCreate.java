package com.example.tiendadbx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ActivityCreate extends AppCompatActivity {

    EditText et_nombre, et_id, et_cantidad, et_precio, et_descripcion;
    FloatingActionButton btn_scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
        

        componentes();
        eventos();
    }

    private void componentes() {
        et_id = findViewById(R.id.et_id);
        et_nombre = findViewById(R.id.et_nombre);
        et_cantidad = findViewById(R.id.et_cantidad);
        et_precio = findViewById(R.id.et_precio);
        et_descripcion = findViewById(R.id.et_descripcion);

        btn_scanner = findViewById(R.id.btn_scanner);
    }

    private void eventos() {
        btn_scanner.setOnClickListener( e -> scanner());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ic_save){
            try {
                //Conectando y abriendo con la base de datos
                ConectedBD conectedBD = new ConectedBD(this, ArticulosContract.DATABASE_NAME, null, 1);
                SQLiteDatabase db = conectedBD.getReadableDatabase();

                //ingresando los datos a un content value
                ContentValues values = new ContentValues();
                values.put(ArticulosContract._ID, et_id.getText().toString());
                values.put(ArticulosContract.NOMBRE, et_nombre.getText().toString());
                values.put(ArticulosContract.PRECIO, et_precio.getText().toString());
                values.put(ArticulosContract.CANTIDAD, Integer.parseInt(et_cantidad.getText().toString()));
                values.put(ArticulosContract.DESCRIPCION, et_descripcion.getText().toString());

                //Realizando la ejecucion del query
                db.insert(ArticulosContract.TABLE_NAME, null, values);

                Toast.makeText(this, "Datos Insertados", Toast.LENGTH_SHORT).show();

                //Cerrando la base de datos
                db.close();
                conectedBD.close();
            }catch (NumberFormatException e){
                Toast.makeText(getBaseContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void scanner() {
        barcodeLauncher.launch(new ScanOptions());
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
        options.setPrompt("Scan a barcode");
        options.setCameraId(0);  // Use a specific camera of the device
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(true);
        barcodeLauncher.launch(options);
    }

    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    et_id.setText(result.getContents());
                }
            });



}