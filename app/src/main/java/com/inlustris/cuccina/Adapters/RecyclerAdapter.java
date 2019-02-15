package com.inlustris.cuccina.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inlustris.cuccina.Beans.Recipe;
import com.inlustris.cuccina.R;
import com.inlustris.cuccina.Startrecipe;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Recipe> mData;
    private Activity mActivity;
    private RecyclerView view;

    public RecyclerAdapter(Context mContext, ArrayList<Recipe> mData, Activity mActivity, RecyclerView view) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
        this.view = view;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.recipe_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Recipe recipe = mData.get(position);
        Glide.with(mContext).load(recipe.getImageurl()).into(holder.pic);
        holder.recipe.setText(recipe.getPrato());
        holder.time.setText(recipe.getTempo());
        holder.calories.setText("Aproximadamente " +  recipe.getCalorias() + "/kcal");
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        firebase.child("recipes").child(recipe.getId()).child("ingredientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.ingredients.setText(dataSnapshot.getChildrenCount() + " ingredientes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.startrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity,Startrecipe.class);
                intent.putExtra("id",recipe.getId());
                intent.putExtra("prato",recipe.getPrato());
                intent.putExtra("picurl",recipe.getImageurl());
                intent.putExtra("tipo",recipe.getTipo());
                intent.putExtra("tempo",recipe.getTempo());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView recipe,ingredients,calories,time;
        private Button startrecipe;
        public MyViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            recipe = itemView.findViewById(R.id.receita);
            time = itemView.findViewById(R.id.tempo);
            calories = itemView.findViewById(R.id.calories);
            ingredients = itemView.findViewById(R.id.ingredientscount);
            startrecipe = itemView.findViewById(R.id.startrecipe);
        }
    }
}
