package com.treebricks.quiz

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.widget.Button

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(ResourcesCompat.getDrawable(resources,R.drawable.ic_close_white_24dp,null))
        val dev_webpage = findViewById(R.id.dev_webpage) as Button
        dev_webpage.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://treebricks.github.io/")))
        }
    }
}
