package com.skyview.directiondemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var saveBtn: Button
    private lateinit var editMobile: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isFirstUser()){
            startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            finish()
        }
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun isFirstUser(): Boolean {
        val sharedPreferences = this.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        val userCreated = sharedPreferences.getBoolean("USER_CREATED",false)
        if (userCreated)return true
        return false
    }

    private fun initView() {
        editMobile = findViewById(R.id.mobileNumber)
        saveBtn = findViewById(R.id.saveBtn)
        saveBtn.setOnClickListener {
            val number = editMobile.text.toString().trim()
            if (!TextUtils.isEmpty(number)) {
                saveUsersKey()
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                finish()
            } else {
                editMobile.setError("Please enter your key", null)
            }
        }
    }

    private fun saveUsersKey() {
        val preferences = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
        preferences.edit().putString("UserKey",editMobile.text.toString()).apply()
    }
}