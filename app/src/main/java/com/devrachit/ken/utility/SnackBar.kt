package com.devrachit.ken.utility


import android.content.Context
import android.view.View
import com.devrachit.ken.R
import com.google.android.material.snackbar.Snackbar

class SnackBar(private val context: Context) {


    fun showErrorSnack(view: View, content: String) {
        Snackbar.make(view, content, Snackbar.LENGTH_LONG)
            .setBackgroundTint(context.resources.getColor(R.color.bg_danger_normal_default))
            .setTextColor(context.resources.getColor(R.color.content_neutral_primary_black))
            .show()
    }

    fun showSuccessSnack(view: View, content: String) {
        Snackbar.make(view, content, Snackbar.LENGTH_LONG)
            .setBackgroundTint(context.resources.getColor(R.color.bg_success_normal_default))
            .setTextColor(context.resources.getColor(R.color.content_neutral_primary_black))
            .show()
    }

    fun showInfoSnack(view: View, content: String) {
        Snackbar.make(view, content, Snackbar.LENGTH_LONG)
            .setBackgroundTint(context.resources.getColor(R.color.bg_accent_normal_default))
            .setTextColor(context.resources.getColor(R.color.content_neutral_primary_black))
            .show()
    }
}