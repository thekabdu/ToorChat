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
class Splash1Fragment : Fragment(){

    private val TAG: String = this::class.java.simpleName
    private val viewModel: SplashViewModel by viewModels()
    private var _binding: FragmentSplash1Binding? = null
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
        //loadLocate()
        /*langItemClick(viewModel.prefsImpl.getLanguage())

        sp_lang.setOnClickListener {
            sp_lang.slideUp()
        }

        setButtonLang(viewModel.prefsImpl.getLanguage())

        sp_lang.setLanguage(viewModel.prefsImpl.getLanguage())

        sp_lang.onLangItemClickListener = this*/
    }

    private fun setListeners() {
        binding.btnNext.setOnClickListener {
            (activity as StartActivity).replaceFragment(Splash2Fragment())
        }
        /*binding.btnLanguage.setOnClickListener {
            showChangeLang()
        }*/
    }

   /* private fun showChangeLang(){
        val listItems = arrayOf("Русский", "English")

        val mBuilder = AlertDialog.Builder(activity)
        mBuilder.setTitle("Выберите Язык")
        mBuilder.setSingleChoiceItems(listItems, 0) { dialog, which ->
            if (which == 0) {
                setLocate("ru")
                binding.btnLanguage.setText("ru")

            } else if (which == 1) {
                setLocate("en")
                binding.btnLanguage.setText("en")

            }
            dialog.dismiss()
        }
        val mmDialog = mBuilder.create()
        mmDialog.show()
    }*/

   /* @SuppressLint("CommitPrefEdits")
    private fun setLocate(Lang: String) {
        val config = resources.configuration
        val locale = Locale(Lang)
        Log.d("language", binding.btnLanguage.text.toString())
        when (Lang) {
            "ru" -> {
                binding.btnNext.text = getString(R.string.next_ru)
                binding.textView.text = getString(R.string.welcome_to_the_world_ru)
            }
            "en" -> {
                binding.btnNext.text = getString(R.string.next_en)
                binding.textView.text = getString(R.string.welcome_to_the_world_en)
            }
        }
        Locale.setDefault(locale)
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        val prefs = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }*/

   /* private fun loadLocate() {
        val sharedPreferences = requireActivity().applicationContext.getSharedPreferences("LANGUAGES", Context.MODE_PRIVATE)
        val Lang = sharedPreferences.getString("LANGUAGE", "")
        setLocate(Lang.toString())
    }*/

    /*override fun langItemClick(lang: String) {
        viewModel.prefsImpl.setLanguage(lang)
        updateResources(requireContext(), lang)
//        updateResources(this.applicationContext, lang)
        onConfigurationChanged(resources.configuration)

        if (binding.btnNext != null) {
            setButtonLang(lang)
        }
    }

    private fun setButtonLang(lang: String) {
        when (lang) {
            "ru" -> {
                binding.btnNext.text = getString(R.string.next_ru)
                binding.textView.text = getString(R.string.welcome_to_the_world_ru)
            }
            "en" -> {
                binding.btnNext.text = getString(R.string.next_en)
                binding.textView.text = getString(R.string.welcome_to_the_world_en)
            }
        }
    }*/

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
