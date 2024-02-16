package com.td.demoactivityrecreatetransition

import android.graphics.Bitmap
import android.os.Parcelable

const val TRANSITION_DATA_KEY = "transition_data"

enum class TransitionType {
    /**
     * 进入
     */
    ENTER,

    /**
     * 退出
     */
    EXIT
}

/**
 * 重建过渡动画 data
 * 实现Parcelable接口，用于Activity重建时保存和恢复数据
 */
class TransitionData(
    val centerX: Float,
    val centerY: Float,
    val screenBitmap: Bitmap,
    val type: TransitionType,
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readParcelable(Bitmap::class.java.classLoader)!!,
        TransitionType.valueOf(parcel.readString()!!)
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeFloat(centerX)
        parcel.writeFloat(centerY)
        parcel.writeParcelable(screenBitmap, flags)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransitionData> {
        override fun createFromParcel(parcel: android.os.Parcel): TransitionData {
            return TransitionData(parcel)
        }

        override fun newArray(size: Int): Array<TransitionData?> {
            return arrayOfNulls(size)
        }
    }
}