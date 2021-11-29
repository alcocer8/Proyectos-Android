package com.example.tiendadbx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private FirebaseAuth mAuth;
    View vista;
    EditText et_correo, et_password;
    Button btn_login, btn_face, btn_google;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment

        et_correo = vista.findViewById(R.id.et_correo);
        et_password = vista.findViewById(R.id.et_password);
        btn_login = vista.findViewById(R.id.btn_ingresa);

        btn_login.setOnClickListener( v -> {
            String correo = et_correo.getText().toString();
            String password = et_password.getText().toString();

            if (!correo.isEmpty() && !password.isEmpty()){
                if (password.length() >= 6){
                    mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener( (Activity) vista.getContext(), task -> {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(vista.getContext(), MainActivity.class);
                            //((Activity) vista.getContext()).finish();
                            startActivity(intent);

                        }else{
                            Toast.makeText(vista.getContext(), "Error.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(vista.getContext(), "Tu password debe ser mayor a 6", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(vista.getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
        });

        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(vista.getContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}