package ba.fpmoz.yumai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import ba.fpmoz.yumai.adapter.CategoryAdapter;
import ba.fpmoz.yumai.model.Category;

public class CategoriesActivity extends AppCompatActivity {

    //For drawer navigation
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout profile, myRecipes, settings, about, faq, logout;


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ( "https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/" );
    FirebaseAuth mAuth = FirebaseAuth.getInstance ();

    //For bottomNavigation
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;

    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature ( Window.FEATURE_NO_TITLE );
        setContentView ( R.layout.activity_categories );
        LinearLayoutManager layoutManager = new LinearLayoutManager ( this );


        //Drawer navigation

        drawerLayout = findViewById ( R.id.drawerLayout );
        menu = findViewById ( R.id.menu );
        profile = findViewById ( R.id.profile );
        myRecipes = findViewById ( R.id.myRecipes );
        settings = findViewById ( R.id.settings );
        about = findViewById ( R.id.about );
        faq = findViewById ( R.id.faq );
        logout = findViewById ( R.id.logout );

        fab = findViewById ( R.id.saveFab );

        menu.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                openDrawer ( drawerLayout );
            }

        } );

        profile.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                redirectActivity ( CategoriesActivity.this, ProfileActivity.class );
            }
        } );


        myRecipes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( CategoriesActivity.this, MyRecipesActivity.class );

            }
        } );

        settings.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( CategoriesActivity.this, SettingsActivity.class );

            }
        } );

        about.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( CategoriesActivity.this, AboutUsActivity.class );

            }
        } );

        faq.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( CategoriesActivity.this, FAQActivity.class );

            }
        } );

        logout.setOnClickListener ( new View.OnClickListener () {
                                        @Override
                                        public void onClick(View view) {
                                            mAuth.signOut ();
                                            Intent intent = new Intent ( getApplicationContext (), LoginActivity.class );
                                            finish ();
                                            startActivity ( intent );
                                        }
                                    }
        );


        //RecyclerView
        this.categoryRecyclerView = findViewById ( R.id.categoryRecyclerView );
        this.categoryRecyclerView.setLayoutManager (
                new LinearLayoutManager ( this )
        );
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category> ().setQuery (
                this.mDatabase.getReference ( "Categories" ),
                Category.class
        ).build ();


        this.categoryAdapter = new CategoryAdapter ( options );
        this.categoryRecyclerView.setAdapter ( this.categoryAdapter );

        //BottomNavigation
        bottomNavigationView = findViewById ( R.id.bottomNavigationView );
        bottomNavigationView.setSelectedItemId ( R.id.categories);
        bottomNavigationView.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId ()) {
                    case R.id.home:
                        startActivity ( new Intent ( CategoriesActivity.this, MainActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.search:
                        startActivity ( new Intent ( CategoriesActivity.this, SearchActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.categories:

                        return true;

                    case R.id.favourites:
                        startActivity ( new Intent ( CategoriesActivity.this, FavoritesActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;


                }
                return false;
            }
        } );

        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                showBottomDialog ();
            }
        } );

    }

    //DrawerNavigation
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer ( GravityCompat.START );
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen ( GravityCompat.START )) {
            drawerLayout.closeDrawer ( GravityCompat.START );
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent ( activity, secondActivity );
        intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
        activity.startActivity ( intent );
    }

    protected void onPause() {
        super.onPause ();
        closeDrawer ( drawerLayout );
    }
    //RecyclerView listners


    @Override
    protected void onStart() {
        super.onStart ();
        this.categoryAdapter.startListening ();
    }

    @Override
    protected void onStop() {
        super.onStop ();
        this.categoryAdapter.stopListening ();

    }


    //BottomNavigation
    private void showBottomDialog() {

        final Dialog dialog = new Dialog ( this );
        dialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
        dialog.setContentView ( R.layout.bottomsheetlayout );

        LinearLayout recipeLayout = dialog.findViewById ( R.id.layoutRecipe );
        LinearLayout airecipeLayout = dialog.findViewById ( R.id.layoutAIRecipe );
        ImageView cancelButton = dialog.findViewById ( R.id.cancelButton );

        recipeLayout.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {



                startActivity ( new Intent ( CategoriesActivity.this, AddRecipeActivity.class ) );

            }
        } );

        airecipeLayout.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                dialog.dismiss ();
                Toast.makeText ( CategoriesActivity.this, "Add new AIrecipe is clicked", Toast.LENGTH_SHORT ).show ();

            }
        } );

        cancelButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                dialog.dismiss ();
            }
        } );

        dialog.show ();
        dialog.getWindow ().setLayout ( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.getWindow ().setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
        dialog.getWindow ().getAttributes ().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow ().setGravity ( Gravity.BOTTOM );

    }
}