package kz.diaspora.app.ui.splashscreen

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentSplash1Binding
import kz.diaspora.app.databinding.FragmentSplash2Binding
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.utils.setLocale

@AndroidEntryPoint
class Splash2Fragment : Fragment() {

    private val TAG: String = this::class.java.simpleName
    private var _binding: FragmentSplash2Binding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() : Fragment {
            val fragment = SplashFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash2, container, false
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
            (activity as StartActivity).replaceFragment(Splash3Fragment())
        }
    }

    /*override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(setLocale(newBase))
    }*/

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}