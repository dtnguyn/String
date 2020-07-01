package com.nguyen.string.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyen.string.data.Blog
import java.lang.reflect.Type


class DataConverter {

    @TypeConverter
    fun fromBlogList(blogs: List<Blog?>?): String? {
        if (blogs == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Blog?>?>() {}.type
        return gson.toJson(blogs, type)
    }

    @TypeConverter
    fun toBlogList(blogsSting: String?): List<Blog>? {
        if (blogsSting == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Blog?>?>() {}.type
        return gson.fromJson<List<Blog>>(blogsSting, type)
    }


}