package com.td.demoactivityrecreatetransition

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

object AppGlobals {

    const val THEME_KEY = "theme"

    lateinit var appContext: Context
        private set

    private lateinit var appConfigSP: SharedPreferences

    /**
     * app创建时调用初始化
     */
    fun init(appContext: Context) {
        this.appContext = appContext
        appConfigSP = this.appContext.getSharedPreferences("AppConfig", Context.MODE_PRIVATE)
    }

    /**
     * 获取主题配置
     */
    fun getTheme(): AppTheme {
        val name = appConfigSP.getString(THEME_KEY, AppTheme.AUTO.name)!!
        return AppTheme.valueOf(name)
    }

    /**
     * 写入主题配置
     */
    fun setTheme(theme: AppTheme) {
        if (theme == AppTheme.AUTO) {
            // delete theme
            appConfigSP.edit().remove(THEME_KEY).apply()
            return
        }
        appConfigSP.edit().putString(THEME_KEY, theme.name).apply()
    }

}

/**
 * 支持的主题
 */
enum class AppTheme(val mode: Int) {
    AUTO(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    DARK(AppCompatDelegate.MODE_NIGHT_YES);

    companion object {
        fun byMode(mode: Int): AppTheme {
            return values().firstOrNull { it.mode == mode } ?: AUTO
        }
    }
}