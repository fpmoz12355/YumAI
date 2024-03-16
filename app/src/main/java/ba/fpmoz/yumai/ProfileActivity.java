package ba.fpmoz.yumai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import ba.fpmoz.yumai.model.UserProfile;

public class ProfileActivity extends AppCompatActivity {

    //For drawer navigation
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout profile, myRecipes, settings, about, faq, logout;

    TextView profiledisplaynameTxt, profilephoneTxt, profileemailTxt;


    Button saveProfileBtn;
    FirebaseAuth mAuth;


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/");

    FirebaseStorage storage;
    com.google.firebase.storage.StorageReference storageReference;

    Uri filePath;

    //For bottomNavigation
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView ( R.layout.activity_profile );

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/");




        this.saveProfileBtn = findViewById ( R.id.saveProfileBtn );

        this.storage = FirebaseStorage.getInstance ();
        this.storageReference = this.storage.getReference ();




        FirebaseUser currentUser = mAuth.getCurrentUser();
        profiledisplaynameTxt = findViewById(R.id.profileDisplaynameTxt);
        profilephoneTxt = findViewById(R.id.profilePhoneTxt);
        profileemailTxt = findViewById(R.id.profileEmailTxt);
        TextView changePsw = findViewById(R.id.changePswBtn);

        changePsw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });


        if(currentUser != null){
            DatabaseReference profileRef = mDatabase.getReference("Profile").child(currentUser.getUid());

            profileRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot> () {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    UserProfile profileUser = task.getResult().getValue(UserProfile.class);
                    if(profileUser != null){
                    //    FullName.setText(profileUser.getFullname());
                        profiledisplaynameTxt.setText(profileUser.getDisplayname ());
                        profilephoneTxt.setText(profileUser.getPhone());
                        profileemailTxt.setText(profileUser.getEmail());
                    }
                }
            });
            saveProfileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String displayname = profiledisplaynameTxt.getText().toString();
                    String email = profileemailTxt.getText().toString();
                    String phone = profilephoneTxt.getText().toString();
                    ArrayList my_favourites = null;


                    UserProfile user = new UserProfile(displayname, email, phone, my_favourites);
                    profileRef.setValue(user);

                    Toast.makeText(getApplicationContext(), "Uspješno ste promijenili podatke", Toast.LENGTH_SHORT);
                }
            });
        }



        //Drawer navigation

        drawerLayout = findViewById ( R.id.drawerLayout );
        menu = findViewById ( R.id.menu );
        profile= findViewById ( R.id.profile );
        myRecipes = findViewById ( R.id.myRecipes );
        settings = findViewById ( R.id.settings );
        about = findViewById ( R.id.about );
        faq = findViewById ( R.id.faq );
        logout = findViewById ( R.id.logout );

        fab = findViewById(R.id.saveFab );


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }

        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(ProfileActivity.this, ProfileActivity.class);
            }
        });


        myRecipes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( ProfileActivity.this, MyRecipesActivity.class );

            }
        } );

        settings.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( ProfileActivity.this, SettingsActivity.class );

            }
        } );

        about.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( ProfileActivity.this, AboutUsActivity.class );

            }
        } );

        faq.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( ProfileActivity.this, FAQActivity.class );

            }
        } );


        //BottomNavigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.categories:
                        startActivity(new Intent(ProfileActivity.this, CategoriesActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.favourites:
                        startActivity(new Intent(ProfileActivity.this, FavoritesActivity.class));
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

    }

    //DrawerNavigation
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer ( GravityCompat.START );
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen ( GravityCompat.START )){
            drawerLayout.closeDrawer ( GravityCompat.START );
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent (activity, secondActivity);
        activity.startActivity ( intent );
    }

    protected void onPause(){
        super.onPause ();
        closeDrawer ( drawerLayout );
    }


    //BottomNavigation
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout recipeLayout = dialog.findViewById(R.id.layoutRecipe);
        LinearLayout airecipeLayout = dialog.findViewById(R.id.layoutAIRecipe);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        recipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(ProfileActivity.this, AddRecipeActivity.class));

            }
        });

        airecipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(ProfileActivity.this,"Add new AIrecipe is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable ( Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity( Gravity.BOTTOM);

    }

    //Provjeravanje korisnikove prijave
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =mAuth.getCurrentUser();
        if(user != null){



        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }


}