package com.rayfantasy.icode.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceFragment
import android.view.View
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.extension.i
import de.psdev.licensesdialog.LicensesDialog
import org.evilbinary.utils.DirUtil
import org.jetbrains.anko.ctx
import java.io.File

class SettingFragment : PreferenceFragment() {
    companion object {
        const val PREF_HIGHLIGHT = "pref_highlight"
        const val DEFAULT_HIGHLIGHT = "molokai"
    }

    private lateinit var license: Preference
    private lateinit var highlight: ListPreference
    private lateinit var sharedPreferences: SharedPreferences
    val highlightThemes by lazy {
        File(DirUtil.getFilesDir(ctx), "themes")
                .listFiles()
                .map { it.name.substringBeforeLast(".") }
                .sorted()
                .toTypedArray()
    }
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        i("key = $key")
        when (key) {
            PREF_HIGHLIGHT -> {
                highlight.summary = sharedPreferences.getString(key, DEFAULT_HIGHLIGHT)
                i("highlight.summary = ${highlight.summary}")
            }
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        sharedPreferences = preferenceManager.sharedPreferences
        license = findPreference("licenses")
        license.onPreferenceClickListener = OnPreferenceClickListener {
            LicensesDialog.Builder(activity).setNotices(R.raw.notices).setIncludeOwnLicense(true).build().show()
            true
        }
        highlight = findPreference(PREF_HIGHLIGHT) as ListPreference
        highlight.entries = highlightThemes
        highlight.entryValues = highlightThemes
    }

    override fun onResume() {
        super.onResume()
        highlight.summary = sharedPreferences.getString(PREF_HIGHLIGHT, DEFAULT_HIGHLIGHT)
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(changeListener)
    }
}
