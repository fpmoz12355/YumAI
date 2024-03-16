package ba.fpmoz.yumai;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.fpmoz.yumai.adapter.RecipeAdapter;
import ba.fpmoz.yumai.model.Recipe;

public class SingleCategoryActivity extends AppCompatActivity {

    //For drawer navigation
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout profile, myRecipes, settings, about, faq, logout;

    //For recyclerView
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ( "https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/" );
    FirebaseAuth mAuth = FirebaseAuth.getInstance ();

    //For bottomNavigation
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature ( Window.FEATURE_NO_TITLE );
        setContentView ( R.layout.activity_single_category );
        LinearLayoutManager layoutManager = new LinearLayoutManager ( this );


        mAuth = FirebaseAuth.getInstance ();


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
        TextView categoryTitleTxt = findViewById ( R.id.CategoryTitleTxt );

        Intent i = getIntent ();
        String categoryTitle = i.getStringExtra ( "categoryTitle" );
        categoryTitleTxt.setText ( categoryTitle );

        DatabaseReference recipeReference = this.mDatabase.getReference ("Categories").child ( categoryTitle );



        menu.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                openDrawer ( drawerLayout );
            }

        } );

        profile.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SingleCategoryActivity.this, ProfileActivity.class );
            }
        } );


        myRecipes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SingleCategoryActivity.this, MyRecipesActivity.class );

            }
        } );

        settings.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SingleCategoryActivity.this, SettingsActivity.class );

            }
        } );

        about.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SingleCategoryActivity.this, AboutUsActivity.class );

            }
        } );

        faq.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SingleCategoryActivity.this, FAQActivity.class );

            }
        } );

        logout.setOnClickListener ( new View.OnClickListener () {
                                        @Override
                                        public void onClick(View view) {
                                            mAuth.signOut ();
                                            Intent intent = new Intent ( getApplicationContext (), LoginActivity.class );
                                            startActivity ( intent );
                                        }
                                    }
        );

        //RecyclerView
        this.recyclerView = findViewById ( R.id.categoryRecyclerView );
        this.recyclerView.setLayoutManager (
                new LinearLayoutManager ( this )

        );

        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(this.mDatabase.getReference("recipe").orderByChild ("category").equalTo(categoryTitle), Recipe.class)
                .build();


        this.recipeAdapter = new RecipeAdapter ( options );
        this.recyclerView.setAdapter ( this.recipeAdapter );

        //BottomNavigation
        bottomNavigationView = findViewById ( R.id.bottomNavigationView );
        bottomNavigationView.setSelectedItemId ( R.id.home );
        bottomNavigationView.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId ()) {
                    case R.id.home:
                        return true;

                    case R.id.search:
                        startActivity ( new Intent ( SingleCategoryActivity.this, SearchActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.categories:
                        startActivity ( new Intent ( SingleCategoryActivity.this, CategoriesActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.favourites:
                        startActivity ( new Intent ( SingleCategoryActivity.this, FavoritesActivity.class ) );
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

    //RecyclerView


    @Override
    protected void onStart() {
        super.onStart ();
        this.recipeAdapter.startListening ();
    }

    @Override
    protected void onStop() {
        super.onStop ();
        this.recipeAdapter.stopListening ();

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


                startActivity ( new Intent ( SingleCategoryActivity.this, AddRecipeActivity.class ) );

            }
        } );

        airecipeLayout.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                dialog.dismiss ();
                Toast.makeText ( SingleCategoryActivity.this, "Add new AIrecipe is clicked", Toast.LENGTH_SHORT ).show ();

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