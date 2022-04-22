package kz.diaspora.app.ui.newlocation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import kz.diaspora.app.NewSelectCityFragment
import kz.diaspora.app.NewSelectLocationCountryFragment
import kz.diaspora.app.R
import kz.diaspora.app.SelectCountryFragment
import kz.diaspora.app.databinding.NewSelectCityFromFragmentBinding
import kz.diaspora.app.ui.MainActivity
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.location_from.LocationFromCityFragmentDirections


@AndroidEntryPoint
class NewSelectCityFromFragment: Fragment() {
private val TAG: String = this::class.java.simpleName

private val cityViewModel: NewSelectCityFromViewModel by viewModels()
private var _binding: NewSelectCityFromFragmentBinding? = null
private val binding get() = _binding!!


override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    _binding = DataBindingUtil.inflate(
        inflater, R.layout.new_select_city_from_fragment, container, false
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
        (activity as StartActivity).addFragment(SelectCountryFragment())
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
