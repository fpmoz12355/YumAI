package ba.fpmoz.yumai;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.CharArrayWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddAIRecipeActivity extends AppCompatActivity implements View.OnClickListener {
    String[] categorie_items = {"Breakfast", "Main-course", "Salad", "Appetizer", "Side-dish", "Dessert", "Snack", "Soup", "Baked-goods"};
    String[] complexity_items = {"Low", "Medium", "High"};
    AutoCompleteTextView autoCompleteTxtCategories;
    AutoCompleteTextView autoCompleteTxtComplexity;
    ArrayAdapter<String> adapterItemsCategories;
    ArrayAdapter<String> adapterItemsComplexity;

    LinearLayout layoutList;
    Button buttonAdd;

    Map<String, String> ingredientsList = new HashMap<> ();

    Button uploadImageBtn;
    ImageView imagePreview;

    FirebaseStorage storage;
    com.google.firebase.storage.StorageReference storageReference;

    Uri filePath;
    String recipeImage = "https://firebasestorage.googleapis.com/v0/b/yumai-a1500.appspot.com/o/images%2Fplate_placeholder.png?alt=media&token=40aaea71-c4be-471b-b138-aa9aad7acdf8";



    //For drawer navigation
    DrawerLayout drawerLayout;
    NestedScrollView scrollView;
    ImageView menu;
    LinearLayout profile, myRecipes, settings, about, faq, logout;


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ( "https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/" );
    FirebaseAuth mAuth = FirebaseAuth.getInstance ();

    //For bottomNavigation
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature ( Window.FEATURE_NO_TITLE );
        setContentView ( R.layout.activity_add_ai_recipe );

        mAuth = FirebaseAuth.getInstance ();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ( "https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/" );

        this.imagePreview = findViewById ( R.id.imagePreview );

        this.storage = FirebaseStorage.getInstance ();
        this.storageReference = this.storage.getReference ();





        EditText additional_ingredientsEdTxt = findViewById ( R.id.additional_ingredientsEdTxt );
        EditText recipeCookingTimeEdTxt = findViewById ( R.id.recipeCookingTimeEdTxt );
        EditText recipeDescriptionEdTxt = findViewById ( R.id.recipeDescriptionEdTxt );
        EditText recipeServingsEdTxt = findViewById ( R.id.recipeServingsEdTxt );
        scrollView = findViewById ( R.id.scroll_view );


        DatabaseReference recipeReference = mDatabase.getReference ( "recipe" );


        //dropdown categories
        autoCompleteTxtCategories = findViewById ( R.id.auto_complete_categories );
        adapterItemsCategories = new ArrayAdapter<String> ( this, R.layout.categorie_list_item, categorie_items );
        autoCompleteTxtCategories.setAdapter ( adapterItemsCategories );
        autoCompleteTxtCategories.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition ( position ).toString ();


            }
        } );

        //dropdown complexity
        autoCompleteTxtComplexity = findViewById ( R.id.auto_complete_complexity );
        adapterItemsComplexity = new ArrayAdapter<String> ( this, R.layout.complexity_list_item, complexity_items );
        autoCompleteTxtComplexity.setAdapter ( adapterItemsComplexity );
        autoCompleteTxtComplexity.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition ( position ).toString ();

            }
        } );

        //Ingredients list

        layoutList = findViewById ( R.id.layout_list );
        buttonAdd = findViewById ( R.id.button_add );
        buttonAdd.setOnClickListener ( this );

        //Drawer navigation

        drawerLayout = findViewById ( R.id.drawerLayout );
        menu = findViewById ( R.id.menu );
        profile = findViewById ( R.id.profile );
        myRecipes = findViewById ( R.id.myRecipes );
        settings = findViewById ( R.id.settings );
        about = findViewById ( R.id.about );
        faq = findViewById ( R.id.faq );
        logout = findViewById ( R.id.logout );

        fab = findViewById ( R.id.saveRecipeBtn );


        menu.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                openDrawer ( drawerLayout );
            }

        } );

        profile.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddAIRecipeActivity.this, ProfileActivity.class );
            }
        } );


        myRecipes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddAIRecipeActivity.this, MyRecipesActivity.class );

            }
        } );

        settings.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddAIRecipeActivity.this, SettingsActivity.class );

            }
        } );

        about.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddAIRecipeActivity.this, AboutUsActivity.class );

            }
        } );

        faq.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddAIRecipeActivity.this, FAQActivity.class );

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

        autoCompleteTxtCategories.setText ( "Breakfast" );
        autoCompleteTxtComplexity.setText ( "Low" );
        recipeCookingTimeEdTxt.setText ( "00:20" );
        recipeServingsEdTxt.setText ( "2" );
        additional_ingredientsEdTxt.setText ("2");
        recipeDescriptionEdTxt.setText ( "no cheese" );


        //BottomNavigation
        bottomNavigationView = findViewById ( R.id.bottomNavigationView );
        bottomNavigationView.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId ()) {
                    case R.id.home:
                        startActivity ( new Intent ( AddAIRecipeActivity.this, MainActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.search:
                        startActivity ( new Intent ( AddAIRecipeActivity.this, SearchActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.categories:
                        startActivity ( new Intent ( AddAIRecipeActivity.this, CategoriesActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.favourites:
                        startActivity ( new Intent ( AddAIRecipeActivity.this, FavoritesActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;


                }
                return false;
            }
        } );

        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String title = additional_ingredientsEdTxt.getText().toString();
                String search = title.toLowerCase(Locale.ENGLISH);
                String category = String.valueOf ( autoCompleteTxtCategories.getText ().toString () );
                String complexity = String.valueOf ( autoCompleteTxtComplexity.getText ().toString () );
                String cooking_time = String.valueOf ( recipeCookingTimeEdTxt.getText ().toString () );
                String servings = String.valueOf ( recipeServingsEdTxt.getText ().toString () );
                String description = String.valueOf ( recipeDescriptionEdTxt.getText ().toString () );
                String additional_ingredients = String.valueOf ( additional_ingredientsEdTxt.getText ().toString () );

                String image = "https://firebasestorage.googleapis.com/v0/b/yumai-a1500.appspot.com/o/images%2FlogoAI.png?alt=media&token=4632b906-890c-40fb-ab85-619652a3f994";
                Map<String, Object> ingredients = new HashMap<> ();
                for (int i = 0; i < ingredientETs.size (); i++) {
                    String ingredientName = ingredientETs.get ( i ).getText ().toString ();
                    String ingredientQty = ingredientQtyETs.get ( i ).getText ().toString ();
                    if (!ingredientName.equals ( "" ) && !ingredientQty.equals ( "" ))
                        ingredients.put ( ingredientName, ingredientQty );
                }

                boolean AI = true;
                String author_id = mAuth.getCurrentUser ().getUid ();

                Long tsLong = System.currentTimeMillis ();
                Date date = new Date ( tsLong );


//                // Format the date as dd-mm-yyyy
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                String formattedDate = dateFormat.format(date);

                Long timestamplong = tsLong;

                String timestamp = timestamplong.toString ();

                if ( category.equals("") || complexity.equals("") || cooking_time.equals("") || servings.equals("") || description.equals("") ||  ingredients.isEmpty ())  {
                    Toast.makeText(getBaseContext(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                } else {

                    StringBuilder ingredientString = new StringBuilder();

                    for (Map.Entry<String, Object> entry : ingredients.entrySet()) {
                        String ingredient = entry.getKey() + ": " + entry.getValue() + ", ";
                        ingredientString.append(ingredient);
                    }



                    String prompt = "Generate a " + category + "recipe with the following ingredients " + ingredientString.toString () + ". The recipe should serve " +servings + "people, have a maximum preparation time of " + cooking_time + " hours and be formatted in JSON. Include details such as category, complexity, cooking time, instructions (in the description), ingredients, search term, servings, and title. You can add" + additional_ingredients + "additional ingredients to make recipe more complex and delicious. And please:"+ description;
                    Log.d ("Prompt", prompt);

//                    Recipe r = new Recipe ( title, search, category, complexity, cooking_time, servings, description, ingredients, recipeImage, AI, author_id, timestamp );
//                    recipeReference.push ().setValue ( r );
//                    Toast.makeText(getBaseContext(), "You have successfully added a recipe.", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent ( AddAIRecipeActivity.this, MainActivity.class );
//                    startActivity ( i );
//
                }
            }
        } );

    }






    @Override
    public void onClick(View v) {

        switch (v.getId ()) {

            case R.id.button_add:

                addView ();

                break;


        }


    }

    List<EditText> ingredientETs = new ArrayList<> ();
    List<EditText> ingredientQtyETs = new ArrayList<> ();

    private void addView() {

        final View ingredientView = getLayoutInflater ().inflate ( R.layout.row_add_recipe, null, false );
        EditText editText = ingredientView.findViewById ( R.id.edit_ingredient );
        ingredientETs.add ( editText );
        EditText editText2 = ingredientView.findViewById ( R.id.edit_ingredients_quantity );
        ingredientQtyETs.add ( editText2 );

        ImageView imageClose = (ImageView) ingredientView.findViewById ( R.id.image_remove );


        imageClose.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                removeView ( ingredientView );
            }
        } );

        layoutList.addView ( ingredientView );
        scrollView.fullScroll ( View.FOCUS_DOWN );

    }

    private void removeView(View view) {

        layoutList.removeView ( view );

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


                startActivity ( new Intent ( AddAIRecipeActivity.this, AddRecipeActivity.class ) );

            }
        } );

        airecipeLayout.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                startActivity ( new Intent ( AddAIRecipeActivity.this, AddAIRecipeActivity.class ) );

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged ( hasCapture );
    }
}