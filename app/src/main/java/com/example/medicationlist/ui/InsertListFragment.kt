package com.example.medicationlist.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.medicationlist.R
import com.example.medicationlist.model.Data
import com.example.medicationlist.model.Event
import com.example.medicationlist.viewModel.MedicationViewModel
import kotlinx.android.synthetic.main.fragment_insert.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

class InsertListFragment : Fragment() {

    var medicationViewModel: MedicationViewModel? = null
    var data = HashMap<String, String>()
    var lastId = 0
    var uid = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_insert, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            medicationViewModel = ViewModelProviders.of(it).get(MedicationViewModel::class.java)
            medicationViewModel!!.medicationType.observe(this, Observer<Map<String, String>> {
                data = it as HashMap<String, String>
                val list = ArrayList(data.keys)
                val nameAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, list)
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spList.adapter = nameAdapter
            })

            medicationViewModel!!.lastId.observe(this, Observer<Int> {
                lastId = it
            })

            medicationViewModel!!.uid.observe(this, Observer<String> {
                uid = it
            })
        }

        btInsert.setOnClickListener {
            val medicName = spList.selectedItem.toString()
            val time = System.currentTimeMillis();
            val localDateTime =
                ISO_LOCAL_DATE_TIME.format(Instant.ofEpochMilli(time).atZone(ZoneOffset.UTC)).toString() + 'Z'
            val event = Event(localDateTime, lastId + 1, medicName, data.get(medicName)!!, uid)
            medicationViewModel!!.lastId.postValue(lastId + 1)
            medicationViewModel!!.dataResource.observe(this, Observer<Data> { innerData ->
                val tempEvent = innerData.events
                tempEvent.add(event)
                innerData.events = tempEvent
            })
            Toast.makeText(context, "New event is inserted", Toast.LENGTH_SHORT).show()
        }
    }
}