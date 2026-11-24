package com.example.exam2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Snackbar exitSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (view, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                });

        EditText emailInput = findViewById(R.id.editEmail);
        EditText passwordInput = findViewById(R.id.editPassword);
        Button loginButton = findViewById(R.id.btnLogin);

        TextWatcher validationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable text) {
                String email = emailInput.getText().toString().trim();
                boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
                boolean validPassword = passwordInput.length() >= 6;
                loginButton.setEnabled(validEmail && validPassword);
            }
        };

        emailInput.addTextChangedListener(validationWatcher);
        passwordInput.addTextChangedListener(validationWatcher);

        loginButton.setOnClickListener(view -> {
            if (exitSnackbar != null) {
                exitSnackbar.dismiss();
                exitSnackbar = null;
            }

            passwordInput.setText("");
            startActivity(new Intent(MainActivity.this, ModeActivity.class));
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (exitSnackbar != null && exitSnackbar.isShown()) {
                    finishAndRemoveTask();
                    return;
                }

                exitSnackbar = Snackbar.make(
                        findViewById(R.id.main),
                        "Press back again to exit",
                        Snackbar.LENGTH_LONG
                );
                exitSnackbar.show();
            }
        });
    }
}