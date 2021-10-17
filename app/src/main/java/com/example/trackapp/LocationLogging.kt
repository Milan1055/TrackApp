package com.example.trackapp

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class LocationLogging(
    var Latitude: Double? = 0.0,
    var Longitude: Double? = 0.0
)