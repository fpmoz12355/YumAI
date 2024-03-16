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
import android.provider.ContactsContract;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicMarkableReference;

import ba.fpmoz.yumai.model.Recipe;
import ba.fpmoz.yumai.model.UserProfile;

public class RecipeDetailsActivity extends AppCompatActivity  {

    //For drawer navigation
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout profile, myRecipes, settings, about, faq, logout;

    TextView recipeTitleTxt;
    ImageView recipeImage;

    String recipeAuthor;
    TextView cardDescription;


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //For bottomNavigation
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    private String ingredients;

    TextView recipeDetailsIngredientRow;

    TextView recipeDetailsIngredientQtyRow;
    private TextView recipeInstructionsTxt;
    private TextView recipeServingTxt;
    private TextView recipeTimeTxt;
    private TextView recipeComplexityTxt;
    private TextView recipeCategoryTxt;
    private TextView recipeAuthorTxt;
    private DataSnapshot dataSnapshot;
    private String Author_ID;
    private String displayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView ( R.layout.activity_recipe_details );


        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/");
        this.recipeImage = findViewById ( R.id.RecipeDetailsImage );
        this.recipeTitleTxt = findViewById ( R.id.recipeTitleTxt );
        this.cardDescription = findViewById ( R.id.recipeDetailsCardDescription);
        this.recipeInstructionsTxt = findViewById ( R.id.instructionsTv );
        this.recipeCategoryTxt = findViewById ( R.id.recipeDetailsCategoryTxt );
        this.recipeServingTxt = findViewById ( R.id.recipeDetailsServingsTxt );
        this.recipeComplexityTxt = findViewById ( R.id.recipeDetailsComplexityTxt);
        this.recipeTimeTxt = findViewById ( R.id.recipeDetailsCookingTimeTxt);
        this.recipeAuthorTxt = findViewById ( R.id.recipeDetailsAuthorTxt);

        Intent i = getIntent ();
        String recipeId = i.getStringExtra ( "recipeId" );

        //For recipe details
        DatabaseReference recipeReference = this.mDatabase.getReference ("recipe").child ( recipeId );



        ValueEventListener valueEventListener = recipeReference.addValueEventListener ( new ValueEventListener () {
            public FirebaseDatabase mDatabase;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists ()) {
                    Recipe recipe = snapshot.getValue ( Recipe.class );
                    recipeTitleTxt.setText ( recipe.getTitle () );
                    recipeInstructionsTxt.setText (recipe.getDescription ());
                    recipeComplexityTxt.setText ( recipe.getComplexity () );
                    recipeServingTxt.setText ( recipe.getServings () );
                    recipeTimeTxt.setText ( recipe.getCooking_time () );
                    recipeCategoryTxt.setText ( recipe.getCategory () );

                    String Author_ID;
                    Author_ID = (recipe.getAuthor_id ());

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference authorReference = database.getReference("Profile").child(Author_ID);

                    authorReference.addValueEventListener (new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                UserProfile authorProfile = dataSnapshot.getValue(UserProfile.class);

                                displayName = authorProfile.getDisplayname();
                                recipeAuthorTxt.setText ( displayName );


                            } else {
                                displayName= "unknown author" ;

                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                    String imageUrl = recipe.getImage ();
                    Picasso.get ().load ( imageUrl ).into ( recipeImage );
                    Map<String, Object> ingredients = recipe.getIngredients();

                    for (Map.Entry<String, Object> entry : ingredients.entrySet()) {
                        String ingredientItem = entry.getKey();
                        Object ingredientQtyItem = entry.getValue();
                        addView(ingredientItem, ingredientQtyItem.toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

        //For ingredinets

        DatabaseReference recipeIngredientsReference = this.mDatabase.getReference ("recipe").child ( "ingredients" );

        ArrayList<String> ingredientsList = new ArrayList<>();
        ArrayList<String> quantitiesList = new ArrayList<> ();

        ValueEventListener valueEventListenerIngredients = recipeIngredientsReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if there is any data
                if (snapshot.exists()) {
                    // Iterate through the dataSnapshot (HashMap under 'ingredients')
                    for (DataSnapshot ingredientSnapshot : snapshot.getChildren()) {
                        String key = ingredientSnapshot.getKey().toString ();
                        String value = ingredientSnapshot.getValue(String.class).toString ();

                        // Add key and value to respective ArrayLists
                        ingredientsList.add(key);
                        quantitiesList.add(value);


                         }

                    Toast.makeText ( getApplicationContext (), quantitiesList.size (), Toast.LENGTH_SHORT ).show ();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        } );
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
                redirectActivity(RecipeDetailsActivity.this, ProfileActivity.class);
            }
        });


        myRecipes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( RecipeDetailsActivity.this, MyRecipesActivity.class );

            }
        } );

        settings.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( RecipeDetailsActivity.this, SettingsActivity.class );

            }
        } );

        about.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( RecipeDetailsActivity.this, AboutUsActivity.class );

            }
        } );

        faq.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( RecipeDetailsActivity.this, FAQActivity.class );

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
                        startActivity(new Intent(RecipeDetailsActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(RecipeDetailsActivity.this, SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.categories:
                        startActivity(new Intent(RecipeDetailsActivity.this, CategoriesActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.favourites:
                        startActivity(new Intent(RecipeDetailsActivity.this, FavoritesActivity.class));
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


                startActivity(new Intent(RecipeDetailsActivity.this, AddRecipeActivity.class));

            }
        });

        airecipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(RecipeDetailsActivity.this,"Add new AIrecipe is clicked",Toast.LENGTH_SHORT).show();

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

    private void addView(String IngredientItem, String IngredientQtyItem) {

        final View ingredientView = getLayoutInflater ().inflate ( R.layout.row_recipe_details, null, false );
        TextView recipeDetailsIngredientRow = ingredientView.findViewById ( R.id.recipeDetailsIngredientRow );
        recipeDetailsIngredientRow.setText ( IngredientItem );
        TextView recipeDetailsIngredientQtyRow = ingredientView.findViewById ( R.id.recipeDetailsIngredientQtyRow);
        recipeDetailsIngredientQtyRow.setText ( IngredientQtyItem );

        ViewGroup container = findViewById(R.id.recipeDetailsIngredients);
        container.addView(ingredientView);

    }
}