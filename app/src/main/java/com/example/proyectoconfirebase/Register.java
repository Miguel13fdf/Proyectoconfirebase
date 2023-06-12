package com.example.proyectoconfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    EditText emailTxt;
    EditText passwordTxt;
    EditText nameTxt;
    ImageButton buttonRegister;

    TextView textView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textView = findViewById(R.id.txt_login);
        textView.setOnClickListener(l -> LoginNow());
    }

    public void LoginNow() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    public void RegisterCall(View view) {
        nameTxt = findViewById(R.id.txt_name);
        emailTxt = findViewById(R.id.txt_email_register);
        passwordTxt = findViewById(R.id.txt_password_register);
        buttonRegister = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        String name = nameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(Register.this, "Ingrese " + (TextUtils.isEmpty(email) ? "un email" : "una contraseña de al menos 6 dígitos"), Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            Toast.makeText(Register.this, "Cuenta creada.", Toast.LENGTH_SHORT).show();
                                            // Registro exitoso, redirige al usuario a la siguiente actividad
                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                            startActivity(intent);
                                            finish(); // Opcionalmente, finaliza la actividad actual para evitar que el usuario vuelva atrás
                                        } else {
                                            Toast.makeText(Register.this, "Error al actualizar el perfil.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Si falla el inicio de sesión, muestra un mensaje al usuario
                        Toast.makeText(Register.this, "Falló la autenticación.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
