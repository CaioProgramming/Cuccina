package com.inlustris.cuccina.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inlustris.cuccina.Beans.Step;
import com.inlustris.cuccina.R;

import java.util.ArrayList;

public class RecyclerStepsAdapter extends RecyclerView.Adapter<RecyclerStepsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Step> mData;
    private Activity mActivity;
    private RecyclerView view;

    public RecyclerStepsAdapter(Context mContext, ArrayList<Step> mData, Activity mActivity, RecyclerView view) {
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
        view = mInflater.inflate(R.layout.step_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Step step = mData.get(position);
        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.fui_slide_in_right);
        holder.card.startAnimation(animation);
        holder.count.setText(String.valueOf(Integer.parseInt(step.getCount()) + 1));
        holder.steptxt.setText(step.getPasso());
        ViewTreeObserver vto = holder.steptxt.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams params = holder.line.getLayoutParams();
                params.height = holder.steptxt.getHeight();
                holder.line.setLayoutParams(params);
                holder.steptxt.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        if(position == mData.size()){
            holder.line.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView steptxt,count;
        private LinearLayout line;
        private RelativeLayout card;

        public MyViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            steptxt = itemView.findViewById(R.id.steptxt);
            count = itemView.findViewById(R.id.counter);
            line = itemView.findViewById(R.id.line);

        }
    }
}
