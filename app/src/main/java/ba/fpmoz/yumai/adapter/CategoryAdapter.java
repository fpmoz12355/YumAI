package ba.fpmoz.yumai.adapter;

import static ba.fpmoz.yumai.R.id.imageRv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import ba.fpmoz.yumai.R;
import ba.fpmoz.yumai.RecipeDetailsActivity;
import ba.fpmoz.yumai.SingleCategoryActivity;
import ba.fpmoz.yumai.model.Category;


public class CategoryAdapter extends FirebaseRecyclerAdapter<Category, CategoryAdapter.CategoryViewHolder> {

    public static final String TAG = "CATEGORY_ADAPTER";

    Context cxt;
    String title;

    public CategoryAdapter(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position, @NonNull Category model) {
//        String categoryId = getRef ( position ).getKey ();
        holder.titleRv.setText(model.getTitle());
        title = model.getTitle ();
        holder.descriptionRv.setText(model.getDescription ());
        Picasso
                .get()
                .load(model.getImage())
                .into(holder.imageRv);

        holder.titleRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Category currentModel = getItem(currentPosition); // Ensure you have a method to get the item by position

                    Intent intent = new Intent(cxt, SingleCategoryActivity.class);
                    intent.putExtra("categoryTitle", currentModel.getTitle());
                    cxt.startActivity(intent);
            }
            }
        });
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.cxt = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView titleRv, descriptionRv;
        ImageView imageRv;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleRv = itemView.findViewById(R.id.titleRv);
            descriptionRv = itemView.findViewById(R.id.descriptionRv);
            imageRv = itemView.findViewById( R.id.imageRv );
        }
    }


}