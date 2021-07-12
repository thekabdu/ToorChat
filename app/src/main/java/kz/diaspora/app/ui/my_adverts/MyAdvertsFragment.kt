package kz.diaspora.app.ui.my_adverts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentMyAdvertsBinding
import kz.diaspora.app.domain.model.PostModel
import kz.diaspora.app.ui.my_adverts.adapter.MyAdvertsAdapter
import kz.diaspora.app.utils.EndlessRecyclerViewScrollListener

@AndroidEntryPoint
class MyAdvertsFragment : Fragment(), MyAdvertsAdapter.OnAdvertClickListener {

    private val TAG: String = this::class.java.simpleName

    private val args: MyAdvertsFragmentArgs by navArgs()
    private val viewModel: MyAdvertsViewModel by viewModels()
    private var _binding: FragmentMyAdvertsBinding? = null
    private val binding get() = _binding!!
    private val adapter: MyAdvertsAdapter by lazy { MyAdvertsAdapter(arrayListOf(), args.myAdvertType, this) }
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_adverts, container, false
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

        (activity as AppCompatActivity).supportActionBar?.title =
            when (args.myAdvertType) {
                MyAdvertType.Favorite -> {
                    getString(R.string.favourites)
                }
                MyAdvertType.Active -> {
                    getString(R.string.active)
                }
                MyAdvertType.NotActive -> {
                    getString(R.string.not_active)
                }
                MyAdvertType.Pending -> {
                    getString(R.string.pending)
                }
            }

        val mLayoutManager = LinearLayoutManager(context)
        binding.rvMain.layoutManager = mLayoutManager
        binding.rvMain.adapter = adapter

        scrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getAdverts(args.myAdvertType)
            }
        }
        binding.rvMain.addOnScrollListener(scrollListener)
        binding.srlSwipe.setOnRefreshListener {
            scrollListener.resetValues()
            adapter.clear()
            viewModel.getAdverts(args.myAdvertType)
        }
        viewModel.getAdverts(args.myAdvertType)
    }

    private fun setObservers() {
        with(viewModel) {
            advertsData.observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter.clear()
                    adapter.add(it)
                }
            })
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
            activateStatusData.observe(viewLifecycleOwner, {
                if (it.message) {
                    Toast.makeText(context, "Успешно активировано", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Пост уже активирован", Toast.LENGTH_LONG).show()
                }
            })
            deactivateStatusData.observe(viewLifecycleOwner, {
                if (it.message) {
                    Toast.makeText(context, "Успешно деактивировано", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Пост уже деактивирован", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun setListeners() {

    }

    override fun onActivateClick(postModel: PostModel) {
        Log.d(TAG, "Activate")
        viewModel.activatePost(postModel)
    }

    override fun onDeactivateClick(postModel: PostModel) {
        Log.d(TAG, "deactivate")
        viewModel.deactivatePost(postModel)
    }

    override fun onEditClick(postModel: PostModel) {
        val action = MyAdvertsFragmentDirections.toEditAdvert(postModel, args.myAdvertType)
        findNavController().navigate(action)
    }

    override fun onViewClick(postModel: PostModel) {
        val action = MyAdvertsFragmentDirections.toDetail(postModel)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}