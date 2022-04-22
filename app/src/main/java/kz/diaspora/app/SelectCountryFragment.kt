package kz.diaspora.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.databinding.SelectCountryFragmentBinding
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.newlocation.NewSelectCountryFromFragment
import kz.diaspora.app.ui.profile.ProfileFragmentDirections

@AndroidEntryPoint
class SelectCountryFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    public val viewModel: SelectCountryViewModel by viewModels()
    private var _binding: SelectCountryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.select_country_fragment, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })

            countryCityNames.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.sLocation.text = it
                } else {
                    binding.sLocation.text = "Местоположение"
                }
            })

            countryFromCityNames.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.sFrom.text = it
                } else {
                    binding.sFrom.text = "Откуда вы?"
                }
            })
        }
    }

    private fun setListeners() {
        binding.sFrom.setOnClickListener {
            (activity as StartActivity).addFragment(NewSelectCountryFromFragment())

        }

        binding.sLocation.setOnClickListener {
            (activity as StartActivity).addFragment(NewSelectLocationCountryFragment())
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}