package com.example.medicationlist.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.medicationlist.R
import com.example.medicationlist.model.Event
import kotlinx.android.synthetic.main.adapter_medication.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class EventItemAdapter(val list: List<Event>, val context: Context) :
    RecyclerView.Adapter<EventItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.adapter_medication,
                p0, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.eventId.text = list[position].id.toString()
        holder.medicationName.text = list[position].medication
        holder.medicationType.text = list[position].medicationtype
        if (!isValidFormat("MM-dd-yyyy", list[position].datetime, Locale.ENGLISH)) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH)
            val date = LocalDate.parse(list[position].datetime, inputFormatter)
            val formattedDate = outputFormatter.format(date)
            holder.timeStamp.text = formattedDate
        } else {
            holder.timeStamp.text = list[position].datetime
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isValidFormat(format: String, value: String, locale: Locale): Boolean {
        var localDateTime: LocalDateTime? = null
        val fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            localDateTime = LocalDateTime.parse(value, fomatter);
            val result = localDateTime.format(fomatter);
            return result.equals(value);
        } catch (e: DateTimeParseException) {
            try {
                val localDate = LocalDate.parse(value, fomatter);
                val result = localDate.format(fomatter);
                return result.equals(value);
            } catch (e1: DateTimeParseException) {
                try {
                    val localTime = LocalTime.parse(value, fomatter);
                    val result = localTime.format(fomatter);
                    return result.equals(value);
                } catch (e2: DateTimeParseException) {
                    //e2.printStackTrace();
                }
            }
        }
        return false;
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var eventId: TextView = view.tvEventId
        var medicationName: TextView = view.tvMedicationName
        var medicationType: TextView = view.tvMedicationType
        var timeStamp: TextView = view.tvTimeStamp
    }
}