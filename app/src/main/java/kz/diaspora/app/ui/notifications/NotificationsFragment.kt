package kz.diaspora.app.ui.notifications

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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentNotificationsBinding
import kz.diaspora.app.domain.model.CommentModel
import kz.diaspora.app.domain.model.NotificationModel
import kz.diaspora.app.domain.model.PostModel
import kz.diaspora.app.ui.notifications.adapter.NotificationsAdapter
import kz.diaspora.app.utils.EndlessRecyclerViewScrollListener

@AndroidEntryPoint
class NotificationsFragment : Fragment(), NotificationsAdapter.OnNotificationClickListener {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: NotificationsViewModel by viewModels()
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val adapter: NotificationsAdapter by lazy { NotificationsAdapter(arrayListOf(), this) }
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notifications, container, false
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
        val mLayoutManager = LinearLayoutManager(context)
        binding.rvMainHome.layoutManager = mLayoutManager
        binding.rvMainHome.adapter = adapter

        scrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {

            }
        }
        binding.rvMainHome.addOnScrollListener(scrollListener)
        binding.swipe.setOnRefreshListener {
            scrollListener.resetValues()
            adapter.clear()
            viewModel.getNotifications()
        }
    }

    private fun setObservers() {
        with(viewModel) {
            notificationsData.observe(viewLifecycleOwner, {
                binding.tvNull.visibility = View.GONE
                if (it.isNotEmpty() && adapter.isEmpty()) {
                    val list = arrayListOf<NotificationModel>()
                    list.addAll(it)
                    list.addAll(it)
                    list.addAll(it)
                    adapter.add(list)
                }
                else{
                    binding.tvNull.visibility = View.VISIBLE
                }
            })
            postData.observe(viewLifecycleOwner, {
                if (it != null) {
                    val action = NotificationsFragmentDirections.toDetailPost(it)
                    findNavController().navigate(action)
                    viewModel.clearPost()
                }
            })
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun setListeners() {

    }

    override fun onViewPostClick(notificationModel: NotificationModel) {
        val post = notificationModel.post_id?.let {
            PostModel(
                viewModel.postData.value?.category_id,
                viewModel.postData.value?.city_id,
                viewModel.postData.value?.comments_count,
                viewModel.postData.value?.date,
                viewModel.postData.value?.description,
                viewModel.postData.value?.email,
                it,
                CommentModel("", "", 0, "", "", 0, ""),
                viewModel.postData.value?.likes,
                viewModel.postData.value?.name,
                viewModel.postData.value?.phone,
                viewModel.postData.value?.post_image,
                viewModel.postData.value?.seen,
                viewModel.postData.value?.surname,
                viewModel.postData.value?.title,
                viewModel.postData.value?.user_id,
                viewModel.postData.value?.user_image,
                false
            )
        }
        if (post != null) {
            viewModel.getPost(post)
        }
    }

    override fun onAnswerPostClick(notificationModel: NotificationModel) {
        val action = NotificationsFragmentDirections.toProfileInfo(notificationModel.user_id.toString())
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}