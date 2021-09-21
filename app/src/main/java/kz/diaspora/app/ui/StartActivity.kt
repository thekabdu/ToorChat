package kz.diaspora.app.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.ActivityStartBinding
import kz.diaspora.app.ui.splashscreen.SplashFragment
import kz.diaspora.app.ui.splashscreen.SplashLanguageFragment
import kz.diaspora.app.utils.Utils
import kz.diaspora.app.utils.setLocale

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    private val fm: FragmentManager = supportFragmentManager
    private var _binding: ActivityStartBinding? = null
    private val binding get() = _binding!!
    val view get() = binding.flContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addFragment(SplashFragment.newInstance())
    }

    fun addFragment(fragment: Fragment) {
        fm.beginTransaction()
//                .setCustomAnimations(R.anim.fade_in,
//                        R.anim.fade_out,
//                        R.anim.fade_in,
//                        R.anim.fade_out)
                .add(R.id.fl_content, fragment)
                .addToBackStack(fragment.tag)
                .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_bottom,
            R.anim.slide_out_bottom)
            .replace(R.id.fl_content, fragment)
            .commit()
    }

    override fun onBackPressed() {
        Utils.hideKeyboard(binding.flContent)
        if (fm.fragments.size > 1) {
            fm.popBackStack()
        } else {
            finish()
        }
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(setLocale(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}