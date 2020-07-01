package com.nguyen.string.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setProfile(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setProfilePosts(profilePosts: ProfilePosts)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setProfileItineraries(profileItineraries: ProfileItineraries)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setProfileSavedPosts(profileSavedPosts: ProfileSavedPosts)




    @Query("SELECT * FROM user_profile WHERE id = :id")
    suspend fun getProfile(id: Int) : Profile?

    @Query("SELECT * FROM user_profile_posts WHERE id = :id")
    suspend fun getProfilePosts(id: Int) : ProfilePosts

    @Query("SELECT * FROM user_profile_itineraries WHERE id = :id")
    suspend fun getProfileItineraries(id: Int) : ProfileItineraries

    @Query("SELECT * FROM user_profile_saved_posts WHERE id = :id")
    suspend fun getProfileSavedPosts(id: Int) : ProfileSavedPosts

}