package com.ganeevrm.android.photogallery.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlickrResponse(val photos: PhotoResponse)
