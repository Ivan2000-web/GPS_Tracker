package com.vansoft.gps_tracker.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.vansoft.gps_tracker.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
    }
}