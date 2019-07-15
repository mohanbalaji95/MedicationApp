package com.example.medicationlist

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.example.medicationlist.model.Data
import com.example.medicationlist.network.SourceAPI
import com.example.medicationlist.ui.DisplayListFragment
import com.example.medicationlist.ui.InsertListFragment
import com.example.medicationlist.viewModel.MedicationViewModel
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DisplayListFragment()).commit()
        navView.setCheckedItem(R.id.nav_list)

        val medicationViewModel = ViewModelProviders.of(this).get(MedicationViewModel::class.java)

        //retrofit call for accessing data from the webserver
        val retrofit = Retrofit.Builder()
            .baseUrl("https://s3-us-west-2.amazonaws.com/ph-svc-mobile-interview-jyzi2gyja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(SourceAPI::class.java)

        api.getAllData().enqueue(object : Callback<Data> {
            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.e("MainActivity", "Response failed")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                medicationViewModel.dataResource.postValue(response.body())
                val tempHmap = HashMap<String, String>()
                val tempMedicationList = response.body()!!.user.medications
                for (i in tempMedicationList) {
                    tempHmap.put(i.name, i.medicationtype)
                }
                val eventsList = response.body()!!.events
                var lastSeenId = 0
                var uid = ""
                for (i in eventsList) {
                    uid = i.uid
                    lastSeenId = i.id
                }
                medicationViewModel.medicationType.postValue(tempHmap)
                medicationViewModel.lastId.postValue(lastSeenId)
                medicationViewModel.uid.postValue(uid)
            }
        })

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_list -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DisplayListFragment())
                    .commit()
            }
            R.id.nav_insert -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, InsertListFragment())
                    .commit()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
