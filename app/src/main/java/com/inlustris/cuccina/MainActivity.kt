package com.inlustris.cuccina

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.inlustris.cuccina.adapters.RecyclerAdapter
import com.inlustris.cuccina.beans.Recipe
import com.inlustris.cuccina.databinding.ActivityMainBinding
import com.inlustris.cuccina.model.ModelListener
import com.inlustris.cuccina.model.RecipesDB
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, ModelListener.RecipesListener {
    private var user = FirebaseAuth.getInstance().currentUser
    private var recyclerAdapter: RecyclerAdapter? = null
    var actbind: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val fontRequest = androidx.core.provider.FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs)
        val config = FontRequestEmojiCompatConfig(this, fontRequest)
        EmojiCompat.init(config)
        super.onCreate(savedInstanceState)
        actbind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setContentView(actbind!!.root)
        setSupportActionBar(actbind!!.maincontent.toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, actbind!!.maincontent.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener { item ->
            actbind!!.maincontent.title.text = item.title.toString()
            CarregarCategoria(item.title.toString())
            true
        }
        configureRecycler()
        checkUser()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setHomeButtonEnabled(true)
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_left_align)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val searchitem: MenuItem = menu!!.findItem(R.id.search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchitem.actionView as SearchView
        searchView.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Pesquise Receitas"
        searchView.setOnQueryTextListener(this)
        return true
    }


    private var gridLayoutManager: GridLayoutManager? = null

    private fun configureRecycler() {
        recyclerAdapter = RecyclerAdapter(this, null)
        gridLayoutManager = GridLayoutManager(this, 2, VERTICAL, false)
        recipes_recycler.adapter = recyclerAdapter
        recipes_recycler.layoutManager = gridLayoutManager
    }


    private fun checkUser() {
        if (user == null) {
            val builder = MaterialAlertDialogBuilder(this).setMessage("Faça login para ver o que tem para você comer hoje, mas se não quiser também não faz, quem ta perdendo é você.")
            builder.setPositiveButton("Ok") { dialogInterface, i ->
                val providers: List<IdpConfig> = listOf(
                        GoogleBuilder().build())
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setLogo(R.mipmap.ic_launcher)
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .build(), RC_SIGN_IN)
            }.show()
        } else {
            CarregarCategoria("")
            val header: View = nav_view.getHeaderView(0)
            val usertxt: TextView = header.findViewById(R.id.username)
            usertxt.text = user!!.displayName
        }
    }

    private fun CarregarCategoria(categoria: String?) {
        configureRecycler()
        if (categoria != "Sair") {
            val recipesDB = RecipesDB(this)
            recipesDB.carregar(this, categoria)
        } else {
            FirebaseAuth.getInstance().signOut()
            Snacky.builder().setActivity(this).info().setText("Você saiu de sua conta").show()
        }
    }

    private fun updaterecycler(recipes: ArrayList<Recipe>) {

        if (recyclerAdapter != null) {
            if (recipes.isNotEmpty()) {
                recyclerAdapter!!.recipes = recipes
                recyclerAdapter!!.notifyDataSetChanged()
                val onSpanSizeLookup: GridLayoutManager.SpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (!recipes[position].isLastRecipe()) 1
                        else 2
                    }
                }
                gridLayoutManager?.spanSizeLookup = onSpanSizeLookup
            }
        } else {
            configureRecycler()
            updaterecycler(recipes)
        }
        actbind!!.maincontent.recipesRecycler.visibility = if (recipes.isNotEmpty()) VISIBLE else GONE
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun Pesquisar(pesquisa: String) {

        RecipesDB(this).carregar(object : ModelListener.RecipesListener {
            override fun recipesLoaded(recipes: ArrayList<Recipe>) {
                val filteredlist = recipes.filter { recipe -> recipe.prato!!.contains(pesquisa) }
                updaterecycler(filteredlist as ArrayList<Recipe>)
            }
        }, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode != Activity.RESULT_OK) {
                if (response != null) {
                    Snacky.builder().setActivity(this).error().setText("Ocorreu um erro(${response.error!!.localizedMessage}) ao realizar o login gostaria de tentar novamente?")
                            .setAction("Ok") { checkUser() }
                            .show()
                } else {
                    checkUser()
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrBlank()) {
            CarregarCategoria("")
        } else {
            Pesquisar(newText)
        }
        return true
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun recipesLoaded(recipes: ArrayList<Recipe>) {
        updaterecycler(recipes)
    }
}