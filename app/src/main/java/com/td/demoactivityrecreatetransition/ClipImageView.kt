package com.td.demoactivityrecreatetransition

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Region
import android.os.Build
import android.util.AttributeSet

/**
 * 可以裁切的ImageView
 * @author Thousand-Dust
 */
class ClipImageView : androidx.appcompat.widget.AppCompatImageView {

    /**
     * 裁切类型
     */
    enum class ClipType {
        /**
         * 圆形
         */
        CIRCLE,
        /**
         * 圆形（反向裁切）
         */
        CIRCLE_REVERSE,
    }

    /**
     * 裁切类型
     */
    private var clipType = ClipType.CIRCLE

    /**
     * 裁切区域
     */
    private var clipPath = Path()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 清空裁切
     */
    fun clearClip() {
        clipPath.reset()
        invalidate()
    }

    /**
     * 裁切圆形
     * @param centerX 圆心X
     * @param centerY 圆心Y
     * @param radius 半径
     * @param clipType 裁切类型
     */
    fun clipCircle(centerX: Float, centerY: Float, radius: Float, clipType: ClipType) {
        clipPath.reset()
        clipPath.addCircle(centerX, centerY, radius, Path.Direction.CW)
        this.clipType = clipType
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (!clipPath.isEmpty) {
            canvas.save()
            when (clipType) {
                ClipType.CIRCLE -> {
                    // 裁切圆形
                    canvas.clipPath(clipPath)
                }

                ClipType.CIRCLE_REVERSE -> {
                    // 反向裁切圆形
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        canvas.clipOutPath(clipPath)
                    } else {
                        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)
                    }
                }
            }
        }
        // 绘制图片
        super.onDraw(canvas)

        if (!clipPath.isEmpty) {
            canvas.restore()
        }
    }

}