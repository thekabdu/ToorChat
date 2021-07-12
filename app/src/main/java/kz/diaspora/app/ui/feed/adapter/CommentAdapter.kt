package kz.diaspora.app.ui.feed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.diaspora.app.BR.item
import kz.diaspora.app.databinding.ItemCommentBinding
import kz.diaspora.app.domain.model.CommentModel


class CommentAdapter(
    private var items: ArrayList<CommentModel> = arrayListOf(),
    private var listener: OnCommentClickListener
) : RecyclerView.Adapter<CommentAdapter.SimpleHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val binding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    inner class SimpleHolder(private val dataBinding: ItemCommentBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun onBind(t: CommentModel): Unit = with(t) {
            dataBinding.setVariable(item, t)

            dataBinding.root.setOnClickListener {
                listener.onCommentClick(t)
            }
        }
    }

    fun add(list: List<CommentModel>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: CommentModel) {
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

    interface OnCommentClickListener {
        fun onCommentClick(commentModel: CommentModel)
    }
}