package com.example.dailyexpense.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Expense(
    var id: Int = 0,
    var type: String = "",
    var price: Int = 0,
    var date: String = ""
): Parcelable
