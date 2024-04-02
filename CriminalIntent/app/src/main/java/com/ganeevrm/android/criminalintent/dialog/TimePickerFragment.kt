package com.ganeevrm.android.criminalintent.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar

class TimePickerFragment : DialogFragment() {
    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val resultTime = Calendar.getInstance()
            resultTime.time = args.crimeDate
            resultTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            resultTime.set(Calendar.MINUTE, minute)
            setFragmentResult(
                DatePickerFragment.REQUEST_KEY_DATE,
                bundleOf(DatePickerFragment.BUNDLE_KEY_DATE to resultTime.time)
            )
        }
        val calendar = Calendar.getInstance()
        calendar.time = args.crimeDate
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(requireContext(), timeListener, initialHour, initialMinute, true)
    }

}