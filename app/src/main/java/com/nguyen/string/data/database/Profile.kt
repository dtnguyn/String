package com.nguyen.string.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")

data class Profile(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "username")
    var username: String?,

    @ColumnInfo(name = "bio")
    var bio: String?,

    @ColumnInfo(name = "profile_photo")
    var profilePhoto: String?,

    @ColumnInfo(name = "post_count")
    var postCount: Int?,

    @ColumnInfo(name = "itinerary_count")
    var itineraryCount: Int?,

    @ColumnInfo(name = "following_count")
    var followingCount: Int?,

    @ColumnInfo(name = "follower_count")
    var followerCount: Int?



)