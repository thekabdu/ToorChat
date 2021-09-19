package kz.diaspora.app.ui.messages.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kz.diaspora.app.BR.item
import kz.diaspora.app.core.DataBindingViewHolder
import kz.diaspora.app.databinding.ItemMessageBinding
import kz.diaspora.app.domain.model.ChatModel
import kz.diaspora.app.domain.model.MessageModel

class MessagesAdapter(
    private var items: ArrayList<MessageModel> = arrayListOf(),
) : RecyclerView.Adapter<MessagesAdapter.SimpleHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val binding =
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    inner class SimpleHolder(dataBinding: ViewDataBinding) :
        DataBindingViewHolder<MessageModel>(dataBinding) {
        override fun onBind(t: MessageModel): Unit = with(t) {
            dataBinding.setVariable(item, t)
        }
    }

    fun add(list: List<MessageModel>) {
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

    interface OnMessageClickListener {
        fun onMessageClick(messageModel: MessageModel)
       /* fun onOptionsItemSelected(item: MenuItem, chatModel: ChatModel): Boolean*/
    }
}