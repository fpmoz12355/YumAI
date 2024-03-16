package ba.fpmoz.yumai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class AboutUsActivity extends AppCompatActivity {

    //For drawer navigation
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout profile, myRecipes, settings, about, faq, logout;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    TextView question1, answer1, question2, answer2, question3, answer3, question4, answer4;

    //For bottomNavigation
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView ( R.layout.activity_about_us );

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/");

        TextView question1 = (TextView) findViewById(R.id.question1);
        TextView answer1 = (TextView) findViewById(R.id.answer1);
        TextView question2 = (TextView) findViewById(R.id.question2);
        TextView answer2 = (TextView) findViewById(R.id.answer2);
        TextView question3 = (TextView) findViewById(R.id.question3);
        TextView answer3 = (TextView) findViewById(R.id.answer3);
        TextView question4 = (TextView) findViewById(R.id.question4);
        TextView answer4 = (TextView) findViewById(R.id.answer4);


        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer1.getVisibility() == View.GONE) {
                    answer1.setVisibility(View.VISIBLE);
                } else {
                    answer1.setVisibility(View.GONE);
                }
            }
        });

        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer2.getVisibility() == View.GONE) {
                    answer2.setVisibility(View.VISIBLE);
                } else {
                    answer2.setVisibility(View.GONE);
                }
            }
        });

        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer3.getVisibility() == View.GONE) {
                    answer3.setVisibility(View.VISIBLE);
                } else {
                    answer3.setVisibility(View.GONE);
                }
            }
        });

        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer4.getVisibility() == View.GONE) {
                    answer4.setVisibility(View.VISIBLE);
                } else {
                    answer4.setVisibility(View.GONE);
                }
            }
        });





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

                redirectActivity(AboutUsActivity.this, ProfileActivity.class);
            }
        });


        myRecipes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AboutUsActivity.this, MyRecipesActivity.class );

            }
        } );

        settings.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AboutUsActivity.this, SettingsActivity.class );

            }
        } );

        about.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AboutUsActivity.this, AboutUsActivity.class );

            }
        } );

        faq.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AboutUsActivity.this, FAQActivity.class );

            }
        } );

        logout.setOnClickListener ( new View.OnClickListener () {
                                        @Override
                                        public void onClick(View view) {
                                            mAuth.signOut();
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }
        );


        //BottomNavigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(AboutUsActivity.this, SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.categories:
                        startActivity(new Intent(AboutUsActivity.this, CategoriesActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.favourites:
                        startActivity(new Intent(AboutUsActivity.this, FavoritesActivity.class));
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
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
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


                startActivity(new Intent(AboutUsActivity.this, AddRecipeActivity.class));

            }
        });

        airecipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(AboutUsActivity.this,"Add new AIrecipe is clicked",Toast.LENGTH_SHORT).show();

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
}