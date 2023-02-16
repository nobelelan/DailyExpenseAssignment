package com.example.dailyexpense

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

object Utils {

    fun verifyDataFromUser(type: String, price: String): Boolean{
        return (type.isNotEmpty() && price.isNotEmpty())
    }
}