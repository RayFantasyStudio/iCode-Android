package com.rayfantasy.icode.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceFragment
import android.view.View
import com.bumptech.glide.Glide
import com.raizlabs.android.dbflow.sql.language.Delete
import com.rayfantasy.icode.R
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.model.changeTheme
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.Reply
import com.rayfantasy.icode.postutil.bean.User
import de.psdev.licensesdialog.LicensesDialog
import org.evilbinary.utils.DirUtil
import org.jetbrains.anko.async
import org.jetbrains.anko.ctx
import java.io.File

class SettingFragment : PreferenceFragment() {
    companion object {
        const val PREF_HIGHLIGHT = "pref_highlight"
        const val PREF_CLEAR_CACHE = "pref_clear_cache"
        const val PREF_THEME = "pref_theme"
        const val DEFAULT_HIGHLIGHT = "molokai"
    }

    private lateinit var highlight: ListPreference
    private lateinit var theme: ListPreference
    private lateinit var sharedPreferences: SharedPreferences
    val highlightThemes by lazy {
        File(DirUtil.getFilesDir(ctx), "themes")
                .listFiles()
                .map { it.name.substringBeforeLast(".") }
                .sorted()
                .toTypedArray()
    }

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            PREF_HIGHLIGHT -> highlight.summary = sharedPreferences.getString(key, DEFAULT_HIGHLIGHT)
            PREF_THEME -> {
                theme.summary = theme.entry
                ctx.changeTheme(sharedPreferences.getString(PREF_THEME, ICodeTheme.THEME_DEFAULT.toString()).toInt())
            }
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        sharedPreferences = preferenceManager.sharedPreferences
        val license = findPreference("licenses")
        license.onPreferenceClickListener = OnPreferenceClickListener {
            LicensesDialog.Builder(activity).setNotices(R.raw.licenses).setIncludeOwnLicense(true).build().show()
            true
        }
        S_pref_ver.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        highlight = findPreference(PREF_HIGHLIGHT) as ListPreference
        highlight.entries = highlightThemes
        highlight.entryValues = highlightThemes
        val clearCache = findPreference(PREF_CLEAR_CACHE)
        clearCache.setOnPreferenceClickListener {
            async() {
                Glide.get(ctx).clearDiskCache()
                Delete.table(User::class.java)
                Delete.table(CodeGood::class.java)
                Delete.table(Reply::class.java)
            }
            true
        }
        theme = findPreference(PREF_THEME) as ListPreference
    }

    override fun onResume() {
        super.onResume()
        highlight.summary = sharedPreferences.getString(PREF_HIGHLIGHT, DEFAULT_HIGHLIGHT)
        theme.summary = theme.entry
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(changeListener)
    }
}
