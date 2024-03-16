package ba.fpmoz.yumai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailTxt;
    private Button buttonPswReset;
    private ProgressBar progressBar;

    private FirebaseAuth authProfile;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static final String TAG = "RESET_PSW";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText emailTxt = findViewById(R.id.emailTxt);
        Button buttonPswReset = findViewById(R.id.buttonPswReset);

        buttonPswReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();

                Patterns Patters;
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Please enter email address",Toast.LENGTH_LONG).show();
                    emailTxt.setError("Please enter email address");
                    emailTxt.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(),"Wrong email. User not found.",Toast.LENGTH_SHORT).show();
                }
                else{
                    resetPassword(email);
                }
            }
        });

    }

    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Please check your email.",Toast.LENGTH_SHORT).show();

                    Intent Intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(Intent);

                }else{
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    };
}