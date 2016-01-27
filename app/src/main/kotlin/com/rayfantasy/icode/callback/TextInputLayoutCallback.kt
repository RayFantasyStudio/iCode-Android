/*package com.rayfantasy.icode.callback

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.view.View

import eu.inmite.android.lib.validations.form.FormValidator
import eu.inmite.android.lib.validations.form.callback.SimpleCallback

class TextInputLayoutCallback @JvmOverloads constructor(context: Context, focusFirstFail: Boolean = true) : SimpleCallback(context, focusFirstFail) {

    override fun showValidationMessage(firstFail: FormValidator.ValidationFail) {
        (firstFail.view.parent as TextInputLayout).isErrorEnabled = true
        (firstFail.view.parent as TextInputLayout).error = firstFail.message
    }

    override fun showViewIsValid(passedValidations: Collection<View>) {
        for (v in passedValidations) {
            val til = v.parent as TextInputLayout
            til.isErrorEnabled = false
        }
    }
}*/
