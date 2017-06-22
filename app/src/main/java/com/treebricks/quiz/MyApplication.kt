package com.treebricks.quiz

import android.app.Application
import io.realm.Realm

/**
 * Created by fahim on 6/16/17.
 */
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}