package kz.diaspora.app.core

import androidx.databinding.ViewDataBinding


abstract class DataBindingViewHolder<T> (
    open val dataBinding: ViewDataBinding
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(dataBinding.root) {

    abstract fun onBind(t: T)
}

