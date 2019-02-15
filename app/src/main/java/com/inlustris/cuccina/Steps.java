package com.inlustris.cuccina;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inlustris.cuccina.Adapters.RecyclerStepsAdapter;
import com.inlustris.cuccina.Beans.Ingredient;
import com.inlustris.cuccina.Beans.Step;

import java.util.ArrayList;

public class Steps extends AppCompatActivity {

    private android.support.v7.widget.RecyclerView steps;
    private android.support.design.widget.FloatingActionButton recipecomplete;
    String id;
    ArrayList<Step> stepArrayList;
    ArrayList<Ingredient> ingredientArrayList;
    Activity activity =this;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
       id = this.getIntent().getStringExtra("id");
       title = this.getIntent().getStringExtra("prato");
        this.recipecomplete = findViewById(R.id.recipecomplete);
        this.steps = findViewById(R.id.steps);
        carregarpassos();
    }
    private void CarregarIngredientes( ){
        ingredientArrayList = new ArrayList<>();
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        firebase.child("recipes").child(id).child("ingredientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Ingredient ingredient = new Ingredient();
                    Ingredient i = d.getValue(Ingredient.class);
                    if (i != null){
                        System.out.println(i.getIngrediente());
                        System.out.println(i.getQuantidade());
                        ingredient.setCount(d.getKey());
                        ingredient.setIngrediente(i.getIngrediente());
                        ingredient.setQuantidade(i.getQuantidade());
                        ingredient.setMedidas(i.getMedidas());
                        if (ingredient.getQuantidade().equals("0.5")){ingredient.setQuantidade("1/2");}
                        if(!ingredient.getQuantidade().equals("1/2")) {

                            int q = Integer.parseInt(ingredient.getQuantidade());
                            if (q < 2) {
                                ingredient.setMedidas(i.getMedidas().replaceAll("s", ""));
                            } else {
                                if (i.getMedidas().equals("Unidade")){
                                    ingredient.setMedidas("");
                                    ingredient.setIngrediente(i.getIngrediente() + "s");
                                }else{
                                    ingredient.setIngrediente(i.getIngrediente() + "s");

                                }
                            }
                        }
                        if(ingredient.getMedidas().equals("Unidade")){ingredient.setMedidas("");}
                        ingredientArrayList.add(ingredient);

                        System.out.println(" ingredientes " + ingredientArrayList.size());
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void carregarpassos(){
            DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
            firebase.child("recipes").child(id).child("passos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    stepArrayList = new ArrayList<>();
                    for (DataSnapshot d: dataSnapshot.getChildren()){
                        Step step = new Step();
                        Step s = d.getValue(Step.class);
                        if (s != null){
                            step.setCount(d.getKey());
                            step.setPasso(s.getPasso());
                        }
                        stepArrayList.add(step);
                    }
                    GridLayoutManager llm = new GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false);
                    steps.setHasFixedSize(true);
                    RecyclerStepsAdapter myadapter = new RecyclerStepsAdapter(activity, stepArrayList, activity,steps);
                    myadapter.notifyDataSetChanged();
                    steps.setAdapter(myadapter);
                    steps.setLayoutManager(llm);
                    getSupportActionBar().setTitle(title);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });









    }
}
