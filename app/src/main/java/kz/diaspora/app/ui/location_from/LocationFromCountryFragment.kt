package kz.diaspora.app.ui.location_from

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentLocationBinding
import kz.diaspora.app.databinding.FragmentLocationFromCountryBinding
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class LocationFromCountryFragment : Fragment() {
    private val TAG: String = this::class.java.simpleName

    private val countryViewModel: LocationFromCountryViewModel by viewModels()
    private var _binding: FragmentLocationFromCountryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_location_from_country, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = countryViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObservers()
        setListeners()
    }

    private fun initView() {

    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        with(countryViewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })

            countryList.observe(viewLifecycleOwner, {
                if (it != null) {
                    val fromList = ArrayList<String>()

                    for (i in it.indices) {
                        fromList.add(it[i].country_name)
                    }

                    val adapterFrom = ArrayAdapter(requireContext(),
                        android.R.layout.simple_list_item_1, fromList)
                    binding.countryList.adapter = adapterFrom
                }
            })

            searchList.observe(viewLifecycleOwner, {
                val adapterFrom = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
                binding.countryList.adapter = adapterFrom
            })
        }
    }

    private fun setListeners() {
        binding.countryList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if(countryViewModel.searchList.value == null) {
                countryViewModel.saveCountry(
                    countryViewModel.countryList.value?.get(position)?.id.toString(),
                    countryViewModel.countryList.value?.get(position)?.country_name.toString()
                )
            }
            else {
                countryViewModel.saveCountry(
                    countryViewModel.searchCountryList.value?.get(position)?.id.toString(),
                    countryViewModel.searchCountryList.value?.get(position)?.country_name.toString()
                )
            }
            val action = LocationFromCountryFragmentDirections.toLocationCityFrom()
            findNavController().navigate(action)
        }
        binding.etSearch.addTextChangedListener {
            addItemsByFilterSearch()
        }
    }

    private fun addItemsByFilterSearch() {
        val countries = countryViewModel.countryList.value
        val searchText = binding.etSearch.text.toString()
        if (countries != null) {
            val fromList = ArrayList<String>()
            if (searchText.isEmpty()) {
                for (i in countries) {
                    fromList.add(i.country_name)
                }
                val adapterFrom = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fromList)
                binding.countryList.adapter = adapterFrom
                countryViewModel.clearSearch()
            }
            else {
                countryViewModel.getSearchList(searchText)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}