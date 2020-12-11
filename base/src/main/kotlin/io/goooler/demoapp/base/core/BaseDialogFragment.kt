package io.goooler.demoapp.base.core

import androidx.fragment.app.DialogFragment

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseDialogFragment : DialogFragment() {

  var dismissListener: OnDismissListener? = null

  override fun dismiss() {
    dismissListener?.onDismiss()
    super.dismiss()
  }

  fun interface OnDismissListener {
    fun onDismiss()
  }
}
