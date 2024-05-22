package com.ganeevrm.android.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ganeevrm.android.criminalintent.databinding.FragmentCrimeListBinding
import com.ganeevrm.android.criminalintent.databinding.ListItemCrimeBinding
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val crimeListViewModel: CrimeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        binding.addCrime.setOnClickListener {
            showNewCrime()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeListViewModel.crimes.collect { crimes ->
                    binding.crimeRecyclerView.adapter = CrimeAdapter(crimes) { crimeId ->
                        findNavController().navigate(
                            CrimeListFragmentDirections.showCrimeDetail(
                                crimeId
                            )
                        )
                    }

                    if (crimes.isEmpty()) {
                        binding.placeholderLinearLayout.visibility = View.VISIBLE
                        binding.crimeRecyclerView.visibility = View.INVISIBLE
                    } else {
                        binding.placeholderLinearLayout.visibility = View.INVISIBLE
                        binding.crimeRecyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
        menu[0].isVisible = true
        menu[1].isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                showNewCrime()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showNewCrime() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newCrime = Crime(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                isSolved = false,
                requiresPolice = 0
            )
            crimeListViewModel.addCrime(newCrime)
            findNavController().navigate(CrimeListFragmentDirections.showCrimeDetail(newCrime.id))
        }
    }


    class CrimeHolder(private val binding: ListItemCrimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {
            var contentDescription = ""
            contentDescription += if(crime.isSolved){
                "Crime is solved"
            } else {
                "Crime is not solved"
            }
            contentDescription += " " + crime.title + " " + DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(crime.date)

            binding.root.contentDescription = contentDescription
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(crime.date)
            binding.crimeSolved.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.root.setOnClickListener {
                onCrimeClicked(crime.id)
            }
        }
    }

    class CrimeAdapter(
        private var crimes: List<Crime>,
        private var onCrimeClicked: (crimeId: UUID) -> Unit
    ) :
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
            holder.bind(crime, onCrimeClicked)
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