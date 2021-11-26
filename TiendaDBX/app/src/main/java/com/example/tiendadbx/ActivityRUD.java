package com.example.tiendadbx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tiendadbx.BD.ArticulosContract;
import com.example.tiendadbx.BD.ConectedBD;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ActivityRUD extends AppCompatActivity {

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
        getMenuInflater().inflate(R.menu.menu_rud, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //Busca el IC el cual fue llamado
        if(id == R.id.ic_search){
            Toast.makeText(getBaseContext(), "Consulta", Toast.LENGTH_SHORT).show();
            consulta();
        }else if(id == R.id.ic_save){
            Toast.makeText(getBaseContext(), "Actualiza", Toast.LENGTH_SHORT).show();
            update();
        }else if(id == R.id.ic_delete){
            Toast.makeText(getBaseContext(), "Elimina", Toast.LENGTH_SHORT).show();
            delete();
        }
        return super.onOptionsItemSelected(item);
    }

    private void consulta() {
        //obteniendo el id a buscar
        String id = et_id.getText().toString();

        //Validando que tengamos algo dentro de id
        if (!id.isEmpty()){
            ConectedBD conectedBD = new ConectedBD(this, ArticulosContract.DATABASE_NAME,null,1);
            SQLiteDatabase db = conectedBD.getReadableDatabase();
            String sql = "SELECT "+ArticulosContract.NOMBRE+", "+ArticulosContract.PRECIO+","+ArticulosContract.CANTIDAD+","+ArticulosContract.DESCRIPCION+" FROM "+ArticulosContract.TABLE_NAME + " WHERE "+ArticulosContract._ID +" = "+id;
            Log.i("Verbose", sql);

            Cursor fila = db.rawQuery(sql, null);

            //Si tenemos una concidencia se cumple la condicion y agregamos los campos encontrados en sus respectivos EditText
            if (fila.moveToFirst()){
                et_nombre.setText(fila.getString(0));
                et_precio.setText(fila.getString(1));
                et_cantidad.setText(fila.getString(2));
                et_descripcion.setText(fila.getString(3));
            }else{
                Toast.makeText(getBaseContext(), "No existe dicho articulo:", Toast.LENGTH_SHORT).show();
            }

            //Cerrando el objecto tipo Cursor y la base de datos
            fila.close();
            db.close();
        }else{
            Toast.makeText(getBaseContext(), "Ingrese un ID.", Toast.LENGTH_SHORT).show();
        }

    }

    private void update(){
        String id = et_id.getText().toString();

        if (!id.isEmpty()){
            ContentValues values = new ContentValues();
            ConectedBD conectedBD = new ConectedBD(this, ArticulosContract.DATABASE_NAME,null,1);
            SQLiteDatabase db = conectedBD.getReadableDatabase();

            values.put(ArticulosContract.NOMBRE, et_nombre.getText().toString());
            values.put(ArticulosContract.PRECIO, et_precio.getText().toString());
            values.put(ArticulosContract.CANTIDAD, Integer.parseInt(et_cantidad.getText().toString()));
            values.put(ArticulosContract.DESCRIPCION, et_descripcion.getText().toString());

            //Actualizamos el elemento con el nombre de la tabla, un objecto contentValues y una clausula WHRERE
            db.update(ArticulosContract.TABLE_NAME,values,"id = "+id,null);

            db.close();
            Toast.makeText(getBaseContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(), "Ingrese un ID.", Toast.LENGTH_SHORT).show();
        }
    }

    private void delete(){
        String id = et_id.getText().toString();

        if (!id.isEmpty()){
            ConectedBD conectedBD = new ConectedBD(this, ArticulosContract.DATABASE_NAME,null,1);
            SQLiteDatabase db = conectedBD.getReadableDatabase();

            //Eliminado el elemento de BD
            db.delete(ArticulosContract.TABLE_NAME,"id = "+id,null);

            et_id.setText("");
            et_nombre.setText("");
            et_precio.setText("");
            et_cantidad.setText("");
            et_descripcion.setText("");

            db.close();
            Toast.makeText(getBaseContext(), "Datos Eliminados", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(), "Ingrese un ID.", Toast.LENGTH_SHORT).show();
        }
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