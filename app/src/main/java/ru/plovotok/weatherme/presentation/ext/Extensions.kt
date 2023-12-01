package ru.plovotok.weatherme.presentation.ext

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun String.hasCyrillic(): Boolean {
    for (char in this) {
        if (char.isLetter() && char.isUpperCase() && char in 'А'..'Я') {
            return true
        } else if (char.isLetter() && char.isLowerCase() && char in 'а'..'я') {
            return true
        }
    }
    return false
}

fun Fragment.openUrl(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    ContextCompat.startActivity(this.requireContext(), browserIntent, null)
}
