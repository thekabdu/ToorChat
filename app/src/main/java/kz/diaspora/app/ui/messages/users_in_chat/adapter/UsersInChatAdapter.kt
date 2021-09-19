package kz.diaspora.app.ui.messages.users_in_chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kz.diaspora.app.BR
import kz.diaspora.app.R
import kz.diaspora.app.core.App
import kz.diaspora.app.core.DataBindingViewHolder
import kz.diaspora.app.databinding.ItemChatBinding
import kz.diaspora.app.databinding.ItemUsersInChatBinding
import kz.diaspora.app.domain.model.ChatModel
import kz.diaspora.app.domain.model.User

class UsersInChatAdapter (
    private var items: ArrayList<User> = arrayListOf(),
    private var listener: OnProjectClickListener
) : RecyclerView.Adapter<UsersInChatAdapter.SimpleHolder>() {

    override fun getItemCount(): Int = items.size
    private var context: Context? = null

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        context = parent.context
        val binding = ItemUsersInChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    inner class SimpleHolder(override val dataBinding: ItemUsersInChatBinding) :
        DataBindingViewHolder<User>(dataBinding) {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onBind(t: User): Unit = with(t) {
            dataBinding.setVariable(BR.item, t)
            dataBinding.btnEnter.setOnClickListener {
                    listener.onEnterClick(t, BR.item)
            }
        }
    }

    fun add(list: List<User>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    interface OnProjectClickListener {
        fun onEnterClick(user: User, position: Int)
    }
}