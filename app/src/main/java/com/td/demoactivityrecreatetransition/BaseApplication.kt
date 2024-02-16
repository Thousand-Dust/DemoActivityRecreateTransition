package com.td.demoactivityrecreatetransition

import android.app.Application

/**
 * @author Thousand-Dust
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppGlobals.init(this)
    }

}