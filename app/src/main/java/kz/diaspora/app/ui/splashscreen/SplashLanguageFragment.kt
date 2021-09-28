package kz.diaspora.app.ui.splashscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentSplashLanguageBinding
import kz.diaspora.app.ui.StartActivity
import java.util.*


@AndroidEntryPoint
class SplashLanguageFragment : Fragment(), View.OnClickListener {

    private val TAG: String = this::class.java.simpleName
    private val viewModel: SplashViewModel by viewModels()
    private var _binding: FragmentSplashLanguageBinding? = null
    private val binding get() = _binding!!

    private val Locale_Preference = "Locale Preference"
    private val Locale_KeyValue = "Saved Locale"
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var myLocale: Locale? = null

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
                inflater, R.layout.fragment_splash_language, container, false
        )
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        loadLocale()
    }

    //Initiate all views
    @SuppressLint("CommitPrefEdits")
    private fun initViews() {
        sharedPreferences = requireActivity().getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
    }

    //Set Click Listener
    private fun setListeners() {
        binding.txtUse.setOnClickListener(this)
        binding.txtRus.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        var lang = "ru"
        when (view.id) {
            R.id.txt_use -> {
                lang = "en"
                (activity as StartActivity).replaceFragment(Splash1Fragment())
            }
            R.id.txt_rus -> {
                lang = "ru"
                (activity as StartActivity).replaceFragment(Splash1Fragment())
            }
        }
        changeLocale(lang) //Change Locale on selection basis
    }

    //Change Locale
    fun changeLocale(lang: String) {
        myLocale = Locale(lang) //Set Selected Locale
        saveLocale(lang) //Save the selected locale
        when (lang) {
            "en" -> {
                binding.txtLang.text = getString(R.string.Choose_your_language)
            }
        }
        Locale.setDefault(myLocale) //set new locale as default
        val config = Configuration() //get Configuration
        config.locale = myLocale //set config locale as selected locale
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics()) //Update the config
    }

    //Save locale method in preferences
    fun saveLocale(lang: String?) {
        editor?.putString(Locale_KeyValue, lang)
        editor?.commit()
    }

    //Get locale method in preferences
    fun loadLocale() {
        val language: String? = sharedPreferences?.getString(Locale_KeyValue, "")
        language?.let { changeLocale(it) }
        Log.d("languagess", language.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}