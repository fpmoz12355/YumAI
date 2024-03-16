package ba.fpmoz.yumai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import ba.fpmoz.yumai.R;
import ba.fpmoz.yumai.RecipeDetailsActivity;
import ba.fpmoz.yumai.model.Recipe;


public class RecipeAdapter extends FirebaseRecyclerAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {

    public static final String TAG = "RECIPE_ADAPTER";

    Context cxt;
    public RecipeAdapter(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position, @NonNull Recipe model) {
        String recipeId = getRef ( position ).getKey ();
        holder.titleRv.setText(model.getTitle());
        holder.categoryRv.setText(model.getCategory());
        holder.timeRv.setText(model.getCooking_time());
        Picasso
                .get()
                .load(model.getImage())
                .into(holder.imageRv);

        holder.titleRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(cxt, RecipeDetailsActivity.class);
                intent.putExtra("recipeId", recipeId);
                cxt.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.cxt = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view, parent, false);
        return new RecipeAdapter.RecipeViewHolder(view);
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView titleRv, categoryRv, timeRv;
        ImageView imageRv;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleRv = itemView.findViewById(R.id.titleRv);
            categoryRv = itemView.findViewById(R.id.categoryRv);
            timeRv = itemView.findViewById(R.id.timeRv);
            imageRv = itemView.findViewById(R.id.imageRv);
        }
    }


}