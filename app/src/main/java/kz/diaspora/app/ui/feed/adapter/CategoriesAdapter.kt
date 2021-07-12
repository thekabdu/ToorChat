package kz.diaspora.app.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kz.diaspora.app.core.DataBindingViewHolder
import kz.diaspora.app.databinding.ItemCategoryBinding
import kz.diaspora.app.domain.model.CategoryList
import kz.diaspora.app.domain.model.CategoryModel
import kz.diaspora.app.BR.item

class CategoriesAdapter(
    private var items: ArrayList<CategoryModel> = arrayListOf(),
    private var listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoriesAdapter.SimpleHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleHolder(binding)
    }

    inner class SimpleHolder(dataBinding: ViewDataBinding) :
        DataBindingViewHolder<CategoryModel>(dataBinding) {
        override fun onBind(t: CategoryModel): Unit = with(t) {
            dataBinding.setVariable(item, t)
            dataBinding.root.setOnClickListener {
                listener.onCategoryClick(t)
            }
        }
    }

    fun add(list: CategoryList) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun isEmpty() : Boolean {
        return items.isNullOrEmpty()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    interface OnCategoryClickListener {
        fun onCategoryClick(categoryModel: CategoryModel)
    }
}