package com.byfrunze.memshelper

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.byfrunze.memshelper.data.ModelSaveQuotes
import com.byfrunze.memshelper.data.RealmController
import com.byfrunze.memshelper.data.RealmDB
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), RealmController {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var quoteRu: String = ""
    var quoteEn: String = ""
    var author: String = ""
    var from: String = ""
    val realm: Realm = RealmDB.initDB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_motivation,
                R.id.nav_chack,
                R.id.nav_advice,
                R.id.nav_programming,
                R.id.nav_history
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_history)
                fab.visibility = View.GONE
            else fab.visibility = View.VISIBLE
        }
        fab.setOnClickListener {
            saveQuote()
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun transQuote(from: String, quoteEn: String, quoteRu: String, author: String) {
        this.from = from
        this.quoteRu = quoteRu
        this.quoteEn = quoteEn
        this.author = author

    }

    private fun saveQuote() {
        var PrKey: Long = realm.where<ModelSaveQuotes>().count()
        realm.beginTransaction()
        val quotes = realm.createObject<ModelSaveQuotes>(PrKey)
        quotes.from = from
        quotes.quoteEn = quoteEn
        quotes.quoteRu = quoteRu
        quotes.author = author
        realm.commitTransaction()
    }
}
