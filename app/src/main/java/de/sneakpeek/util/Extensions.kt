package de.sneakpeek.util

import android.content.res.Resources
import android.support.annotation.AttrRes
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun Resources.Theme.resolveColor(@AttrRes colorID: Int) : Int {
    val typedValue = TypedValue()
    this.resolveAttribute(colorID, typedValue, true)
    return typedValue.data
}