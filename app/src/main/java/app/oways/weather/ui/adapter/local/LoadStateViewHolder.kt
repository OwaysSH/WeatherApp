package app.oways.weather.ui.adapter.local

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.oways.weather.R
import app.oways.weather.utils.extentions.gone
import app.oways.weather.utils.extentions.visible
import kotlinx.android.synthetic.main.list_item_load_state.view.*

sealed class LoadStateViewHolder(
    val parent: ViewGroup,
    val retry: () -> Unit
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_load_state, parent, false)) {

    fun bind() {
        when (this) {
            is Error -> {
                itemView.progress_bar?.gone()
            }
            is Loading -> {
                retry()
                itemView.apply {
                    progress_bar?.visible()
                }
            }
            else -> {
                itemView.apply {
                    progress_bar?.gone()
                }
            }
        }
    }

    class Loading(parent: ViewGroup, retry: () -> Unit) : LoadStateViewHolder(parent, retry)
    class NotLoading(parent: ViewGroup, retry: () -> Unit) : LoadStateViewHolder(parent, retry)
    class Error(parent: ViewGroup, retry: () -> Unit) : LoadStateViewHolder(parent, retry)

}