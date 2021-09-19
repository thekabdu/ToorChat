package kz.diaspora.app.ui.splashscreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentSplash1Binding
import kz.diaspora.app.ui.StartActivity
import java.util.*


@AndroidEntryPoint
class Splash1Fragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

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
        loadLocate()
    }

    private fun setListeners() {
        binding.btnNext.setOnClickListener {
            (activity as StartActivity).replaceFragment(Splash2Fragment())
        }
        binding.btnLanguage.setOnClickListener {
            showChangeLang()
        }
    }

    private fun showChangeLang(){
        val listItems = arrayOf("Русский", "English")

        val mBuilder = AlertDialog.Builder(activity)
        mBuilder.setTitle("Выберите Язык")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialog, which ->
            if (which == 0) {
                setLocate("")
                binding.btnLanguage.text = "RU"
                recreate(requireActivity())
            } else if (which == 1) {
                setLocate("en")
                recreate(requireActivity())
                binding.btnLanguage.text = "EN"
            }
            dialog.dismiss()
        }
        var mmDialog = mBuilder.create()
        mmDialog.show()
    }

    @SuppressLint("CommitPrefEdits")
    private fun setLocate(Lang: String) {
        val config = resources.configuration
        val locale = Locale("")
        Locale.setDefault(locale)
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        val prefs = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = prefs!!.edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

  /* fun changelanguage(context: Context): String? {
        //String lang = "hi_IN";
        //  Locale locale = new Locale(lang);
        val locale = Locale.getDefault()
        Locale.setDefault(locale)
        System.out.println("My_Lang$locale")
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config,
                context.resources.displayMetrics)
        return locale.toString()
    }*/

    private fun loadLocate() {
        val sharedPreferences = requireActivity().applicationContext.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocate(language!!)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
