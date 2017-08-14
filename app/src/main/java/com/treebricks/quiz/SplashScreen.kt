package com.treebricks.quiz

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            kotlin.run {
                val intent = Intent(this, MainActivity().javaClass)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }

    override fun onNewIntent(intent: Intent?) {
        this.intent = intent
    }
}
