package ru.plovotok.weatherme.presentation

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.databinding.FragmentAddLocationBinding
import ru.plovotok.weatherme.presentation.adapters.LocationsAdapter
import ru.plovotok.weatherme.presentation.base.BaseFragment
import ru.plovotok.weatherme.presentation.base.UIState


class AddLocationFragment : BaseFragment<FragmentAddLocationBinding>(), LocationsAdapter.LocationItemClickListener {

    private val viewModel : AddLocationViewModel by viewModels()
    private var currentInputString = ""

    private val locationsAdapter = LocationsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLocationList()

        with(binding.locationsRv) {
            adapter = locationsAdapter
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
                        locationsAdapter.loadItems(items = state.data as List<LocationResponseDTO>)
                        locationsAdapter.notifyDataSetChanged()
                    } else {
                        showToast("Не найдено")
                    }

                }
                is UIState.Loading -> showLoading()
                else -> {}
            }
        }
    }

    companion object {
        private const val REQUEST_INTERVAL_MILLIS = 700L
    }

    override fun onItemClick(item: LocationResponseDTO) {
        showToast("${item.name} добавлено")
    }

}