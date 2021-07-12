package kz.diaspora.app.ui.feed.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.BR.item
import kz.diaspora.app.R
import kz.diaspora.app.core.App
import kz.diaspora.app.databinding.ItemFeedBinding
import kz.diaspora.app.domain.model.PostModel
import javax.inject.Inject

class FeedAdapter @Inject constructor(
    private var items: ArrayList<PostModel> = arrayListOf(),
    private var listener: OnFeedClickListener,
    private val context: Context
) : RecyclerView.Adapter<FeedAdapter.SimpleHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val binding =
            ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    inner class SimpleHolder(private val dataBinding: ItemFeedBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
        fun onBind(t: PostModel): Unit = with(t) {
            dataBinding.setVariable(item, t)
            val commentModel = t.commentModel
            if (commentModel != null) {
                dataBinding.tvAuthor2.text = commentModel.name + " " + (this.commentModel?.surname ?: "")
                dataBinding.tvComment.text = commentModel.comment
                Glide.with(context)
                    .load(commentModel.user_image)
                    .circleCrop()
                    .into(dataBinding.ivCommentAuthor)
            } else {
                dataBinding.tvAuthor2.visibility = View.GONE
                dataBinding.tvComment.visibility = View.GONE
                dataBinding.ivCommentAuthor.visibility = View.GONE
            }
            dataBinding.root.setOnClickListener {
                listener.onFeedClick(t)
            }
            dataBinding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, if (t.is_favourite) {
                R.drawable.ic_like_active
            } else {
                R.drawable.ic_like
            }))
            dataBinding.btnSendComment.setOnClickListener {
                listener.onSendClick(t, dataBinding.etComment.text.toString())
                dataBinding.etComment.text.clear()
            }
            dataBinding.ivLike.setOnClickListener {
                if (t.is_favourite) {
                    dataBinding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_like))
                    listener.onDisLikeClick(t.id)
                } else {
                    dataBinding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_like_active))
                    listener.onLikeClick(t.id)
                }

                items.find { it.id == t.id }?.is_favourite = !t.is_favourite
            }
        }
    }

    fun add(list: List<PostModel>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: PostModel) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun isEmpty() : Boolean {
        return items.isEmpty()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    interface OnFeedClickListener {
        fun onFeedClick(postModel: PostModel)
        fun onSendClick(postModel: PostModel, text: String)
        fun onLikeClick(post_id: Int)
        fun onDisLikeClick(post_id: Int)
    }
}