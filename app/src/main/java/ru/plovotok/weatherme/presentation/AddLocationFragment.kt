package ru.plovotok.weatherme.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.databinding.FragmentAddLocationBinding
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.presentation.adapters.locations.LocationsAdapter
import ru.plovotok.weatherme.presentation.base.BaseFragment
import ru.plovotok.weatherme.presentation.base.UIState


class AddLocationFragment : BaseFragment<FragmentAddLocationBinding>(), LocationsAdapter.LocationItemClickListener {

    private var currentInputString = ""
//    private val viewModel : AddLocationViewModel by viewModels()
    private val viewModel : AddLocationViewModel by lazy {
        AddLocationViewModel((requireActivity().application as WeatherApplication).repository)
}

    private val serverLocationsAdapter = LocationsAdapter(this, LocationsAdapter.Type.SERVER_LOCATIONS)
    private val myLocationsAdapter = LocationsAdapter(this, LocationsAdapter.Type.MY_LOCATIONS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLocationsList()
        collectLocationList()
        collectMyLocations()


        with(binding.locationsRv) {
            adapter = serverLocationsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        with(binding.myLocationsRv) {
            adapter = myLocationsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        setToolbar(toolbar = binding.toolbar, title = "Добавить локацию", backButtonEnabled = true, addButtonEnabled = false, switchEnabled = false)
        binding.toolbar.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.addButton.visibility = View.GONE
        binding.toolbar.switch1.visibility = View.GONE

        val timer = object : CountDownTimer(REQUEST_INTERVAL_MILLIS, 100L) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                if (currentInputString.length > 2) {
                    if (currentInputString.hasCyrillic()) {
                        viewModel.getLocationListByQuery(query = currentInputString, lang = "ru")
                    } else {
                        viewModel.getLocationListByQuery(query = currentInputString, lang = "en")
                    }

                }
            }

        }


        binding.query.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                timer.cancel()
                currentInputString = s.toString()
                timer.start()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun String.hasCyrillic(): Boolean {
        for (char in this) {
            if (char.isLetter() && char.isUpperCase() && char in 'А'..'Я') {
                return true
            } else if (char.isLetter() && char.isLowerCase() && char in 'а'..'я') {
                return true
            }
        }
        return false
    }

    override fun getViewBinding(): FragmentAddLocationBinding {
        return FragmentAddLocationBinding.inflate(layoutInflater)
    }

    private fun collectLocationList() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.locationList.collect { state ->
            when(state) {
                is UIState.Success -> {
                    hideLoading()
                    if(!state.data.isNullOrEmpty()) {
                        binding.locNotFoundTv.visibility = View.GONE
                        binding.locationsRv.visibility = View.VISIBLE
                        serverLocationsAdapter.difLoadItems(items = state.data as List<LocationResponse>)
                    } else {
                        binding.locNotFoundTv.visibility = View.VISIBLE
                    }

                }
                is UIState.Loading -> showLoading()
                else -> {}
            }
        }
    }

    private fun collectMyLocations() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.myLocations.collect { state ->
            when(state) {
                is UIState.Success -> {
                    if (!state.data.isNullOrEmpty()) {
                        myLocationsAdapter.difLoadItems(state.data)
                    }
                }
                else -> {}
            }
        }
    }

    companion object {
        private const val REQUEST_INTERVAL_MILLIS = 700L
    }

    override fun onItemAdd(item: LocationResponse) {
        showToast("${item.name} добавлено")

        viewModel.addLocationToList(item)
        viewModel.getLocationsList()
    }

    override fun onItemRemove(item: LocationResponse) {
        viewModel.removeLocation(item)
        viewModel.getLocationsList()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemFavourite(item: LocationResponse) {

        viewLifecycleOwner.lifecycleScope.launch {
            while (binding.myLocationsRv.isComputingLayout) delay(100L)

        }

        val dialog = SetAsFavouriteLocationFragment(location = item, dialogListener = object : SetAsFavouriteLocationFragment.DialogListener {
            override fun onConfirm() {
                viewModel.setLocationAsFavourite(item)
                findNavController().popBackStack()
            }

            override fun onDismiss() {

            }

        })
        dialog.show(childFragmentManager, "SetAsFavouriteDialog")

    }

}