package com.nguyen.string.data

import com.google.gson.annotations.SerializedName
import com.nguyen.string.data.Photo

data class User (
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var facebookID: String? = null,
    var profilePhoto: String? = null,
    var gender: String? = null,
    var bio: String? = null,
    var website: String? = null,
    @SerializedName("checkfollow")
    var checkFollow: Boolean? = null,
    var currentLocation: String? =  null,
    var isSuperUser: Boolean? = null,
    var photos: List<Photo>? = null,
    var followingCounter: Int? = null,
    var postsCounter: Int? = null,
    var itineraryCounter: Int? = null,
    var followerCounter: Int? = null
)