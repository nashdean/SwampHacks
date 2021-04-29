package com.masterclass.quizapp

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(val name: String?, var score: Int) : Parcelable