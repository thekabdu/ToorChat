package kz.diaspora.app.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.ui.MainActivity
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.sign_in.SignInFragment

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName
    private val viewModel: SplashViewModel by viewModels()

    companion object {
        fun newInstance() : Fragment {
            val fragment = SplashFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun setObservers() {
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })

            authData.observe(viewLifecycleOwner, {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (it) {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        (activity as StartActivity).finish()
                    } else {
                        (activity as StartActivity).replaceFragment(Splash1Fragment())
                    }
                }, 1000)
            })
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setObservers()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}