package kz.diaspora.app.ui.notifications.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kz.diaspora.app.BR.item
import kz.diaspora.app.core.DataBindingViewHolder
import kz.diaspora.app.databinding.ItemFeedBinding
import kz.diaspora.app.databinding.ItemNotificationBinding
import kz.diaspora.app.domain.model.NotificationModel
import kz.diaspora.app.domain.model.PostModel

class NotificationsAdapter(
    private var items: ArrayList<NotificationModel> = arrayListOf(),
    private var listener: OnNotificationClickListener
) : RecyclerView.Adapter<NotificationsAdapter.SimpleHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    inner class SimpleHolder(private val dataBinding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun onBind(t: NotificationModel): Unit = with(t) {
            dataBinding.setVariable(item, t)
            dataBinding.bView.setOnClickListener {
                listener.onViewPostClick(t)
            }
            dataBinding.bAnswer.setOnClickListener {
                listener.onAnswerPostClick(t)
            }
            dataBinding.ivAvatar.setOnClickListener {
                listener.onAnswerPostClick(t)
            }
            dataBinding.tvAuthor.setOnClickListener {
                listener.onAnswerPostClick(t)
            }
        }
    }

    fun add(list: List<NotificationModel>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return items.isNullOrEmpty()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    interface OnNotificationClickListener {
        fun onViewPostClick(notificationModel: NotificationModel)
        fun onAnswerPostClick(notificationModel: NotificationModel)
    }
}