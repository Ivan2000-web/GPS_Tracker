package com.vansoft.gps_tracker.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.vansoft.gps_tracker.R
import com.vansoft.gps_tracker.utils.showToast

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var timePref: Preference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init(){
        timePref = findPreference("update_time_key")!!
        val changeListener = onChangeListener()
        timePref.onPreferenceChangeListener = changeListener
    }

    private fun onChangeListener() : OnPreferenceChangeListener{
        return OnPreferenceChangeListener{
            pref, value ->
            val nameArray = resources.getStringArray(R.array.loc_time_update_name)
            val valueArray = resources.getStringArray(R.array.loc_time_update_value)
            val title = pref.title.toString().substringBefore(":")
            pref.title = "$title: ${nameArray[valueArray.indexOf(value)]}"
            true
        }
    }
}