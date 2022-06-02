package app.oways.weather.utils.extentions

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

fun AppCompatImageView.load(
    url: String?,
    placeholder: Int
) {
    Glide.with(this.context)
        .load(url)
        .placeholder(placeholder)
        .into(this)
}
