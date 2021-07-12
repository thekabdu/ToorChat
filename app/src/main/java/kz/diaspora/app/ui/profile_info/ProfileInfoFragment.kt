package kz.diaspora.app.ui.profile_info

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentProfileInfoBinding
import kz.diaspora.app.ui.MainActivity


@AndroidEntryPoint
class ProfileInfoFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: ProfileInfoViewModel by viewModels()
    private var _binding: FragmentProfileInfoBinding? = null
    private val binding get() = _binding!!
    private val args: ProfileInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_info, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getUserData(args.userId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObservers()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserData(args.userId)
        setObservers()
    }

    private fun initView() {
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
            userData.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.user = it
                }

                if (it.marital_status == null || !isNumber(it.marital_status) || it.marital_status!!.toInt() == 0) {
                    binding.tvStatus.setText("Статус не выбран")
                }
                else binding.tvStatus.setText(statusData.value?.get(it.marital_status!!.toInt()-1)?.name)
            })
        }
    }

    private fun setListeners() {
        binding.btnEditProfile.setOnClickListener {
            if (!binding.btnEditProfile.text.toString().isEmpty()) {
                call()
            }
        }
        binding.btnWhatsapp.setOnClickListener {
            if (!viewModel.userData.value?.whatsapp?.isEmpty()!!) {
                whatsApp()
            }
            else Toast.makeText(context, "Этот пользователь не ввел свой номер WhatsApp", Toast.LENGTH_LONG).show()
        }
    }

    fun call() {
        val permissionCheck = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE) }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            activity?.let {
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CALL_PHONE), 123)
            }
        } else {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: " + binding.btnEditProfile.text.toString()))
            startActivity(intent)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun whatsApp() {

        try {
            val packageManager = activity?.packageManager
            val i = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=${viewModel.userData.value?.whatsapp}"
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
           // if (packageManager?.let { i.resolveActivity(it) } != null){
                startActivity(i)
           // }else{
                //Toast.makeText(context, "Пожалуйста, установите Whatsapp", Toast.LENGTH_SHORT).show()
           // }

        }catch (e: Exception){
            Toast.makeText(context, ""+e.stackTrace, Toast.LENGTH_SHORT).show()
        }
    }

    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}