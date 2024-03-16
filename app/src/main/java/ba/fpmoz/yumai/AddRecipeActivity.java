package ba.fpmoz.yumai;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import ba.fpmoz.yumai.model.Recipe;


public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {
    String[] categorie_items = {"Breakfast", "Main-course", "Salad", "Apetizer", "Side-dish", "Dessert", "Snack", "Soup", "Baked-goods"};
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
        setContentView ( R.layout.activity_addrecipe );

        mAuth = FirebaseAuth.getInstance ();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ( "https://yumai-a1500-default-rtdb.europe-west1.firebasedatabase.app/" );


        this.uploadImageBtn = findViewById ( R.id.uploadImageBtn );
        this.imagePreview = findViewById ( R.id.imagePreview );

        this.storage = FirebaseStorage.getInstance ();
        this.storageReference = this.storage.getReference ();


        uploadImageBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                selectImage ();
            }
        } );

        EditText recipeTitleEdTxt = findViewById ( R.id.recipeTitleEdTxt );


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
                redirectActivity ( AddRecipeActivity.this, ProfileActivity.class );
            }
        } );


        myRecipes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddRecipeActivity.this, MyRecipesActivity.class );

            }
        } );

        settings.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddRecipeActivity.this, SettingsActivity.class );

            }
        } );

        about.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddRecipeActivity.this, AboutUsActivity.class );

            }
        } );

        faq.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                redirectActivity ( AddRecipeActivity.this, FAQActivity.class );

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

        recipeTitleEdTxt.setText ( "Title" );
        autoCompleteTxtCategories.setText ( "Cat" );
        autoCompleteTxtComplexity.setText ( "Com" );
        recipeCookingTimeEdTxt.setText ( "00:10" );
        recipeServingsEdTxt.setText ( "1" );
        recipeDescriptionEdTxt.setText ( "tekst" );


        //BottomNavigation
        bottomNavigationView = findViewById ( R.id.bottomNavigationView );
        bottomNavigationView.setSelectedItemId ( R.id.home );
        bottomNavigationView.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId ()) {
                    case R.id.home:
                        startActivity ( new Intent ( AddRecipeActivity.this, MainActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.search:
                        startActivity ( new Intent ( AddRecipeActivity.this, SearchActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.categories:
                        startActivity ( new Intent ( AddRecipeActivity.this, CategoriesActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.favourites:
                        startActivity ( new Intent ( AddRecipeActivity.this, FavoritesActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;


                }
                return false;
            }
        } );

        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String title = recipeTitleEdTxt.getText().toString();
                String search = title.toLowerCase(Locale.ENGLISH);
                String category = String.valueOf ( autoCompleteTxtCategories.getText ().toString () );
                String complexity = String.valueOf ( autoCompleteTxtComplexity.getText ().toString () );
                String cooking_time = String.valueOf ( recipeCookingTimeEdTxt.getText ().toString () );
                String servings = String.valueOf ( recipeServingsEdTxt.getText ().toString () );
                String description = String.valueOf ( recipeDescriptionEdTxt.getText ().toString () );
                Map<String, Object> ingredients = new HashMap<> ();
                for (int i = 0; i < ingredientETs.size (); i++) {
                    String ingredientName = ingredientETs.get ( i ).getText ().toString ();
                    String ingredientQty = ingredientQtyETs.get ( i ).getText ().toString ();
                    if (!ingredientName.equals ( "" ) && !ingredientQty.equals ( "" ))
                        ingredients.put ( ingredientName, ingredientQty );
                }

                boolean AI = false;
                String author_id = mAuth.getCurrentUser ().getUid ();

                Long tsLong = System.currentTimeMillis ();
                Date date = new Date ( tsLong );


//                // Format the date as dd-mm-yyyy
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                String formattedDate = dateFormat.format(date);

                Long timestamplong = tsLong;

                String timestamp = timestamplong.toString ();

                if (title.equals("") || category.equals("") || complexity.equals("") || cooking_time.equals("") || servings.equals("") || description.equals("") ||  ingredients.isEmpty ())  {
                    Toast.makeText(getBaseContext(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                } else {


                    Recipe r = new Recipe ( title, search, category, complexity, cooking_time, servings, description, ingredients, recipeImage, AI, author_id, timestamp );
                recipeReference.push ().setValue ( r );
                    Toast.makeText(getBaseContext(), "You have successfully added a recipe.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent ( AddRecipeActivity.this, MainActivity.class );
                startActivity ( i );
                finish ();
            }
            }
        } );

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == 22 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData () != null) {
            this.filePath = data.getData ();
            uploadImage();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap ( getContentResolver (), filePath );
                imagePreview.setImageBitmap ( bitmap );
            } catch (IOException e) {
                e.printStackTrace ();
            }

        }
    }



    private void selectImage() {
        Intent i = new Intent ();
        i.setType ( "image/*" );
        i.setAction ( Intent.ACTION_GET_CONTENT );
        startActivityForResult (
                Intent.createChooser ( i, "Odaberite sliku" ), 22
        );
    }

    private void uploadImage() {
        if (this.filePath != null) {

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Učitavam sliku");
            progressDialog.show();
            StorageReference ref = (StorageReference) storageReference.child("images/"
                    + UUID.randomUUID().toString()
            );
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.hide();
                    Toast.makeText(
                            getApplicationContext(),
                            "Slika je učitana na server",
                            Toast.LENGTH_LONG).show();
                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            recipeImage = task.getResult().toString();
                        }
                    });
                }
            });
        }
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


                startActivity ( new Intent ( AddRecipeActivity.this, AddRecipeActivity.class ) );

            }
        } );

        airecipeLayout.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                dialog.dismiss ();
                Toast.makeText ( AddRecipeActivity.this, "Add new AIrecipe is clicked", Toast.LENGTH_SHORT ).show ();

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