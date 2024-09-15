package com.ganeevrm.android.photogallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganeevrm.android.photogallery.api.GalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryViewModel"

class PhotoGalleryViewModel : ViewModel() {
    private val photoRepository = PhotoRepository()

    private val _galleryIyems: MutableStateFlow<List<GalleryItem>> = MutableStateFlow(emptyList())
    val galleryItem: StateFlow<List<GalleryItem>>
        get() = _galleryIyems.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val items = fetchGalleryItems("planets")
                _galleryIyems.value = items
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch gallery items", ex)
            }
        }
    }

    fun setQuery(query: String){
        viewModelScope.launch { _galleryIyems.value = fetchGalleryItems(query) }
    }

    private suspend fun fetchGalleryItems(query: String): List<GalleryItem>{
        return if (query.isNotEmpty()){
            photoRepository.searchPhotos(query)
        } else {
            photoRepository.fetchPhotos()
        }
    }
}