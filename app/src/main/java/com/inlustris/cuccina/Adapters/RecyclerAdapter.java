package com.inlustris.cuccina.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inlustris.cuccina.Beans.Recipe;
import com.inlustris.cuccina.R;
import com.inlustris.cuccina.Startrecipe;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.pop_in);
        holder.pic.startAnimation(animation);
        holder.recipe.setText(recipe.getPrato());
        holder.tempo.setText(recipe.getTempo());
        holder.calorias.setText(recipe.getCalorias()+ "/kcal por 100/g");
        holder.startrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartRecipe(recipe);
            }
        });
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartRecipe(recipe);
            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartRecipe(recipe);
            }
        });
    }

    private void StartRecipe(Recipe recipe) {
        Intent intent = new Intent(mActivity,Startrecipe.class);
        intent.putExtra("id",recipe.getId());
        intent.putExtra("prato",recipe.getPrato());
        intent.putExtra("picurl",recipe.getImageurl());
        intent.putExtra("tipo",recipe.getTipo());
        intent.putExtra("tempo",recipe.getTempo());
        mActivity.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout card;
        private ImageView pic;
        private TextView recipe,calorias,tempo;
        private Button startrecipe;
        MyViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            tempo = itemView.findViewById(R.id.tempo);
            calorias = itemView.findViewById(R.id.calorias);
            pic = itemView.findViewById(R.id.pic);
            recipe = itemView.findViewById(R.id.receita);
            startrecipe = itemView.findViewById(R.id.startrecipe);
        }
    }
}
