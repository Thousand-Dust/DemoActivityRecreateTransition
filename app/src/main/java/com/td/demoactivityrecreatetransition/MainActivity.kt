package com.td.demoactivityrecreatetransition

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.addListener
import androidx.core.view.drawToBitmap
import kotlin.math.hypot

class MainActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var ivTransition: ClipImageView

    private var recreateTransitionData: TransitionData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        // 重建过渡动画
        if (savedInstanceState != null)
            savedInstanceState.getParcelable<TransitionData>(TRANSITION_DATA_KEY)?.let {
                transitionAnimation(it)
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (recreateTransitionData != null) {
            // 保存重建过渡动画 data
            outState.putParcelable(TRANSITION_DATA_KEY, recreateTransitionData)
        }
    }

    /**
     * 使用过渡动画重建（recreate）Activity
     */
    private fun transitionRecreate(type: TransitionType) {
        // 获取切换主题menu的坐标（以menu的中心点为圆形揭露动画的中心点）
        val menuItemView = toolbar.menu.findItem(R.id.menu_theme_toggle).let {
            toolbar.findViewById<View>(it.itemId)
        }
        val location = IntArray(2)
        menuItemView.getLocationOnScreen(location)
        val centerX = location[0] + menuItemView.width / 2f
        val centerY = location[1] + menuItemView.height / 2f
        // Activity截图
        val screenBitmap = window.decorView.drawToBitmap()
        recreateTransitionData = TransitionData(centerX, centerY, screenBitmap, type)
        // 重建Activity
        recreate()
    }

    /**
     * 过渡动画
     */
    private fun transitionAnimation(transitionData: TransitionData) {
        ivTransition.visibility = View.VISIBLE
        ivTransition.setImageBitmap(transitionData.screenBitmap)

        ivTransition.post {
            val animator = ValueAnimator.ofFloat()
            var clipType = ClipImageView.ClipType.CIRCLE
            when (transitionData.type) {
                TransitionType.ENTER -> {
                    // 进入动画，裁切掉圆内的区域 圆由小变大
                    animator.setFloatValues(
                        0f,
                        hypot(ivTransition.width.toFloat(), ivTransition.height.toFloat())
                    )
                    clipType = ClipImageView.ClipType.CIRCLE_REVERSE
                }

                TransitionType.EXIT -> {
                    // 退出动画，裁切掉圆外的区域 圆由大变小
                    animator.setFloatValues(
                        hypot(
                            ivTransition.width.toFloat(),
                            ivTransition.height.toFloat()
                        ),
                        0f
                    )
                    clipType = ClipImageView.ClipType.CIRCLE
                }
            }
            animator.duration =
                resources.getInteger(android.R.integer.config_longAnimTime).toLong()
            animator.addListener(
                onEnd = {
                    // 动画结束后隐藏 ImageView
                    ivTransition.visibility = View.GONE
                }
            )
            animator.addUpdateListener {
                val radius = it.animatedValue as Float
                // 更新裁切区域
                ivTransition.clipCircle(
                    transitionData.centerX,
                    transitionData.centerY,
                    radius,
                    clipType
                )
            }
            animator.start()
        }
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.setOnMenuItemClickListener(this)
        ivTransition = findViewById(R.id.iv_transition)

        val localNightMode = delegate.localNightMode
        val menuThemeToggle = toolbar.menu.findItem(R.id.menu_theme_toggle)
        if (localNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menuThemeToggle.setIcon(R.drawable.daytime)
            menuThemeToggle.setTitle(R.string.daytime)
        } else {
            menuThemeToggle.setIcon(R.drawable.nighttime)
            menuThemeToggle.setTitle(R.string.nighttime)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_theme_toggle -> {
                val localNightMode = delegate.localNightMode
                val theme = if (localNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppTheme.LIGHT
                } else {
                    AppTheme.DARK
                }
                if (delegate.localNightMode == theme.mode) {
                    return true
                }
                AppGlobals.setTheme(theme)
                // 使用过渡动画重建Activity
                transitionRecreate(
                    // 根据当前主题设置过渡动画类型
                    if (theme == AppTheme.DARK)
                        TransitionType.ENTER
                    else
                        TransitionType.EXIT
                )
                return true
            }
        }
        return false
    }

}