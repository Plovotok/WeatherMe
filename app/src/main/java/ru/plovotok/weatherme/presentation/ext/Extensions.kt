package ru.plovotok.weatherme.presentation.ext

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
