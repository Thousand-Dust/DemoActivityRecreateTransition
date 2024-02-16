package com.td.demoactivityrecreatetransition

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * 基础 Activity
 * 实现了加载本地配置的主题和语言
 * @author Thousand-Dust
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        // 加载本地配置的主题
        val theme = AppGlobals.getTheme()
        delegate.localNightMode = theme.mode

//        val config = newBase.resources.configuration
        // 加载本地配置的语言
//        val language = AppGlobals.getLanguage()
//        config.setLocale(language.locale)
//        val context = newBase.createConfigurationContext(config)
//        super.attachBaseContext(context)
        return super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Activity全屏显示，隐藏状态栏和导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }

}