package kz.diaspora.app.ui.edit_advert

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentEditAdvertBinding
import kz.diaspora.app.ui.profile.ProfileFragmentDirections
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File

@AndroidEntryPoint
class EditAdvertFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: EditAdvertViewModel by viewModels()

    private var _binding: FragmentEditAdvertBinding? = null
    private val binding get() = _binding!!
    private lateinit var easyImage: EasyImage
    private var avatarFile: File? = null
    private var avatarFilePath: String? = null
    private var category_id: String? = null
    private lateinit var city_id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_advert, container, false
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

    private fun setObservers() {
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })

            postData.observe(viewLifecycleOwner, {
                if (it != null) {
                    Glide.with(requireContext())
                        .load(it.path)
                        .placeholder(R.drawable.ic_add_photo)
                        .circleCrop()
                        .into(binding.ivImage)
                    avatarFilePath = it.path
                    binding.etTitle.setText(it.title)
                    binding.etDescription.setText(it.description)
                    it.category_id?.let { it1 -> binding.sCategories.setSelection(it1.toInt()) }
                    binding.etPhone.setText(it.phone)
                }
            })

            countryCityNames.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.sFrom.text = it
                } else {
                    binding.sFrom.text = "Местоположение"
                }
            })

            categoryList.observe(viewLifecycleOwner, {
                if (it != null) {
                    val fromList = ArrayList<String>()
                    if (it.size != 0) {
                        fromList.add("Выберите категорию")
                        for (i in it.indices) {
                            fromList.add(it[i].category_name)
                        }
                    }

                    val adapterFrom = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item, fromList
                    )
                    adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.sCategories.adapter = adapterFrom

                    if (!viewModel.postData.value?.category_id.isNullOrEmpty()) {
                        viewModel.postData.value?.category_id?.let { it1 -> binding.sCategories.setSelection(it1.toInt()) }
                    }
                }
            })
        }
    }

    @SuppressLint("ShowToast")
    private fun setListeners() {
        binding.btnPublish.setOnClickListener {
            Log.d(TAG, "Clicked $avatarFile")

            if (binding.etTitle.text.isNullOrEmpty() ||
                binding.etDescription.text.isNullOrEmpty() ||
                binding.etPhone.text.isNullOrEmpty() ||
//                binding.etEmail.text.isNullOrEmpty() ||
                avatarFilePath.isNullOrEmpty() ||
                binding.sFrom.text == "Местоположение"
            ) {
                Toast.makeText(context, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT)
                    .show()
            } else {

                viewModel.createPost(
                    avatarFilePath!!,
                    binding.etTitle.text.toString(),
                    binding.etDescription.text.toString(),
                    category_id,
                    city_id,
                    "", //binding.etEmail.text.toString()
                    binding.etPhone.text.toString()
                )
                getActivity()?.finish()

                Toast.makeText(context, "Добавлено в ожидающие", Toast.LENGTH_SHORT).show()
                viewModel.clear()
            }

        }

        binding.sFrom.setOnClickListener {
            val action = EditAdvertFragmentDirections.toLocationCountry()
            findNavController().navigate(action)
            viewModel.savePostData(
                avatarFilePath,
                binding.etTitle.text.toString(),
                binding.etDescription.text.toString(),
                category_id,
                binding.etPhone.text.toString()
            )
        }

        binding.sCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    category_id = viewModel.categoryList.value?.get(position)?.id.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.chooseFile.setOnClickListener {
            easyImage = EasyImage.Builder(requireContext())
                .setCopyImagesToPublicGalleryFolder(true)
                .setFolderName("diaspora")
                .allowMultiple(false) //todo multiple
                .build()
            easyImage.openChooser(this)
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
                    avatarFilePath = imageFiles[0].file.path
                    Glide.with(requireContext())
                        .load(avatarFilePath)
                        .circleCrop()
                        .into(binding.ivImage)
//                    avatarFilePath = imageFiles[0].file.path
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.clear()
    }
}