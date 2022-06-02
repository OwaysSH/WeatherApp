package app.oways.weather.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import app.oways.weather.R
import app.oways.weather.data.local.doa.mapper.CityMapper
import app.oways.weather.data.remote.DataState
import app.oways.weather.data.remote.ForecastResponse
import app.oways.weather.data.remote.WeatherResponse
import app.oways.weather.ui.adapter.remote.ForecastAdapter
import app.oways.weather.utils.other.DateUtility
import app.oways.weather.utils.extentions.gone
import app.oways.weather.utils.extentions.load
import app.oways.weather.utils.extentions.visible
import app.oways.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.empty_result_layout.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_search_bar.*


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), TextWatcher {

    private val homeFragmentArgs: HomeFragmentArgs by navArgs()

    private var searchValue: String? = null

    private val weatherViewModel: WeatherViewModel by viewModels()
    private var forecastAdapter: ForecastAdapter? = null
    private var recyclerListStatus: Bundle? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFragmentExtras()
        initSearchView()
        if (forecastAdapter == null || homeFragmentArgs.selectedCity.isNullOrEmpty().not()) {
            initView()
        } else {
            restoreRecyclerState()
        }
        observers()

        searchValue?.let {
            weatherViewModel.getCityWeather(it)
            weatherViewModel.getCityForecast(it)
        }?:showEmptyViewInfo(true)
    }

    private fun getFragmentExtras() {
        searchValue = homeFragmentArgs.selectedCity
    }

    private fun observers() {
        weatherViewModel.cityWeather.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<WeatherResponse?> -> {
                    dataState.data?.let { weatherInfo ->
                        showEmptyViewInfo()
                        setCurrentCityWeatherInfo(weatherInfo)
                    } ?: showEmptyViewInfo(true)

                }
                is DataState.Empty -> {
                    showEmptyViewInfo(true)
                }
                is DataState.GenericError -> {
                    showEmptyViewInfo(true)
                }
                is DataState.NetworkError -> {
                    showEmptyViewInfo(true)

                }
            }
        })

        weatherViewModel.cityForecast.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<ForecastResponse?> -> {
                    dataState.data?.list?.let { list ->
                        forecastAdapter?.addTempType(weatherViewModel.getTempType())
                        forecastAdapter?.addList(list)
                        forecast_recycler_view?.visible()
                    }?:forecast_recycler_view?.gone()
                }
                is DataState.GenericError -> {
                    forecast_recycler_view?.gone()
                }
                is DataState.NetworkError -> {
                    forecast_recycler_view?.gone()
                }
            }
        })
    }

    private fun initView() {
        initAdapter()
        setRecyclerViewAdapter()

    }

    private fun initSearchView() {

        temp_type_tv?.text = String.format(getString(R.string.temp_type), weatherViewModel.getTempType())
        temp_type_tv?.setOnClickListener {
            temp_type_tv.text =  String.format(getString(R.string.temp_type), weatherViewModel.switchTempType())
            searchValue?.let { it1 ->
                weatherViewModel.getCityWeather(it1)
                weatherViewModel.getCityForecast(it1)
            }
        }
        search_edit_text?.addTextChangedListener(this)
        search_edit_text?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchValue?.let { it1 ->
                    weatherViewModel.getCityWeather(it1)
                    weatherViewModel.getCityForecast(it1)
                }
                true
            }
            false
        }
        search_btn?.setOnClickListener {
            searchValue?.let { it1 ->
                weatherViewModel.getCityWeather(it1)
                weatherViewModel.getCityForecast(it1)
            }
        }

        fav_list_image_view?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteCitiesFragment())
        }
    }

    private fun setCurrentCityWeatherInfo(weatherInfo: WeatherResponse) {

        temp_tv?.text =
            String.format(getString(R.string.temp_value), weatherInfo.mainTemp?.temp?.toUInt(),  weatherViewModel.getTempType())
        weather_desc_tv?.text = weatherInfo.weather?.get(0)?.description
        current_city_name_tv?.text = "${weatherInfo.cityName}"
        current_date_tv?.text = "${
            DateUtility.getCurrentDate(
                weatherInfo.dateTime,
                weatherInfo.timezone
            )
        }"
        feels_like_temp_tv?.text = String.format(
            getString(R.string.feels_like),
            weatherInfo.mainTemp?.feels_like?.toUInt()
        )
        val iconUrl = "http://openweathermap.org/img/w/${weatherInfo.weather?.get(0)?.icon}.png"
        max_min_temp_tv?.text = String.format(
            getString(R.string.max_min_temp),
            weatherInfo.mainTemp?.temp_max?.toUInt(),
            weatherInfo.mainTemp?.temp_min?.toUInt()
        )
        weather_icon_imageview?.load(iconUrl, R.drawable.ic_sunny_placeholder)

        picker_icon?.apply {
            isSelected = weatherInfo.isSelectedCity ?: false
            setOnClickListener {
                if (weatherInfo.isSelectedCity == true) {
                    weatherInfo.cityId?.let { it1 ->
                        weatherViewModel.deleteCityById(it1)
                        picker_icon?.isSelected = false
                    }
                } else {
                    weatherViewModel.addCity(CityMapper.transform(weatherInfo))
                    picker_icon?.isSelected = true

                }
            }
        }
    }

    private fun showEmptyViewInfo(show: Boolean? = false) {
        if (show == true) {
            empty_container?.visible()
        } else {
            empty_container?.gone()

        }

    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        if (editable != null || editable.toString().isEmpty().not()) {
            searchValue = editable.toString().trim()
        }
    }

    private fun setRecyclerViewAdapter() {

    }

    private fun initAdapter() {
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        forecast_recycler_view?.layoutManager = manager

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = (displayMetrics.widthPixels / 2.5).toInt()
        forecastAdapter = ForecastAdapter(width, weatherViewModel.getTempType())
        forecast_recycler_view?.apply {
            this.adapter = forecastAdapter
        }
    }

    private fun restoreRecyclerState() {
        if (recyclerListStatus != null) {
            forecast_recycler_view?.layoutManager?.onRestoreInstanceState(
                recyclerListStatus?.getParcelable(
                    "KEY_RECYCLER_STATE"
                )
            )
            forecast_recycler_view?.apply {
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        recyclerListStatus = Bundle()
        val listState = forecast_recycler_view?.layoutManager?.onSaveInstanceState()
        recyclerListStatus?.putParcelable("KEY_RECYCLER_STATE", listState)
    }
}