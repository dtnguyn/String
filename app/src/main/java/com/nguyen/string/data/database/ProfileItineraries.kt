package com.nguyen.string.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.nguyen.string.data.Blog
import com.nguyen.string.util.DataConverter

@Entity(tableName = "user_profile_itineraries")

data class ProfileItineraries (

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "itineraries")
    @TypeConverters(DataConverter::class)
    var itineraries: List<Blog>?

)