package kz.diaspora.app.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
//import com.github.nkzawa.socketio.client.IO
//import com.github.nkzawa.socketio.client.Socket
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentFeedBinding
import kz.diaspora.app.domain.model.CategoryModel
import kz.diaspora.app.domain.model.PostModel
import kz.diaspora.app.ui.feed.adapter.CategoriesAdapter
import kz.diaspora.app.ui.feed.adapter.FeedAdapter
import kz.diaspora.app.utils.EndlessRecyclerViewScrollListener
import kz.diaspora.app.utils.setDivider
import java.net.URISyntaxException

@AndroidEntryPoint
class FeedFragment : Fragment(), FeedAdapter.OnFeedClickListener,
    CategoriesAdapter.OnCategoryClickListener {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: FeedViewModel by viewModels()
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
//    private var socket: Socket? = null
    private var currentCategory: Int = 1

    private val adapterCategory: CategoriesAdapter by lazy {
        CategoriesAdapter(
            arrayListOf(),
            this
        )
    }
    private val adapter: FeedAdapter by lazy { FeedAdapter(arrayListOf(), this,requireContext()) }
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_feed, container, false
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
        val mLayoutManager1 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.layoutManager = mLayoutManager1
        binding.rvCategories.adapter = adapterCategory

        val mLayoutManager = LinearLayoutManager(context)
        binding.rvFeed.layoutManager = mLayoutManager
        binding.rvFeed.adapter = adapter
        binding.rvFeed.setDivider(R.drawable.divider_base)

        scrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getPosts(currentCategory)
            }
        }
        binding.rvFeed.addOnScrollListener(scrollListener)
//        viewModel.getRooms()
        binding.srlSwipe.setOnRefreshListener {
            scrollListener.resetValues()
            adapter.clear()
            viewModel.getPosts(currentCategory)
        }
    }

    private fun setObservers() {
        with(viewModel) {
//            roomsData.observe(viewLifecycleOwner, {
//                initSocket()
//            })
            categoriesData.observe(viewLifecycleOwner, {
                if (adapterCategory.isEmpty()) adapterCategory.add(it)
            })
            advertsData.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) addItemsByFilterSearch(it)
            })
            messageData.observe(viewLifecycleOwner, {

            })
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun setListeners() {
        binding.etSearch.addTextChangedListener {
            addItemsByFilterSearch(viewModel.advertsData.value)
        }
    }

    private fun addItemsByFilterSearch(posts: List<PostModel>?) {
        val searchText = binding.etSearch.text.toString()
        adapter.clear()
        if (posts != null) {
            if (searchText.isEmpty()) adapter.add(posts)
            else {
                for (i in posts.indices) {
                    val post = posts[i]
                    if ((post.title != null && post.title.contains(searchText)) || (post.description != null && post.description.contains(searchText))) {
                        adapter.add(post)
                    }
                }
            }
        }
    }

    override fun onFeedClick(postModel: PostModel) {
        val action = FeedFragmentDirections.toDetailPost(postModel)
        findNavController().navigate(action)
    }

    override fun onSendClick(postModel: PostModel, text: String) {
        viewModel.sendCommentToPost(postModel, text)
        scrollListener.resetValues()
        adapter.clear()
        viewModel.getPosts(currentCategory)
    }

    override fun onLikeClick(post_id: Int) {
        viewModel.likePost(post_id)
    }

    override fun onDisLikeClick(post_id: Int) {
        viewModel.dislikePost(post_id)
    }

    override fun onCategoryClick(categoryModel: CategoryModel) {
        viewModel.getPosts(categoryModel.id)
        currentCategory = categoryModel.id
        adapter.clear()
    }

    override fun onResume() {
        super.onResume()
        scrollListener.resetValues()
        adapter.clear()
        currentCategory = 1
        viewModel.getPosts(currentCategory)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        currentCategory = 1
    }
}