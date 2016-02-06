package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceFragment
import com.rayfantasy.icode.R
import de.psdev.licensesdialog.LicensesDialog

class SettingFragment : PreferenceFragment() {
    private lateinit var license: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        license = findPreference("licenses")
        license.onPreferenceClickListener = OnPreferenceClickListener {
            LicensesDialog.Builder(activity).setNotices(R.raw.notices).setIncludeOwnLicense(true).build().show()
            true
        }
    }
}
