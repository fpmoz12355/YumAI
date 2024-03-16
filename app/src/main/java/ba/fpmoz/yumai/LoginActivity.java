package ba.fpmoz.yumai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(mAuth.getCurrentUser() != null){

        }

        EditText loginEmailTxt = findViewById(R.id.LoginEmailTxt);
        EditText loginPasswordTxt = findViewById(R.id.LoginPasswordTxt);


        Button loginBtn = findViewById(R.id.LoginBtn);

        loginBtn.setOnLongClickListener(view -> {
            loginEmailTxt.setText("lucijaz98@gmail.com");
            loginPasswordTxt.setText("12345678");
            return true;
        });

        TextView forgotPsw = findViewById(R.id.forgotPsw);

        forgotPsw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmailTxt.getText().toString();
                String password = loginPasswordTxt.getText().toString();
                if (email.equals("") && password.equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), "Welcome to YumAI!", Toast.LENGTH_SHORT).show();
                                Intent movieIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(movieIntent);
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), "Wrong credentials!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });

    }
}