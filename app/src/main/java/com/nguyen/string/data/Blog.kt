package com.nguyen.string.data

import com.google.gson.annotations.SerializedName

data class Blog (
    var id: Int? = null,
    var type: String? = null,
    var title: String? = null,
    var description: String? = null,
    var photos: List<Photo>? = null,
    var videos: Video? = null,
    var likeCounter: Int? = null,
    var commentCounter: Int? = null,
    @SerializedName("created_at")
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var walkthrough: Int? = null,
    var user: User? = null,
    var isLiked: Boolean? = null,
    var saveCounter: Int? = null,
    var place: Place? = null,
    var isSaved: Boolean? = null,
    var tags: List<Tag>? = null,
    var copyCounter: Int? = null,
    var itineraries: List<Itinerary>? = null,
    var websiteUrl: String? = null,
    @SerializedName("CountItinerarySections")
    var countItinerarySections: Int? = null,
    @SerializedName("CountItineraryPlaces")
    var countItineraryPlaces: Int? = null,
    var isPrivate: Boolean? = null
)