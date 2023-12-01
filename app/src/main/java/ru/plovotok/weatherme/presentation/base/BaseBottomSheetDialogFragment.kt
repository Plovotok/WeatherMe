package ru.plovotok.weatherme.presentation.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.R

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding>(
    private val peekHeight: Int = 200,
    private val state: Int = BottomSheetBehavior.STATE_EXPANDED,
    private val skipCollapsed: Boolean = true,
    private val isDraggable: Boolean = true,
    private val useDialogStyle: Boolean = false,
    private val fullHeight: Boolean = false
) : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    abstract fun getViewBinding(): VB

    private var behavior: BottomSheetBehavior<FrameLayout>? = null
    fun getBehavior() = behavior

    fun runInRepeatScope(
        state: Lifecycle.State,
        block: suspend CoroutineScope.() -> Unit
    ) {
        lifecycleScope.launch { repeatOnLifecycle(state) { block() } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            behavior = BottomSheetBehavior.from(bottomSheet!!)

            behavior?.also {
                it.peekHeight = peekHeight
                it.state = state
                it.skipCollapsed = skipCollapsed
                it.isDraggable = isDraggable
            }

//            if (fullHeight)
//                setupFullHeight(bottomSheetDialog)
        }

        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val layoutParams = bottomSheet!!.layoutParams
        val windowHeight = getWindowHeight() - (requireView().getBottomInset())
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        getBehavior()?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun View.getInsets(): WindowInsets? {
        return this.rootWindowInsets
    }

    private fun View.getBottomInset(): Int = this.getInsets()?.systemWindowInsetBottom ?: 0

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

//    private fun setNavigationBarColor(dialog: Dialog) {
//        val window: Window? = dialog.window
//
//        if (window != null) {
//            val metrics = DisplayMetrics()
//            val dimDrawable = GradientDrawable()
//            val navigationBarDrawable = GradientDrawable()
//            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)
//            val windowBackground = LayerDrawable(layers)
//
//            window.windowManager.defaultDisplay.getMetrics(metrics)
////            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
////            navigationBarDrawable.setColor(ContextCompat.getColor(requireContext(), R.color.light_blue_400))
//
//            windowBackground.setLayerInsetTop(1, metrics.heightPixels)
//            window.setBackgroundDrawable(windowBackground)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}