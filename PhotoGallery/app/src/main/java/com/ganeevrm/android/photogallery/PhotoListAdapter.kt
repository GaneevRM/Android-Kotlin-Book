package com.ganeevrm.android.photogallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ganeevrm.android.photogallery.api.GalleryItem
import com.ganeevrm.android.photogallery.databinding.ListItemGalleryBinding

class PhotoViewHolder(private val binding: ListItemGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(galleryItem: GalleryItem, onItemClicked: (Uri) -> Unit) {
        binding.itemImageView.load(galleryItem.url)
        binding.root.setOnClickListener {
            onItemClicked(galleryItem.photoPageUri)
        }
    }
}

class PhotoListAdapter(
    private val galleryItems: List<GalleryItem>,
    private val onItemClicked: (Uri) -> Unit
) :
    RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGalleryBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = galleryItems[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount() = galleryItems.size
}