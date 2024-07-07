package com.ganeevrm.android.photogallery.api

import retrofit2.http.GET

private const val API_KEY = "dbdfa900fc37ebec90be16931800f971"
interface FlickrApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=$API_KEY" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    suspend fun fetchPhotos(): FlickrResponse
}