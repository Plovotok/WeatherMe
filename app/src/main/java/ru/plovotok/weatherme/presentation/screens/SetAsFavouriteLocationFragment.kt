package ru.plovotok.weatherme.presentation.screens

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

        val setText = resources.getString(R.string.set)
        val byDefaultText = resources.getString(R.string.by_default)
        binding.textView.text = "$setText ${location.name} $byDefaultText?"

        binding.agreeButton.button.text = resources.getString(R.string.yes)
        binding.agreeButton.button.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.agree_button_color))
        binding.agreeButton.button.setOnClickListener {
            dialogListener.onConfirm()
            dismiss()
        }

        binding.dismissButton.button.text = resources.getString(R.string.no)
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