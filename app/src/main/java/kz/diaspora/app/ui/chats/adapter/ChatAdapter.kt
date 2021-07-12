package kz.diaspora.app.ui.chats.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kz.diaspora.app.core.DataBindingViewHolder
import kz.diaspora.app.databinding.ItemChatBinding
import kz.diaspora.app.domain.model.ChatModel
import kz.diaspora.app.BR.item
import kz.diaspora.app.R
import kz.diaspora.app.core.App

class ChatAdapter(
    private var items: ArrayList<ChatModel> = arrayListOf(),
    private var listener: OnProjectClickListener
) : RecyclerView.Adapter<ChatAdapter.SimpleHolder>() {

    override fun getItemCount(): Int = items.size
    private var context: Context? = null

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        context = parent.context
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    inner class SimpleHolder(override val dataBinding: ItemChatBinding) :
        DataBindingViewHolder<ChatModel>(dataBinding) {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onBind(t: ChatModel): Unit = with(t) {
            dataBinding.setVariable(item, t)
            dataBinding.btnEnter.setOnClickListener {
                if (t.is_run == true) {
                    listener.onEnterClick(t, item)
                }
                else listener.onJoinClick(t, item)
            }
            dataBinding.ivLike.setOnClickListener {
                if (t.is_liked) {
                    dataBinding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_like))
                } else {
                    dataBinding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_like_active))
                }
                listener.onLikeClick(t.id)
                items.find { it.id == t.id }?.is_liked = !t.is_liked
            }
        }
    }

    fun add(list: List<ChatModel>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun isEmpty() : Boolean {
        return items.isEmpty()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    interface OnProjectClickListener {
        fun onEnterClick(chatModel: ChatModel, position: Int)
        fun onJoinClick(chatModel: ChatModel, position: Int)
        fun onLikeClick(chat_id: Int)
    }
}