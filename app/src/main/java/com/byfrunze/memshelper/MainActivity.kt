package com.byfrunze.memshelper

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.byfrunze.memshelper.data.ModelSaveQuotes
import com.byfrunze.memshelper.data.RealmController
import com.byfrunze.memshelper.data.RealmDB
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), RealmController {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var quoteRu: String = ""
    var quoteEn: String = ""
    var author: String = ""
    var from: String = ""
    val realm: Realm = RealmDB.initDB()

    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_QUOTE = "quote"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = StrictMode.VmPolicy.Builder()

        val sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(APP_PREFERENCES_QUOTE, "rb_advice")
        editor.apply()

        StrictMode.setVmPolicy(builder.build())
        setContentView(R.layout.activity_main)
        toolbar.setTitleTextColor(Color.BLACK)
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
                R.id.nav_history,
                R.id.nav_create_mem,
                R.id.nav_settings_widget
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_history
                || destination.id == R.id.nav_create_mem
            )
                fab.visibility = View.GONE
            else fab.visibility = View.VISIBLE
        }
        fab.setOnClickListener {
            saveQuote()
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
        }

        if (!isConnected()) {
            MaterialDialog(this).show {
                title(R.string.error)
                message(text = "Problem with network")
                icon(R.drawable.ic_uncheck)
            }
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

    private fun isConnected(): Boolean {
        var result = false
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    private fun saveQuote() {
        val PrKey: Long = realm.where<ModelSaveQuotes>().count() + 1
        realm.beginTransaction()
        val quotes = realm.createObject<ModelSaveQuotes>()
        quotes.id = PrKey
        quotes.from = from
        quotes.quoteEn = quoteEn
        quotes.quoteRu = quoteRu
        quotes.author = author
        realm.commitTransaction()
    }
}
