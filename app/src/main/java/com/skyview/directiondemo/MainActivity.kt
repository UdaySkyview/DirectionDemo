package com.skyview.directiondemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener







class MainActivity : AppCompatActivity() {
    private lateinit var saveBtn :Button
    private lateinit var editMobile :EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        editMobile=findViewById(R.id.mobileNumber)
        saveBtn=findViewById(R.id.saveBtn)

        saveBtn.setOnClickListener {
            val number = editMobile.text.toString().trim()
            if (!TextUtils.isEmpty(number)) {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java).putExtra("number",number))
            }
            else{
                editMobile.setError("Please enter number",null)
            }
        }

        // Write a message to the database
        // Write a message to the database
        /*val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

        // Read from the database
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("response_vale", "Value is: $value")
                Toast.makeText(this@MainActivity,""+value,Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("response error", "Failed to read value.", error.toException())
            }
        })*/
    }
}