package com.example.medicationlist.network

import com.example.medicationlist.model.Data
import retrofit2.Call
import retrofit2.http.GET

interface SourceAPI {

    @GET("propeller_mobile_assessment_data.json")
    fun getAllData(): Call<Data>
}