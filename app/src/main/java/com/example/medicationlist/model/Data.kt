package com.example.medicationlist.model

data class Data(
    var events: MutableList<Event>,
    val user: User
)