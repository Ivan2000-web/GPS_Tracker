package com.vansoft.gps_tracker.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import com.vansoft.gps_tracker.R
import com.vansoft.gps_tracker.databinding.SaveDialogBinding
import com.vansoft.gps_tracker.db.TrackItem

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

    fun showSaveDialog(context: Context, item: TrackItem?, listener: Listener){
        val builder = AlertDialog.Builder(context)
        val binding = SaveDialogBinding.inflate(LayoutInflater.from(context), null, false)
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.apply {
            val time = "Time: ${item?.time} s"
            val speed = "Speed: ${item?.averageSpeed} km/h"
            val distance = "Distance: ${item?.distance} m"
            tvTime.text = time
            tvSpeed.text = speed
            tvDistance.text = distance
            bSave.setOnClickListener(){
                dialog.dismiss()
                listener.onClick()
            }
            bCancel.setOnClickListener(){ dialog.dismiss() }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //делаем прозрачный фон у AlertDialog, т.к у нашего CardView не видны закруглённые края
        dialog.setCanceledOnTouchOutside(false) //запрещаем закрытие AlertDialog нажатием на область экрана
        dialog.show()
    }

    fun showStartDialog(context: Context, message: String){
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setMessage(message)
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "It's clear"){
                _, _, -> dialog.dismiss()
        }
        dialog.show()
    }

    interface Listener{
        fun onClick()
    }
}