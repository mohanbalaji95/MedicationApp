package com.example.medicationlist.model

data class User(
    val address1: String,
    val address2: String,
    val disease: String,
    val dob: String,
    val medications: List<Medication>,
    val name: String,
    val sex: String,
    val uid: String
)