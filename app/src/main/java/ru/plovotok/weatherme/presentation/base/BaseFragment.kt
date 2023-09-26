package ru.plovotok.weatherme.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.presentation.WeatherActivity

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    fun isBindingReady() = _binding != null

    abstract fun getViewBinding(): VB


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()

        return binding.root
    }

    fun runInRepeatScope(
        state: Lifecycle.State,
        block: suspend CoroutineScope.() -> Unit
    ) {
        lifecycleScope.launch { repeatOnLifecycle(state) { block() } }
    }

    fun showLoading(view: CoordinatorLayout = binding.root as CoordinatorLayout) {
        (requireActivity() as WeatherActivity).showLoading(view)
    }

    fun hideLoading(view: CoordinatorLayout = binding.root as CoordinatorLayout) {
        (requireActivity() as WeatherActivity).hideLoading(view)
    }

    fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}