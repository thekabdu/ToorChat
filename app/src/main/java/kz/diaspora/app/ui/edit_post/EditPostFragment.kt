package kz.diaspora.app.ui.edit_post

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentEditPostBinding
import kz.diaspora.app.ui.my_adverts.MyAdvertType
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File
import java.lang.Exception

@AndroidEntryPoint
class EditPostFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: EditPostViewModel by viewModels()

    private val args: EditPostFragmentArgs by navArgs()

    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var easyImage: EasyImage
    private var avatarFile: File? = null
    private lateinit var category_id: String
    private lateinit var city_id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_post, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.post = args.postModel

        try {
            val post = args.postModel

            if (args.myAdvertType == MyAdvertType.NotActive) {
                binding.btnDelete.visibility = View.VISIBLE
            }
        } catch (e: Exception) {

        }

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

            cityList.observe(viewLifecycleOwner, {
                if (it != null) {
                    val fromList = ArrayList<String>()
                    if (it.size != 0) {
                        fromList.add("Откуда вы?")

                        for (i in it.indices) {
                            fromList.add(it[i].city_name)
                        }
                    } else {
                        fromList.add("Пожалуйста, выберите страну в профиле")
                    }

                    val adapterFrom = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item, fromList
                    )
                    adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.sFrom.adapter = adapterFrom
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
                }
            })
        }
    }

    @SuppressLint("ShowToast")
    private fun setListeners() {
        binding.btnPublish.setOnClickListener {
            if (binding.etTitle.text.isNullOrEmpty() ||
                binding.etDescription.text.isNullOrEmpty() ||
                binding.etPhone.text.isNullOrEmpty() ||
                binding.etEmail.text.isNullOrEmpty() ||
                avatarFile == null
            ) {
                Toast.makeText(context, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show()
            } else {

                viewModel.editPost(
                    args.postModel.id.toString(),
                    avatarFile!!.path,
                    binding.etTitle.text.toString(),
                    binding.etDescription.text.toString(),
                    category_id,
                    city_id,
                    binding.etEmail.text.toString(),
                    binding.etPhone.text.toString(),
                )

                Toast.makeText(context, "Опубликовано", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    city_id = "${viewModel.cityList.value?.get(position - 1)?.id}"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.sCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    category_id = "${viewModel.categoryList.value?.get(position - 1)?.id}"
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.chooseFile.setOnClickListener {
            easyImage = EasyImage.Builder(requireContext())
                .setCopyImagesToPublicGalleryFolder(true)
                .setFolderName("diaspora")
                .allowMultiple(false)
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
                    avatarFile = imageFiles[0].file
                    Glide.with(requireContext())
                        .load(imageFiles[0].file)
                        .circleCrop()
                        .into(binding.ivImage)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}