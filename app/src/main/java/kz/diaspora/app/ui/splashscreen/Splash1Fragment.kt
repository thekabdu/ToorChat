package kz.diaspora.app.ui.splashscreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_splash1.*
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentSplash1Binding
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.custom_views.languages.OnLangItemClickListener
import kz.diaspora.app.ui.sign_up.SignUpViewModel
import kz.diaspora.app.utils.updateResources
import java.util.*


@AndroidEntryPoint
class Splash1Fragment : Fragment() {

    private val TAG: String = this::class.java.simpleName
    private val viewModel: SplashViewModel by viewModels()
    private var _binding: FragmentSplash1Binding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): Fragment {
            val fragment = SplashFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_splash1, container, false
        )
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnNext.setOnClickListener {
            (activity as StartActivity).replaceFragment(Splash2Fragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
