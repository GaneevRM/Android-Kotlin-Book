package com.ganeevrm.android.criminalintent

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ganeevrm.android.criminalintent.databinding.FragmentCrimeListBinding
import com.ganeevrm.android.criminalintent.databinding.ListItemCrimeBinding
import kotlinx.coroutines.launch


class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val crimeListViewModel: CrimeListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val crimes = crimeListViewModel.loadCrimes()
                binding.crimeRecyclerView.adapter = CrimeAdapter(crimes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    class CrimeHolder(val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime) {
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text = DateFormat.format("EEE, MMM, dd, yyyy", crime.date)
            binding.crimeSolved.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, "${crime.title} pressed!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
            return CrimeHolder(binding)
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemViewType(position: Int): Int {
            return crimes[position].requiresPolice
        }

    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}