package ba.fpmoz.yumai;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ba.fpmoz.yumai.model.UserProfile;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "REGISTRATION";

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance(("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // Polja za unos podataka
        EditText fullnameTxt = findViewById(R.id.registerDisplayNameTxt);
        EditText registerEmailTxt = findViewById(R.id.registerMailTxt);
        EditText registerPhoneTxt = findViewById(R.id.registerPhoneTxt);
        EditText registerPasswordTxt = findViewById(R.id.registerPasswordTxt);
        EditText registerPasswordCnfTxt = findViewById(R.id.registerPasswordCnf);
        TextView noAccount = findViewById(R.id.noAccount);

        noAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // Gumb za registraciju
        Button registerBtn = findViewById(R.id.saveProfileBtn);

        // Što se događa nakon klika
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dohvaćanje podataka
                String dispalyname = fullnameTxt.getText().toString();
                String phone = registerPhoneTxt.getText().toString();
                String email = registerEmailTxt.getText().toString();
                String password = registerPasswordTxt.getText().toString();
                String passwordCnf = registerPasswordCnfTxt.getText().toString();
                String user_image = "";
                ArrayList my_favourites = new ArrayList<> ();
                ArrayList my_recipes = new ArrayList<> ();

                Log.d(TAG, "userData: " + dispalyname + " " + phone);

                if (!dispalyname.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && password.equals(passwordCnf)) {
                    Log.d(TAG, "ifSuccess");
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //Dodavanje u realtime bazu
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                UserProfile user = new UserProfile( dispalyname, phone, email, my_favourites);

                                DatabaseReference profileRef = mDatabase.getReference("Profile");

                                profileRef.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            firebaseUser.sendEmailVerification();

                                            fullnameTxt.setText("");
                                            registerPhoneTxt.setText("");
                                            registerEmailTxt.setText("");
                                            registerPasswordTxt.setText("");
                                            registerPasswordCnfTxt.setText("");

                                            Log.d(TAG, "uploadProfile: uspješno");
                                            Toast.makeText(getApplicationContext(), "Registracija je uspješna. Molimo verficirajte email.", Toast.LENGTH_LONG).show();
                                        }else{
                                            Log.d(TAG, "uploadProfile: neuspješno");
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Greška prilikom unosa ",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }
}