package com.inlustris.cuccina;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inlustris.cuccina.Adapters.RecyclerAdapter;
import com.inlustris.cuccina.Beans.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.mateware.snacky.Snacky;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    FirebaseUser user;
    RecyclerAdapter myadapter;
    private DrawerLayout drawerlayout;
    private android.support.v7.widget.RecyclerView recipes;
    private android.support.v7.widget.Toolbar toolbar;
    private static final int RC_SIGN_IN = 123;
    private Query recipesdb;
    private Activity activity = this;
    TextView usertxt;
    private ArrayList<Recipe> recipeArrayList;
    private NavigationView navview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.toolbar = findViewById(R.id.toolbar);
        android.widget.ImageView nomorerecipes = findViewById(R.id.nomorerecipes);
        this.recipes = findViewById(R.id.recipes);
         setSupportActionBar(toolbar);

        recipesdb = FirebaseDatabase.getInstance().getReference();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
         usertxt = header.findViewById(R.id.usuarios);
        Typeface font = Typeface.createFromAsset(this.getAssets(),"fonts/GrandHotel-Regular.ttf");
        usertxt.setTypeface(font);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                    Carregar();
                    // Handle the camera action
//                } else if (id == R.id.nav_favorite) {
//                    getSupportActionBar().setTitle("Favoritos");
                } else if (id == R.id.nav_bakery) {
                    getSupportActionBar().setTitle("Confeitaria");
                    CarregarCategoria("Confeitaria");

                } else if (id == R.id.nav_soup) {
                    getSupportActionBar().setTitle("Cremes e sopas");
                    CarregarCategoria("Cremes e sopas");
                } else if (id == R.id.nav_candy) {
                    getSupportActionBar().setTitle("Doces");
                    CarregarCategoria("Doces");

                } else if (id == R.id.nav_sea) {
                    getSupportActionBar().setTitle("Frutos do mar");
                    CarregarCategoria("Frutos do mar");

                } else if(id == R.id.nav_protein){
                    getSupportActionBar().setTitle("Proteínas");
                    CarregarCategoria("Proteínas");

                }else if(id == R.id.nav_saladas){
                    getSupportActionBar().setTitle("Saladas");
                    CarregarCategoria("Saladas");
                }else if(id == R.id.nav_lettuce){
                    getSupportActionBar().setTitle("Legumes,vegetais e cia.");
                    CarregarCategoria("Legumes,vegetais e cia.");

                }else if(id == R.id.nav_drinks){
                    getSupportActionBar().setTitle("Bebidas");
                    CarregarCategoria("Bebidas");
                }

                DrawerLayout drawer =  findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ChecKUser();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            Snacky.builder().setActivity(this).info().setText("Você saiu de sua conta").show();
            recipes.removeAllViews();
            Carregar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void Carregar(){
        recipesdb = FirebaseDatabase.getInstance().getReference().child("recipes");
         user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){recipesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (recipeArrayList != null){
                    recipeArrayList.clear();
                    recipes.removeAllViews();
                }else{
                recipeArrayList = new ArrayList<>();}
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Recipe recipe = new Recipe();
                    Recipe r = d.getValue(Recipe.class);
                    if (r != null){
                        System.out.println(r.getPrato());
                        recipe.setPrato(r.getPrato());
                        recipe.setImageurl(r.getImageurl());
                        recipe.setCalorias(r.getCalorias());
                        recipe.setTempo(r.getTempo());
                        recipe.setId(d.getKey());
                        recipe.setTipo(r.getTipo());
                        recipeArrayList.add(recipe);
                        System.out.println(recipeArrayList.size());
                    }
                }
                Collections.shuffle(recipeArrayList);
                GridLayoutManager llm = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);

                myadapter = new RecyclerAdapter(activity, recipeArrayList, activity,recipes);
                myadapter.notifyDataSetChanged();
                recipes.setAdapter(myadapter);
                recipes.setLayoutManager(llm);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
    }

    private void ChecKUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("Faça login para ver o que tem para você comer hoje, mas se não quiser também não faz, quem ta perdendo é você.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    List<AuthUI.IdpConfig> providers = Collections.singletonList(
                            new AuthUI.IdpConfig.GoogleBuilder().build());
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                            .setLogo(R.mipmap.ic_launcher)
                            .setAvailableProviders(providers)
                            .setTheme(R.style.AppTheme)
                            .build(), RC_SIGN_IN);
                }
            }).show();

        }else{
            Carregar();
            usertxt.setText(user.getDisplayName());
        }
    }


    private void CarregarCategoria(final String categoria){
        recipesdb = FirebaseDatabase.getInstance().getReference().child("recipes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){recipesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (recipeArrayList != null){
                    recipeArrayList.clear();
                }else{
                recipeArrayList = new ArrayList<>();}
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Recipe recipe = new Recipe();
                    Recipe r = d.getValue(Recipe.class);
                    if (r != null){
                        System.out.println(r.getPrato());
                        recipe.setPrato(r.getPrato());
                        recipe.setImageurl(r.getImageurl());
                        recipe.setCalorias(r.getCalorias());
                        recipe.setTempo(r.getTempo());
                        recipe.setId(d.getKey());
                        recipe.setTipo(r.getTipo());
                        System.out.println(r.getTipo());
                        if (r.getTipo().equals(categoria)){
                        recipeArrayList.add(recipe);}
                        System.out.println(" receitas " + recipeArrayList.size());
                    }
                }
                Collections.shuffle(recipeArrayList);
                GridLayoutManager llm = new GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false);
                recipes.setHasFixedSize(true);
                RecyclerAdapter myadapter = new RecyclerAdapter(activity, recipeArrayList, activity,recipes);
                myadapter.notifyDataSetChanged();
                recipes.setAdapter(myadapter);
                recipes.setLayoutManager(llm);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snacky.builder().setActivity(activity).error().setText(databaseError.getMessage() + ": " + databaseError.getDetails() + " " + databaseError.getCode()).show();
            }
        });
        }else{
            ChecKUser();
        }




    }

    private void Pesquisar(String pesquisa){
        recipesdb =  FirebaseDatabase.getInstance().getReference().child("recipes").orderByChild("prato").startAt(pesquisa).endAt(pesquisa +  "\uf8ff");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){recipesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (recipeArrayList != null){
                    recipeArrayList.clear();
                    recipes.removeAllViews();
                }else{
                    recipeArrayList = new ArrayList<>();}
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Recipe recipe = new Recipe();
                    Recipe r = d.getValue(Recipe.class);
                    if (r != null){
                        System.out.println(r.getPrato());
                        recipe.setPrato(r.getPrato());
                        recipe.setImageurl(r.getImageurl());
                        recipe.setCalorias(r.getCalorias());
                        recipe.setTempo(r.getTempo());
                        recipe.setId(d.getKey());
                        recipe.setTipo(r.getTipo());
                        recipeArrayList.add(recipe);
                        System.out.println("Search result " + recipeArrayList.size());
                    }
                }
                Collections.shuffle(recipeArrayList);
                GridLayoutManager llm = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                recipes.setHasFixedSize(true);
                myadapter = new RecyclerAdapter(activity, recipeArrayList, activity,recipes);
                myadapter.notifyDataSetChanged();
                recipes.setAdapter(myadapter);
                recipes.setLayoutManager(llm);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }else{
            ChecKUser();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN){

            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode != RESULT_OK) {
                if (response != null) {
                    Snacky.builder().setActivity(this).error().setText("Erro " + Objects.requireNonNull(response.getError()).getMessage() + " causa " + response.getError().getCause()).show();
                }else {
                    Carregar();
                    usertxt.setText(user.getDisplayName());
                }

            }

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
            Pesquisar(newText);
            if (newText.isEmpty()){Carregar();}
            return true;

    }
}
