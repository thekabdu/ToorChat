package kz.diaspora.app.ui.my_adverts.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.diaspora.app.BR.item
import kz.diaspora.app.databinding.ItemMyAdvertBinding
import kz.diaspora.app.domain.model.PostModel
import kz.diaspora.app.ui.my_adverts.MyAdvertType

class MyAdvertsAdapter(
    private var items: ArrayList<PostModel> = arrayListOf(),
    private val type: MyAdvertType,
    private var listener: OnAdvertClickListener
) : RecyclerView.Adapter<MyAdvertsAdapter.SimpleHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val binding =
            ItemMyAdvertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    inner class SimpleHolder(private val dataBinding: ItemMyAdvertBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun onBind(t: PostModel): Unit = with(t) {
            dataBinding.setVariable(item, t)

            when (type) {
                MyAdvertType.Favorite -> {
                    dataBinding.btnActivate.visibility = View.GONE
                    dataBinding.btnEdit.visibility = View.GONE
                    dataBinding.btnDeactivate.visibility = View.GONE
                    dataBinding.btnEditTwo.visibility = View.GONE
                    dataBinding.btnEditThree.visibility = View.GONE
                    dataBinding.btnView.visibility = View.VISIBLE
                }
                MyAdvertType.NotActive -> {
                    dataBinding.btnActivate.visibility = View.VISIBLE
                    dataBinding.btnEdit.visibility = View.VISIBLE
                    dataBinding.btnDeactivate.visibility = View.GONE
                    dataBinding.btnEditTwo.visibility = View.GONE
                    dataBinding.btnEditThree.visibility = View.GONE
                    dataBinding.btnView.visibility = View.GONE
                }
                MyAdvertType.Active -> {
                    dataBinding.btnActivate.visibility = View.GONE
                    dataBinding.btnEdit.visibility = View.GONE
                    dataBinding.btnDeactivate.visibility = View.VISIBLE
                    dataBinding.btnEditTwo.visibility = View.VISIBLE
                    dataBinding.btnEditThree.visibility = View.GONE
                    dataBinding.btnView.visibility = View.GONE
                }
                MyAdvertType.Pending -> {
                    dataBinding.btnActivate.visibility = View.GONE
                    dataBinding.btnEdit.visibility = View.GONE
                    dataBinding.btnDeactivate.visibility = View.GONE
                    dataBinding.btnEditTwo.visibility = View.GONE
                    dataBinding.btnEditThree.visibility = View.VISIBLE
                    dataBinding.btnView.visibility = View.GONE
                }
            }

            dataBinding.btnEdit.setOnClickListener {
                listener.onEditClick(t)
            }
            dataBinding.btnEditTwo.setOnClickListener {
                listener.onEditClick(t)
            }
            dataBinding.btnEditThree.setOnClickListener {
                listener.onEditClick(t)
            }
            dataBinding.btnActivate.setOnClickListener {
                listener.onActivateClick(t)
            }
            dataBinding.btnDeactivate.setOnClickListener {
                listener.onDeactivateClick(t)
            }
            dataBinding.btnView.setOnClickListener {
                listener.onViewClick(t)
            }
        }
    }

    fun add(list: List<PostModel>) {
        items.addAll(list)
        Log.d("AdvertsAdapter", list.toString())
        notifyDataSetChanged()
    }

    fun isEmpty() : Boolean {
        return items.isEmpty()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    interface OnAdvertClickListener {
        fun onActivateClick(postModel: PostModel)
        fun onDeactivateClick(postModel: PostModel)
        fun onEditClick(postModel: PostModel)
        fun onViewClick(postModel: PostModel)
    }
}