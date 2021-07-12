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
import kz.diaspora.app.databinding.FragmentLocationCityBinding
import kz.diaspora.app.databinding.FragmentLocationFromCityBinding
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class LocationFromCityFragment : Fragment() {
    private val TAG: String = this::class.java.simpleName

    private val cityViewModel: LocationFromCityViewModel by viewModels()
    private var _binding: FragmentLocationFromCityBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_location_from_city, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = cityViewModel
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
        with(cityViewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })

            cityList.observe(viewLifecycleOwner, {
                if (it != null) {
                    val fromList = ArrayList<String>()

                    for (i in it.indices) {
                        fromList.add(it[i].city_name)
                    }

                    val adapterFrom = ArrayAdapter(requireContext(),
                        android.R.layout.simple_list_item_1, fromList)
                    binding.cityList.adapter = adapterFrom
                }
            })

            searchList.observe(viewLifecycleOwner, {
                val adapterFrom = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
                binding.cityList.adapter = adapterFrom
            })
        }
    }

    private fun setListeners() {

        binding.cityList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if(cityViewModel.searchList.value == null) {
                cityViewModel.saveCity(
                    cityViewModel.cityList.value?.get(position)?.id.toString(),
                    cityViewModel.cityList.value?.get(position)?.city_name.toString()
                )
            }
            else {
                cityViewModel.saveCity(
                    cityViewModel.searchCityList.value?.get(position)?.id.toString(),
                    cityViewModel.searchCityList.value?.get(position)?.city_name.toString()
                )
            }
            val action = LocationFromCityFragmentDirections.toProfile()
            findNavController().navigate(action)
        }

        binding.etSearch.addTextChangedListener {
            addItemsByFilterSearch()
        }
    }

    private fun addItemsByFilterSearch() {
        val cities = cityViewModel.cityList.value
        val searchText = binding.etSearch.text.toString()
        if (cities != null) {
            val fromList = ArrayList<String>()
            if (searchText.isEmpty()) {
                for (i in cities) {
                    fromList.add(i.city_name)
                }
                val adapterFrom = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fromList)
                binding.cityList.adapter = adapterFrom
                cityViewModel.clearSearch()
            }
            else {
                cityViewModel.getSearchList(searchText)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}