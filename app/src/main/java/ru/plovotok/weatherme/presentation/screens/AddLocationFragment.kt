package ru.plovotok.weatherme.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.databinding.FragmentAddLocationBinding
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.presentation.adapters.locations.LocationsAdapter
import ru.plovotok.weatherme.presentation.base.BaseFragment
import ru.plovotok.weatherme.presentation.base.UIState
import ru.plovotok.weatherme.presentation.ext.hasCyrillic


@AndroidEntryPoint
class AddLocationFragment : BaseFragment<FragmentAddLocationBinding>(), LocationsAdapter.LocationItemClickListener {

    private var currentInputString = ""
//    private val viewModel : AddLocationViewModel by viewModels()
    private val viewModel : AddLocationViewModel by viewModels()

    private val serverLocationsAdapter = LocationsAdapter(this, LocationsAdapter.Type.SERVER_LOCATIONS)
    private val myLocationsAdapter = LocationsAdapter(this, LocationsAdapter.Type.MY_LOCATIONS)
//    private val myLocationsAdapter = TestAdapter()
//    /private val myLocationsAdapter = NewTestAdapter()

    private var isNowEditing: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLocationsList()
        collectLocationList()
        collectMyLocations()

        binding.deleteBtn.setOnClickListener {
            viewModel.removeActiveListLocations()
            myLocationsAdapter.finishEditing()
        }

        with(binding.locationsRv) {
            adapter = serverLocationsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        with(binding.myLocationsRv) {
            adapter = myLocationsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            invalidateItemDecorations()
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
//                        myLocationsAdapter.loadItems(state.data)
//                        myLocationsAdapter.submitList(state.data.toMutableList())
//                        myLocationsAdapter.notifyDataSetChanged()
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
        showSnack("${item.name} добавлено")

        viewModel.addLocation(item)
    }

    override fun onItemRemove(item: LocationResponse) {
        showSnack("${item.name} удалено")

        viewModel.removeLocation(item)
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

    override fun onListEditing(isEditing: Boolean) {
        when(isEditing) {
            true -> {
                isNowEditing = true
                binding.deleteBtn.visibility = View.VISIBLE
                binding.deleteBtn.animate()
                    .translationY(0f)
                    .setDuration(400L)
                    .start()
            }
            false -> {
                isNowEditing = false
                val bottomMargin = (binding.deleteBtn.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin.toFloat()

                binding.deleteBtn.animate()
                    .translationY(binding.deleteBtn.height + bottomMargin)
                    .setDuration(400L)
                    .withEndAction {
                        binding.deleteBtn.visibility = View.GONE
                    }
                    .start()
            }
        }

        if (!isNowEditing) {
            viewModel.clearEditingList()
        }

    }

    override fun onItemAddToDeleteList(item: LocationResponse) {
        viewModel.addToEditingList(item)
    }

    override fun onItemRemoveFromDeleteList(item: LocationResponse) {
        viewModel.removeFromEditingList(item)
    }

}