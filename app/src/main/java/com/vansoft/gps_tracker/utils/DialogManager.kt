package com.vansoft.gps_tracker.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.vansoft.gps_tracker.R

object DialogManager {
    fun showLocEnableDialog(context: Context, listener: Listener){
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle(R.string.location_disabled)
        dialog.setMessage(context.getString(R.string.location_dialog_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes"){
            _, _, -> listener.onClick()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No"){
                _, _, -> dialog.dismiss()
        }
        dialog.show()
    }

    interface Listener{
        fun onClick()
    }
}