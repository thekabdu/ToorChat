package kz.diaspora.app.ui.feed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.core.App
import kz.diaspora.app.databinding.FragmentDetailBinding
import kz.diaspora.app.databinding.FragmentFeedBinding
import kz.diaspora.app.domain.model.CategoryModel
import kz.diaspora.app.domain.model.CommentModel
import kz.diaspora.app.domain.model.PostModel
import kz.diaspora.app.ui.feed.adapter.CommentAdapter
import kz.diaspora.app.ui.feed.adapter.FeedAdapter
import kz.diaspora.app.ui.my_adverts.MyAdvertType
import kz.diaspora.app.utils.EndlessRecyclerViewScrollListener
import kz.diaspora.app.utils.setDivider

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailFragment : Fragment() , CommentAdapter.OnCommentClickListener{
    private val TAG: String = this::class.java.simpleName

    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val adapter: CommentAdapter by lazy { CommentAdapter(arrayListOf(), this) }
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.item = args.model
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObservers()
        setListeners()
    }


    private fun initView() {
        (activity as AppCompatActivity).supportActionBar?.show()

        val mLayoutManager = LinearLayoutManager(context)
        binding.rvComment.layoutManager = mLayoutManager
        binding.rvComment.adapter = adapter

        scrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getDetailedPost(args.model)
            }
        }

        binding.rvComment.addOnScrollListener(scrollListener)
        viewModel.getDetailedPost(args.model)
    }

    private fun setObservers() {
        with(viewModel) {
            postData.observe(viewLifecycleOwner, {
                if (it.comments.isNotEmpty()) {
                    adapter.clear()
                    adapter.add(it.comments)
                }
            })
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun setListeners() {
//        binding.etSearch.addTextChangedListener {
//            addItemsByFilterSearch()
//        }
        binding.profilelayoutco.setOnClickListener {
            val action = DetailFragmentDirections.toProfileInfo(viewModel.postData.value?.post?.user_id.toString())
            findNavController().navigate(action)
        }
        binding.btnSendComment.setOnClickListener {
            viewModel.sendCommentToPost(args.model, binding.etComment.text.toString())
            binding.etComment.text.clear()
        }
        binding.ivLike.setOnClickListener {
            if (viewModel.postData.value?.post?.is_favourite == true) {
                binding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_like))
                viewModel.postData.value?.post?.id?.let { it1 -> onDisLikeClick(it1) }
            } else {
                binding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_like_active))
                viewModel.postData.value?.post?.id?.let { it1 -> onLikeClick(it1) }
            }
        }
    }

    override fun onCommentClick(commentModel: CommentModel) {
        val action = DetailFragmentDirections.toProfileInfo(commentModel.user_id.toString())
        findNavController().navigate(action)
    }

    private fun onLikeClick(post_id: Int) {
        viewModel.likePost(post_id)
    }

    private fun onDisLikeClick(post_id: Int) {
        viewModel.dislikePost(post_id)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}