package app.oways.weather.ui.adapter.local

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.oways.weather.R
import app.oways.weather.data.local.entity.CityEntity
import kotlinx.android.synthetic.main.city_list_item.view.*

class SelectedCitiesAdapter(private val onCityClick: (cityName: String) -> Unit, private val removeCityById: (id: Long) -> Unit) :
    PagingDataAdapter<CityEntity, SelectedCitiesAdapter.SelectedCityViewHolder>(
        COMPARATOR
    ) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CityEntity>() {
            override fun areItemsTheSame(oldItem: CityEntity, newItem: CityEntity) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CityEntity,
                newItem: CityEntity
            ) = oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: SelectedCityViewHolder, position: Int) {
        holder.bind(
            city = getItem(position),
            removeCityById,
            onCityClick
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SelectedCityViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.city_list_item, parent, false)
        )

    inner class SelectedCityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            city: CityEntity?,
            removeCityById: (id: Long) -> Unit,
            onCityClick: (cityName: String) -> Unit
        ) {
            with(itemView) {
                city?.apply {
                    city_card?.setOnClickListener {
                        city.name?.let { it1 -> onCityClick(it1) }
                    }
                    title_text_view?.text = "$name, $countryName"

                    picker_icon?.apply {
                        isSelected = true
                        setOnClickListener {
                            city.id?.let { id ->
                                removeCityById(id)
                            }
                        }
                    }
                }
            }
        }
    }

}