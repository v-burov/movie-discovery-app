package com.burov.movie.discovery.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
data class PersonEntity(
    @PrimaryKey
    val id: String,
)