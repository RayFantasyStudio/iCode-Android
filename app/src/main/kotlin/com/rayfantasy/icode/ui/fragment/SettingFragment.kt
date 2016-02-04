package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener

import com.rayfantasy.icode.R

import de.psdev.licensesdialog.LicensesDialog

class SettingFragment : PreferenceFragment() {

    private lateinit var license: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*addPreferencesFromResource(R.xml.settings)*/
        license = findPreference("licenses") as Preference
        license.onPreferenceClickListener = OnPreferenceClickListener {
            val PreKey = "licenses"
            if (PreKey.equals(license)) {
                LicensesDialog.Builder(activity).setNotices(R.raw.notices).setIncludeOwnLicense(true).build().show()
            }
            false
        }
    }
    /* class PreferenceClick {
         fun onPreferenceClick(preference: Preference): Boolean {
             String P = license.getKey().toString
             if (preference === license) {
                 LicensesDialog.Builder(getAtNotices(R.raw.notices).setIncludeOwnLicense(true).build().show())
             }
             return true
         }
     }*/
}
