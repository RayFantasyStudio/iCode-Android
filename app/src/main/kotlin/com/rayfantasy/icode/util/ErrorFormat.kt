package com.rayfantasy.icode.util

import android.app.Activity
import com.rayfantasy.icode.R

/**
 * Created by qweas on 2016/2/3 0003.
 */
fun error(operation: String, rc: Int, activity: Activity): String {

    return when (operation) {
        "insertCodeGood" -> when (rc) {
            -1 -> getStringResources(activity, R.string.internet_error)
            -2 -> getStringResources(activity, R.string.failed_to_analysis_local_data_error)
            -3 -> getStringResources(activity, R.string.failed_to_analysis_server_data_error)
            1 -> getStringResources(activity, R.string.server_unknow_exception_error)
            -4 -> getStringResources(activity, R.string.user_not_login_error)
            2 -> getStringResources(activity, R.string.cannot_insert_data_to_database_error)
            3 -> getStringResources(activity, R.string.check_user_failed_error)
            else -> getStringResources(activity, R.string.unknow_error)
        }
        "addReply" -> when (rc) {
            4 -> getStringResources(activity, R.string.check_user_failed_error)
            2 -> getStringResources(activity, R.string.cannot_insert_data_to_database_error)
            5 -> getStringResources(activity, R.string.sent_code_interval_too_short_error)
            -4 -> getStringResources(activity, R.string.user_not_login_error)
            3 -> getStringResources(activity, R.string.check_user_failed_error)
            else -> getStringResources(activity, R.string.unknow_error)
        }
        "registerUser" -> when (rc) {
            2 -> getStringResources(activity, R.string.cannot_insert_data_to_database_error)
            3 -> getStringResources(activity, R.string.check_user_failed_error)
            4 -> getStringResources(activity, R.string.pwd_too_short)
            5 -> getStringResources(activity, R.string.imei_illegal)
            6 -> getStringResources(activity, R.string.imei_already_existed)
            else -> getStringResources(activity, R.string.unknow_error)
        }
        "loginUser" -> when (rc) {
            2 -> getStringResources(activity, R.string.cannot_find_user)
            3 -> getStringResources(activity, R.string.pwd_error)
            else -> getStringResources(activity, R.string.unknow_error)
        }
        "delete" -> when (rc) {
            -4 -> getStringResources(activity, R.string.user_not_login_error)
            2 -> getStringResources(activity, R.string.cannot_find_id_in_reply_database_error)
            3 -> getStringResources(activity, R.string.check_user_failed_error)
            else -> getStringResources(activity, R.string.unknow_error)
        }
        "editCodeGood" -> when (rc) {
            -4 -> getStringResources(activity, R.string.user_not_login_error)
            -5 -> getStringResources(activity, R.string.id_is_empty_error)
            2 -> getStringResources(activity, R.string.cannot_find_id_in_reply_database_error)
            3 -> getStringResources(activity, R.string.check_user_failed_error)
            else -> getStringResources(activity, R.string.unknow_error)
        };

        else -> getStringResources(activity, R.string.unknow_error)
    }
}

fun getStringResources(activity: Activity, id: Int) = activity.resources.getString(id)