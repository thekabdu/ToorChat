package kz.diaspora.app.utils

import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kz.diaspora.app.R
import kz.diaspora.app.domain.model.PostModel

@BindingAdapter("image_url")
fun loadImage(imageView: ImageView, url: String?) {
    Glide
        .with(imageView.context)
        .load(url)
        .placeholder(R.drawable.ic_image)
        .into(imageView)
}
//
//@BindingAdapter("image_url")
//fun loadImage(imageView: ImageView, postModel: PostModel) {
//    if (postModel.images.isNullOrEmpty()) {
//        Glide
//            .with(imageView.context)
//            .load(R.drawable.ic_image)
//            .into(imageView)
//    } else {
//        Glide
//            .with(imageView.context)
//            .load(postModel.images[0])
//            .placeholder(R.drawable.ic_image)
//            .into(imageView)
//    }
//}

@BindingAdapter("image_url_circle")
fun loadImageCircle(imageView: ImageView, avatar: String?) {
    if (avatar != null) {
        if (avatar.isEmpty()) {
            Glide
                .with(imageView.context)
                .load(R.drawable.ic_profile)
                .circleCrop()
                .into(imageView)
        } else {
            Glide
                .with(imageView.context)
                .load(avatar)
                .circleCrop()
                .placeholder(R.drawable.ic_profile)
                .into(imageView)
        }
    }
}

//@BindingAdapter("image_url_rounded_5")
//fun loadImageRounded5(imageView: ImageView, postModel: PostModel) {
//    if (!postModel.images.isNullOrEmpty()) {
//        Glide
//            .with(imageView.context)
//            .load(postModel.images[0])
//            .transform(CenterCrop(), RoundedCorners(Utils.dpToPx(5f).toInt()))
//            .into(imageView)
//    }
//}

@BindingAdapter("set_html")
fun setHtml(textView: TextView, text: String?) {
    if (text != null) {
        textView.text = Html.fromHtml(text)
    }
}

@BindingAdapter("is_refreshing")
fun isRefreshing(swipeRefreshLayout: SwipeRefreshLayout, boolean: Boolean) {
    swipeRefreshLayout.isRefreshing = boolean
}