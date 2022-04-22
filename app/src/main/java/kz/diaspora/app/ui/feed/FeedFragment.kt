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
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.iid.FirebaseInstanceId
//import com.github.nkzawa.socketio.client.IO
//import com.github.nkzawa.socketio.client.Socket
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.SelectCountryFragment
import kz.diaspora.app.data.cloud.firebase.Constants
import kz.diaspora.app.data.cloud.firebase.Constants.Companion.BASE_URL
import kz.diaspora.app.data.cloud.firebase.Constants.Companion.FIREBASE_DEVICE_TOKEN
import kz.diaspora.app.data.cloud.firebase.Constants.Companion.SERVER_KEY
import kz.diaspora.app.data.cloud.firebase.FirebaseService
import kz.diaspora.app.databinding.FragmentFeedBinding
import kz.diaspora.app.domain.model.CategoryModel
import kz.diaspora.app.domain.model.PostModel
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.feed.adapter.CategoriesAdapter
import kz.diaspora.app.ui.feed.adapter.FeedAdapter
import kz.diaspora.app.ui.forgot_password.ForgotPasswordFragment
import kz.diaspora.app.ui.profile.ProfileFragment
import kz.diaspora.app.utils.EndlessRecyclerViewScrollListener
import kz.diaspora.app.utils.setDivider
import org.json.JSONObject
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
            loginData.observe(viewLifecycleOwner, {
                if(it.user.city_id == null){
                    val fragment = ProfileFragment()
                    val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                    fragmentTransaction.replace(R.id.content, fragment, "")
                    fragmentTransaction.commit()
                    Toast.makeText(context, "Выберите откуда вы и где вы находитесь", Toast.LENGTH_LONG).show()
                }
            })
//            roomsData.observe(viewLifecycleOwner, {
//                initSocket()
//            })
            categoriesData.observe(viewLifecycleOwner, {
                if (adapterCategory.isEmpty()) adapterCategory.add(it)
            })
            advertsData.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) addItemsByFilterSearch(it)
                binding.noValue.visibility = View.GONE
                if (it.isEmpty())     binding.noValue.visibility = View.VISIBLE

            })
            messageData.observe(viewLifecycleOwner, {

            })





            error.observe(viewLifecycleOwner, {

             //   Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun setListeners() {
        binding.etSearch.addTextChangedListener {
            addItemsByFilterSearch(viewModel.advertsData.value)
        }
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val deviceToken = instanceIdResult.token

            Log.d("deviceToken", deviceToken)
            viewModel.sendDeviceToken(deviceToken)
        }
    }

    private fun postNotification(title: String?, description: String?) {
        val volleyQueue = Volley.newRequestQueue(activity)

        val params = JSONObject().let {
            it.put("to", "${Constants.FIREBASE_DEVICE_TOKEN}/${getString(R.string.default_notification_channel_name)}")
            it.put("notification", JSONObject().apply {
                put("title", title)
                put("body", description)
                put("sound", "default")
                put("content_available", true)
                put("priority", "high")
            })
        }

        val request = object : JsonObjectRequest(
                Method.POST,
                Constants.BASE_URL,
                params,
                {
                  //  Toast.makeText(activity, "Push Notification Success", Toast.LENGTH_LONG).show()
                },
            { //Toast.makeText(activity, "Push Notification Failed", Toast.LENGTH_LONG).show()
                 }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "key=${Constants.SERVER_KEY}"
                return headers
            }
        }
        volleyQueue.add(request)
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
                binding.noValue.visibility = View.VISIBLE
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