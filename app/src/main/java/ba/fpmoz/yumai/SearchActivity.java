package ba.fpmoz.yumai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ba.fpmoz.yumai.adapter.RecipeAdapter;
import ba.fpmoz.yumai.model.Recipe;

public class SearchActivity extends AppCompatActivity {

    //For drawer navigation
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout profile, myRecipes, settings, about, faq, logout;

    //For bottomNavigation
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;

    //For search
    private SearchView searchView;

    //For recyclerView
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ( "https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/" );
    FirebaseAuth mAuth = FirebaseAuth.getInstance ();
    DatabaseReference recipeReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView ( R.layout.activity_search );

        recyclerView = findViewById(R.id.recyclerView);


        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/");
        recipeReference = mDatabase.getReference ("recipe");

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase ();
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase ();
                performEmptySearch (query);
                return false;

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
                redirectActivity(SearchActivity.this, ProfileActivity.class);
            }
        });


        myRecipes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SearchActivity.this, MyRecipesActivity.class );

            }
        } );

        settings.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SearchActivity.this, SettingsActivity.class );

            }
        } );

        about.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SearchActivity.this, AboutUsActivity.class );

            }
        } );

        faq.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( SearchActivity.this, FAQActivity.class );

            }
        } );



        //BottomNavigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(SearchActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        return true;

                    case R.id.categories:
                        startActivity(new Intent(SearchActivity.this, CategoriesActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.favourites:
                        startActivity(new Intent(SearchActivity.this, FavoritesActivity.class));
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

        //RecyclerView
        this.recyclerView = findViewById ( R.id.recyclerView );
        this.recyclerView.setLayoutManager (
                new LinearLayoutManager ( this )
        );
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe> ().setQuery (
                this.mDatabase.getReference ( "recipe" ).orderByChild ( "search" ),
                Recipe.class
        ).build ();


        this.recipeAdapter = new RecipeAdapter ( options );
        this.recyclerView.setAdapter ( this.recipeAdapter );

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


    private void performSearch(String query) {
        if (!query.isEmpty()) {

            Query searchQuery = recipeReference
                .orderByChild("search")
                .startAt (query)
                .endAt(query + "\uf8ff");



        searchQuery.addListenerForSingleValueEvent(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(searchQuery, Recipe.class).build();

        recipeAdapter.updateOptions(options);
        recipeAdapter.startListening();
    }

}

     private void performEmptySearch(String query){

        if (query.isEmpty ()) {

            this.recyclerView = findViewById ( R.id.recyclerView );
            this.recyclerView.setLayoutManager (
                    new LinearLayoutManager ( this )
            );
            FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe> ().setQuery (
                    this.mDatabase.getReference ( "recipe" ),
                    Recipe.class
            ).build ();


            this.recipeAdapter = new RecipeAdapter ( options );
            this.recyclerView.setAdapter ( this.recipeAdapter );

        }
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

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout recipeLayout = dialog.findViewById(R.id.layoutRecipe);
        LinearLayout airecipeLayout = dialog.findViewById(R.id.layoutAIRecipe);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        recipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(SearchActivity.this, AddRecipeActivity.class));

            }
        });

        airecipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(SearchActivity.this,"Add new AIrecipe is clicked",Toast.LENGTH_SHORT).show();

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