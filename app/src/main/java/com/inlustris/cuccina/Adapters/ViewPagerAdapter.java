package com.inlustris.cuccina.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inlustris.cuccina.Beans.Ingredient;
import com.inlustris.cuccina.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<String> ingredientes;
    ArrayAdapter<String> adapter;
    int stepcount;
    String picurl,name,tipo,tempo;

    public ViewPagerAdapter(Activity activity) {
        this.activity = activity;
         picurl = activity.getIntent().getStringExtra("picurl");
         name = activity.getIntent().getStringExtra("prato");
         tipo = activity.getIntent().getStringExtra("tipo");
         tempo = activity.getIntent().getStringExtra("tempo");
         contarpassos();
    }


    private void CarregarIngredientes(final ListView lista){
        ingredientes = new ArrayList<>();
        String id = activity.getIntent().getStringExtra("id");
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
                        ingredientes.add(ingredient.getQuantidade() + " " +ingredient.getMedidas() +" " + ingredient.getIngrediente());

                        System.out.println(" ingredientes " + ingredientes.size());
                    }
                }
                adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_activated_1, ingredientes);
                lista.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void contarpassos(){
        String id = activity.getIntent().getStringExtra("id");
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        firebase.child("recipes").child(id).child("passos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stepcount = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = layoutInflater.inflate(R.layout.pagerlayout, container, false);
        LinearLayout back = view.findViewById(R.id.background);
        TextView title = view.findViewById(R.id.title);
        TextView data = view.findViewById(R.id.data);
        ListView lista = view.findViewById(R.id.list);
        ImageView pic = view.findViewById(R.id.foodpick);
        LinearLayout foodinfo = view.findViewById(R.id.foodinfo);
        back(back);

        if (position == 0){
            Glide.with(activity).load(picurl).into(pic);
            title.setText("Prepare-se!");
            data.setText(name);
            lista.setVisibility(View.GONE);
        }else if(position == 1){
            title.setText("Você vai precisar desses ingredientes");
            CarregarIngredientes(lista);
            foodinfo.setVisibility(View.GONE);
            lista.setVisibility(View.VISIBLE);
        }else if(position == 2){
            if (stepcount == 0){contarpassos();}
            title.setVisibility(View.GONE);
            foodinfo.setVisibility(View.VISIBLE);
            lista.setVisibility(View.GONE);
            pic.setVisibility(View.GONE);
            data.setText("Esta receita tem " + stepcount + " passos!" + " se deus quiser você conclui em " + tempo);
        }

































        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)(object));

    }

    private void back(LinearLayout back) {
        if (tipo != null) {
            switch (tipo) {
                case "Bebidas":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_400));
                    break;
                case "Massas":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_yellow_400));
                    break;
                case "Frutos do mar":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_900));
                    break;
                case "Proteínas":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_red_400));
                    break;
                case "Confeitaria":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_purple_400));
                    break;
                case "Legumes, vegetais e cia.":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_green_400));
                    break;
                case "Doces":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_red_A200));
                    break;
                case "Saladas":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_light_green_500));
                    break;
                case "Cremes e sopas":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_yellow_200));
                    break;
                case "Molhos":
                    back.setBackgroundColor(activity.getResources().getColor(R.color.md_red_A400));
                    break;
            }
        }
    }
}
