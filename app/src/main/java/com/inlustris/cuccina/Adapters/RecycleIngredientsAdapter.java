package com.inlustris.cuccina.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inlustris.cuccina.Beans.Ingredient;
import com.inlustris.cuccina.R;

import java.util.ArrayList;

public class RecycleIngredientsAdapter extends RecyclerView.Adapter<RecycleIngredientsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Ingredient> mData;
    private Activity mActivity;
    private RecyclerView view;

    public RecycleIngredientsAdapter(Context mContext, ArrayList<Ingredient> mData, Activity mActivity, RecyclerView view) {
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
        view = mInflater.inflate(R.layout.ingredient_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         Ingredient ingredient = mData.get(position);

         holder.ingredientxt.setText(ingredient.getIngrediente());
        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.fui_slide_in_right);
        holder.card.startAnimation(animation);
        System.out.println(ingredient);
         holder.ingredientinfo.setText(ingredient.getQuantidade() + " " +  ingredient.getMedidas());


    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientxt,ingredientinfo;
        private LinearLayout line;
        RelativeLayout card;

        public MyViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            ingredientxt = itemView.findViewById(R.id.ingrediente);
            ingredientinfo = itemView.findViewById(R.id.ingredienteinfo);

        }
    }
}
