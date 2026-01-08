package com.ieltsbeta.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, dobInput, emailInput, passwordInput;
    private Button signUpBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        dobInput = findViewById(R.id.dob_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        signUpBtn = findViewById(R.id.signup_btn);

        signUpBtn.setOnClickListener(v -> {
            String first = firstNameInput.getText().toString().trim();
            String last = lastNameInput.getText().toString().trim();
            String dob = dobInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String pass = passwordInput.getText().toString().trim();

            if (first.isEmpty() || last.isEmpty() || dob.isEmpty()
                    || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Account created! Please login.", Toast.LENGTH_LONG).show();
                            finish(); // go back to Login
                        } else {
                            Toast.makeText(
                                    this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });
    }
}