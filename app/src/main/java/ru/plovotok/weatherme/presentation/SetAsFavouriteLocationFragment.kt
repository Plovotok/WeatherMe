package ru.plovotok.weatherme.presentation

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.FragmentSetAsFavouriteLocationBinding
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.presentation.base.BaseDialogFragment

@AndroidEntryPoint
class SetAsFavouriteLocationFragment(private val location : LocationResponse, private val dialogListener : DialogListener) : BaseDialogFragment<FragmentSetAsFavouriteLocationBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView.text = "Set ${location.name} as favourite location?"

        binding.agreeButton.button.text = "Yes"
        binding.agreeButton.button.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.agree_button_color))
        binding.agreeButton.button.setOnClickListener {
            dialogListener.onConfirm()
            dismiss()
        }

        binding.dismissButton.button.text = "No"
        binding.dismissButton.button.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.disagree_button_color))
        binding.dismissButton.button.setOnClickListener {
            dialogListener.onDismiss()
            dismiss()
        }
    }

    override fun getViewBinding(): FragmentSetAsFavouriteLocationBinding {
        return FragmentSetAsFavouriteLocationBinding.inflate(layoutInflater)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    interface DialogListener {
        fun onConfirm()

        fun onDismiss()
    }

}