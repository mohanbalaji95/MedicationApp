package com.example.medicationlist.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicationlist.adapters.EventItemAdapter
import com.example.medicationlist.model.Data
import com.example.medicationlist.viewModel.MedicationViewModel
import kotlinx.android.synthetic.main.fragment_display.*
import java.util.*
import androidx.recyclerview.widget.DividerItemDecoration
import android.graphics.drawable.ClipDrawable.HORIZONTAL


class DisplayListFragment : Fragment() {

    var medicationViewModel: MedicationViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.example.medicationlist.R.layout.fragment_display, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            medicationViewModel = ViewModelProviders.of(it).get(MedicationViewModel::class.java)
            medicationViewModel!!.getData().observe(this, Observer<Data> {
                displayEventData(it)
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun displayEventData(data: Data) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            Collections.sort(data.events)
            Collections.reverse(data.events)
            adapter = EventItemAdapter(data.events, context)
        }
        val itemDecor = DividerItemDecoration(getContext(), HORIZONTAL)
        recyclerView.addItemDecoration(itemDecor)
    }
}