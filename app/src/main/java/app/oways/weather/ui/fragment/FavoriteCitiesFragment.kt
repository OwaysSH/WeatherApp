package app.oways.weather.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.oways.weather.R
import app.oways.weather.data.remote.DataState
import app.oways.weather.ui.adapter.local.CityLoadStateAdapter
import app.oways.weather.ui.adapter.local.SelectedCitiesAdapter
import app.oways.weather.utils.extentions.gone
import app.oways.weather.utils.extentions.visible
import app.oways.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.empty_list_layout.*
import kotlinx.android.synthetic.main.fragment_favorite_cities.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteCitiesFragment : Fragment(R.layout.fragment_favorite_cities) {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private var citiesAdapter: SelectedCitiesAdapter? = null
    private var recyclerListStatus: Bundle? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observers()
    }

    private fun initView() {

        back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        initRecyclerView()
    }

    private fun observers() {
        weatherViewModel.selectedDestinationsLiveData.observe(viewLifecycleOwner, {
            count_tv?.text = String.format(getString(R.string.selected_cities_count), it?.size?:0)
            if (it?.isNullOrEmpty() == true){
                empty_container?.visible()
            }else{
                empty_container?.gone()
            }
        })

        weatherViewModel.selectedCities.observe(viewLifecycleOwner, { dataState->
            when(dataState){
                is DataState.Success->{

                    lifecycleScope.launch {
                        dataState.data.collectLatest { pagingData ->
                            citiesAdapter?.submitData(pagingData)
                        }
                    }
                }
                is DataState.Empty ->{
                    empty_container?.visible()
                }
            }
        })

    }

    private fun initRecyclerView() {
        if (citiesAdapter == null) {

            citiesAdapter = SelectedCitiesAdapter({cityName ->
                findNavController().navigate(FavoriteCitiesFragmentDirections.actionFavoriteCitiesFragmentToHomeFragment(cityName))
            },{  cityId->
                weatherViewModel.deleteCityById(cityId)
            })
            cities_recycler_view?.apply {
                adapter = citiesAdapter?.withLoadStateHeaderAndFooter(
                    header = CityLoadStateAdapter(citiesAdapter!!::retry),
                    footer = CityLoadStateAdapter(citiesAdapter!!::retry)
                )
            }
            getDestinations()
        } else {
            restoreRecyclerState()
        }
    }

    private fun getDestinations() {
        weatherViewModel.getSelectedCities()
    }

    private fun restoreRecyclerState() {
        if (recyclerListStatus != null) {
            cities_recycler_view?.layoutManager?.onRestoreInstanceState(
                recyclerListStatus?.getParcelable(
                    "KEY_RECYCLER_STATE"
                )
            )
            cities_recycler_view?.apply {
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
                this.adapter = citiesAdapter?.withLoadStateHeaderAndFooter(
                    header = CityLoadStateAdapter(
                        citiesAdapter!!::retry
                    ),
                    footer = CityLoadStateAdapter(
                        citiesAdapter!!::retry
                    )
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        recyclerListStatus = Bundle()
        val listState = cities_recycler_view?.layoutManager?.onSaveInstanceState()
        recyclerListStatus?.putParcelable("KEY_RECYCLER_STATE", listState)

    }
}