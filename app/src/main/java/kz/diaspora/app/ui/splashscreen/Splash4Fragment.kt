package kz.diaspora.app.ui.splashscreen

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentSplash1Binding
import kz.diaspora.app.databinding.FragmentSplash4Binding
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.sign_in.SignInFragment

@AndroidEntryPoint
class Splash4Fragment : Fragment() {

    private val TAG: String = this::class.java.simpleName
    private var _binding: FragmentSplash4Binding? = null
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
            inflater, R.layout.fragment_splash4, container, false
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
            (activity as StartActivity).replaceFragment(SignInFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}