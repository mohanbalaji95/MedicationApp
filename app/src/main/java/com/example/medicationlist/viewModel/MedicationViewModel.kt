package com.example.medicationlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicationlist.model.Data

class MedicationViewModel : ViewModel() {

    val dataResource = MutableLiveData<Data>()
    val medicationType = MutableLiveData<HashMap<String, String>>()
    val lastId = MutableLiveData<Int>()
    val uid = MutableLiveData<String>()

    fun getData(): LiveData<Data> {
        return dataResource
    }
}