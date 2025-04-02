package com.ajaytanwar.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import kotlinx.coroutines.delay
import java.util.logging.Handler


class splash_screen : AppCompatActivity() {
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this , startactivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}

