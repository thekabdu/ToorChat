package kz.diaspora.app.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentProfileBinding
import kz.diaspora.app.ui.MainActivity
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.edit_profile.languages.LanguagesBottomSheetCallback
import kz.diaspora.app.ui.my_adverts.MyAdvertType
import kz.diaspora.app.utils.setLocale
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment(),LanguagesBottomSheetCallback {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var easyImage: EasyImage
    private var avatarFile: File? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_profile, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObservers()
        setListeners()
    }

    private fun initView() {
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })

            authData.observe(viewLifecycleOwner, {
                if (!it) {
                    startActivity(Intent(requireContext(), StartActivity::class.java))
                    (activity as MainActivity).finish()
                }
            })

            userData.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.user = it
                }
            })

            newUserData.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.user = it
                    if (it.user_image.isNullOrEmpty()) viewModel.getUserData()
                }
            })

            countsData.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.tvFavoriteCount.text = it.favourite_posts.toString()
                    binding.tvWaitCount.text = it.wait.toString()
                    binding.tvActiveCount.text = it.active.toString()
                    binding.tvNotActiveCount.text = it.not_active.toString()
                }
            })

            countryCityNames.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.sLocation.text = it
                } else {
                    binding.sLocation.text = "Местоположение"
                }
            })

//            countryFrom.observe(viewLifecycleOwner, {
//                if (it != null && countryList.value != null) {
//                    for (i in countryList.value?.indices!!) {
//                        if (countryList.value!![i].country_name == it) {
//                            binding.sFrom.setSelection(i + 1)
//                        }
//                    }
//
//                } else {
//                    binding.sFrom.setSelection(0)
//                }
//            })

            countryFromCityNames.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.sFrom.text = it
                } else {
                    binding.sFrom.text = "Откуда вы?"
                }
//                if (it != null) {
//                    val fromList = ArrayList<String>()
//                    fromList.add("Откуда вы?")
//
//                    for (i in it.indices) {
//                        fromList.add(it[i].country_name)
//                    }
//
//                    val adapterFrom = ArrayAdapter(
//                        requireContext(),
//                        android.R.layout.simple_spinner_item, fromList
//                    )
//                    adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                    binding.sFrom.adapter = adapterFrom
//
//                    for (i in it.indices) {
//                        if (fromList[i] == countryFrom.value) {
//                            binding.sFrom.setSelection(i)
//                        }
//                    }
//                }
            })
        }
    }

    private fun setListeners() {
        binding.llFavorites.setOnClickListener {
            val action = ProfileFragmentDirections.toMyAdverts(MyAdvertType.Favorite)
            findNavController().navigate(action)
        }
        binding.llPending.setOnClickListener {
            val action = ProfileFragmentDirections.toMyAdverts(MyAdvertType.Pending)
            findNavController().navigate(action)
        }
        binding.llActive.setOnClickListener {
            val action = ProfileFragmentDirections.toMyAdverts(MyAdvertType.Active)
            findNavController().navigate(action)
        }
        binding.llNotActive.setOnClickListener {
            val action = ProfileFragmentDirections.toMyAdverts(MyAdvertType.NotActive)
            findNavController().navigate(action)
        }
        binding.btnCreateAdvert.setOnClickListener {
            val action = ProfileFragmentDirections.toCreateAdvert()
            findNavController().navigate(action)
        }
        binding.btnEditProfile.setOnClickListener {
            val action = ProfileFragmentDirections.toEditProfile()
            findNavController().navigate(action)
        }

        binding.sFrom.setOnClickListener {
            val action = ProfileFragmentDirections.toLocationCountryFrom()
            findNavController().navigate(action)
        }

        binding.sLocation.setOnClickListener {
            val action = ProfileFragmentDirections.toLocationCountry()
            findNavController().navigate(action)
        }

        binding.btnExit.setOnClickListener {
            viewModel.exit()
        }

        binding.ivAvatar.setOnClickListener {
            easyImage = EasyImage.Builder(requireContext())
                .setCopyImagesToPublicGalleryFolder(true)
                .setFolderName("diaspora")
                .allowMultiple(false)
                .build()
            easyImage.openChooser(this)
        }

        binding.txtMessageHelp.setOnClickListener {
            val uri: Uri = Uri.parse("https://diaspora.direct/support")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(
                requestCode,
                resultCode,
                data,
                requireActivity(),
                object : DefaultCallback() {
                    override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                        avatarFile = imageFiles[0].file
                        viewModel.changeUserAvatar(avatarFile!!.path)
                        Glide.with(requireContext())
                                .load(imageFiles[0].file)
                                .placeholder(R.drawable.avatar_placeholder)
                                .circleCrop()
                                .into(binding.ivAvatar)
                    }

                    override fun onImagePickerError(error: Throwable, source: MediaSource) {
                        //Some error handling
                        error.printStackTrace()
                    }

                    override fun onCanceled(source: MediaSource) {
                        //Not necessary to remove any files manually anymore
                    }
                })
    }



    override fun onResume() {
        super.onResume()
        viewModel.getPostsCount()
        viewModel.getUserData()
        setObservers()
    }
    override fun languageClicked() {
        activity?.onConfigurationChanged(activity?.applicationContext!!.resources!!.configuration)
        activity?.recreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}