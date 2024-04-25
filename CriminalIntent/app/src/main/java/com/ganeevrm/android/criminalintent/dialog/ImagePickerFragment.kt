package com.ganeevrm.android.criminalintent.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.ganeevrm.android.criminalintent.R
import com.ganeevrm.android.criminalintent.utils.PictureUtils
import java.io.File

class ImagePickerFragment : DialogFragment() {
    private val args: ImagePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater

        val view = inflater.inflate(R.layout.crime_photo_zoom_view, null)
        val imageView: ImageView = view.findViewById(R.id.crime_photo_zoom)

        val photoFile = args.photoFileName?.let {
            File(requireContext().applicationContext.filesDir, it)
        }
        if (photoFile?.exists() == true) {
            imageView.doOnLayout { measuredView ->
                val scaledBitmap =
                    PictureUtils.getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                imageView.setImageBitmap(scaledBitmap)
            }
        } else {
            imageView.setImageBitmap(null)
        }

        builder.setView(view)
        return builder.create()
    }
}