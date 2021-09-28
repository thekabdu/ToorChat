package kz.diaspora.app.ui.edit_profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kz.diaspora.app.R
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.databinding.FragmentEditProfileBinding
import kz.diaspora.app.domain.model.Language
import kz.diaspora.app.domain.model.User
import kz.diaspora.app.ui.custom_views.languages.OnLangItemClickListener
import kz.diaspora.app.ui.edit_profile.adapter.LangAdapter
import kz.diaspora.app.ui.edit_profile.languages.LanguagesBottomSheetCallback
import kz.diaspora.app.ui.edit_profile.languages.LanguagesBottomSheetDialogFragment
import kz.diaspora.app.utils.updateResources
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditProfileFragment : Fragment(), LanguagesBottomSheetCallback, DialogInterface{

    private val TAG: String = this::class.java.simpleName
    private val viewModel: EditProfileViewModel by viewModels()
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val Locale_Preference = "Locale Preference"
    private val Locale_KeyValue = "Saved Locale"
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var myLocale: Locale? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_profile, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
       /* initViews()*/
        //loadLocale()
        /* setSpinner()*/
        /*setLocale(viewModel.prefsImpl.getLanguage())*/

        binding.spLang.setOnClickListener {
           sp_lang.slideUp()
        }

//        setButtonLang(viewModel.prefsImpl.getLanguage())
//
//        sp_lang.setLanguage(viewModel.prefsImpl.getLanguage())
//
//        sp_lang.onLangItemClickListener = this
    }

    /*@SuppressLint("CommitPrefEdits")
    private fun initViews() {
        sharedPreferences = requireActivity().getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
    }
*//*
    override fun langItemClick(lang: String) {
        changeLocale(lang) //Change Locale on selection basis
        if (binding.btSave != null) {
            setButtonLang(lang)
        }
    }

    //Change Locale
    fun changeLocale(lang: String) {
        myLocale = Locale(lang) //Set Selected Locale
        saveLocale(lang) //Save the selected locale
        Locale.setDefault(myLocale) //set new locale as default
        val config = Configuration() //get Configuration
        config.locale = myLocale //set config locale as selected locale
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics()) //Update the config
    }

    fun saveLocale(lang: String?) {
        editor?.putString(Locale_KeyValue, lang)
        editor?.commit()
    }

    //Get locale method in preferences
    fun loadLocale() {
        val language: String? = sharedPreferences?.getString(Locale_KeyValue, "")
        language?.let { changeLocale(it) }
        Log.d("languagessss",language.toString())
    }


    private fun setButtonLang(lang: String) {
        when (lang) {
            "ru" -> binding.btSave.text = getString(R.string.save_ru)

            "en" -> binding.btSave.text = getString(R.string.save_en)
        }
    }*/

    private fun setObservers() {
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })

            statusData.observe(viewLifecycleOwner, {
                val statusList = mutableListOf<String>()
                statusList.add("")
                for (status in it) {
                    status.name?.let { it1 -> statusList.add(it1) }
                }
                val adapterLocation = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusList)
                adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spStatus.adapter = adapterLocation

                if (userData.value?.marital_status == null || !isNumber(userData.value!!.marital_status)) {
                    binding.spStatus.setSelection(0)
                }
                else binding.spStatus.setSelection(userData.value?.marital_status!!.toInt())
            })

            userData.observe(viewLifecycleOwner, {
                binding.etName.setText(it.name)
                binding.etSurname.setText(it.surname)
                binding.etPhone.setText(it.phone_number)

                var format = it.birthday.toString().split(" ")[0].split("-")
                try {
                    binding.etBirthDate.updateDate(Integer.parseInt(format[0]), Integer.parseInt(format[1]),Integer.parseInt(format[2]))
                } catch (e: Exception) {

                }

                if (it.marital_status == null || !isNumber(it.marital_status)) {
                    binding.spStatus.setSelection(0)
                }
                else binding.spStatus.setSelection(it.marital_status!!.toInt())

                if (it.whatsapp == null) {
                    binding.etWhatsappNumber.setText("")
                } else {
                    binding.etWhatsappNumber.setText(it.whatsapp.toString())
                }

                if (it.telegram == null) {
                    binding.etTelegramNumber.setText("")
                } else {
                    binding.etTelegramNumber.setText(it.telegram)
                }

                if (it.IMO == null) {
                    binding.etImoNumber.setText("")
                } else {
                    binding.etImoNumber.setText(it.IMO)
                }

                if (it.viber == null) {
                    binding.etViberNumber.setText("")
                } else {
                    binding.etViberNumber.setText(it.viber)
                }

                if (it.instagram == null) {
                    binding.etInstagramNumber.setText("")
                } else {
                    binding.etInstagramNumber.setText(it.instagram)
                }

                if (it.facebook == null) {
                    binding.etFacebookNumber.setText("")
                } else {
                    binding.etFacebookNumber.setText(it.facebook)
                }
            })
        }
    }

    private fun setListeners() {
        binding.btSave.setOnClickListener {
            val user = viewModel.userData.value

            if (user != null) {
                user.name = binding.etName.text.toString()
                user.surname = binding.etSurname.text.toString()
                user.phone_number = binding.etPhone.text.toString()

                if (binding.rbMale.isChecked)
                    user.gender = "male"

                if (binding.rbFemale.isChecked)
                    user.gender = "female"

                user.birthday = "${binding.etBirthDate.year}-${binding.etBirthDate.month}-${binding.etBirthDate.dayOfMonth}"

                Log.d(TAG, binding.spStatus.selectedItem.toString())

//                if (binding.spStatus.selectedItem.toString() == "Не выбрано") {
//                    user.marital_status = "Не выбрано"
//                } else if (binding.spStatus.selectedItem.toString() == "Путешествую") {
//                    user.marital_status = "Путешествую"
//                } else if (binding.spStatus.selectedItem.toString() == "Вдохновляюсь") {
//                    user.marital_status = "Вдохновляюсь"
//                } else if (binding.spStatus.selectedItem.toString() == "Ищу общение") {
//                    user.marital_status = "Ищу общение"
//                }

                user.marital_status = binding.spStatus.selectedItemPosition.toString()

                user.telegram = binding.etTelegramNumber.text.toString()
                user.whatsapp = binding.etWhatsappNumber.text.toString()
                user.IMO = binding.etImoNumber.text.toString()
                user.viber = binding.etViberNumber.text.toString()
                user.instagram = binding.etInstagramNumber.text.toString()
                user.facebook = binding.etFacebookNumber.text.toString()

                viewModel.saveUserData(user)
                Toast.makeText(context, "Сохранено", Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
    }

    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

    override fun languageClicked() {
        activity?.onConfigurationChanged(activity?.applicationContext!!.resources!!.configuration)
        activity?.recreate()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun cancel() {

    }

    override fun dismiss() {

    }
}