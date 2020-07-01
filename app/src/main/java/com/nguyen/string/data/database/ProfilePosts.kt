package com.nguyen.string.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.nguyen.string.data.Blog
import com.nguyen.string.util.DataConverter

@Entity(tableName = "user_profile_posts")

data class ProfilePosts (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "posts")
    @TypeConverters(DataConverter::class)
    var posts: List<Blog>?
)