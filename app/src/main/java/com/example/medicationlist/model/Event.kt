package com.example.medicationlist.model

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

data class Event(
    var datetime: String,
    val id: Int,
    val medication: String,
    val medicationtype: String,
    val uid: String
) : Comparable<Event> {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun compareTo(other: Event): Int {
        return getDateTime().compareTo(other.getDateTime());
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateTime(): Date {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(datetime)
    }
}