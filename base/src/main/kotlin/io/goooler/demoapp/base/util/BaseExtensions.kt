@file:Suppress("unused")
@file:JvmName("BaseExtensionUtil")
@file:OptIn(ExperimentalContracts::class)

package io.goooler.demoapp.base.util

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import android.text.Spannable
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.MainThread
import androidx.core.content.getSystemService
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.os.bundleOf
import androidx.core.text.parseAsHtml
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import java.io.File
import java.io.Serializable
import java.lang.reflect.Method
import java.math.BigDecimal
import java.util.UUID
import java.util.regex.Pattern
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

// ---------------------Types-------------------------------//

@Dimension(unit = Dimension.DP)
annotation class Dp

@Dimension(unit = Dimension.SP)
annotation class Sp

@Dimension(unit = 3)
annotation class Pt

typealias ParamMap = HashMap<String, Any>

// ---------------------Any-------------------------------//

inline val randomUUID: String get() = UUID.randomUUID().toString()

inline val currentTimeMillis: Long get() = System.currentTimeMillis()

inline val currentThreadName: String get() = Thread.currentThread().name

inline val isMainThread: Boolean get() = Looper.getMainLooper() == Looper.myLooper()

fun <T : Any> unsafeLazy(initializer: () -> T): Lazy<T> =
  lazy(LazyThreadSafetyMode.NONE, initializer)

fun <T : Parcelable> T.deepCopy(): T? {
  var parcel: Parcel? = null
  return try {
    parcel = Parcel.obtain().also {
      it.writeParcelable(this, 0)
      it.setDataPosition(0)
    }
    parcel.readParcelable(this::class.java.classLoader)
  } finally {
    parcel?.recycle()
  }
}

@Throws(ReflectiveOperationException::class)
fun lazyReflectedMethod(
  declaringClass: Class<*>,
  methodName: String,
  vararg parameterTypes: Any,
): Lazy<Method> = lazy {
  getReflectedMethod(declaringClass, methodName, *getParameterTypes(parameterTypes))
}

@Throws(ReflectiveOperationException::class)
fun getParameterTypes(parameterTypes: Array<out Any>): Array<Class<*>> =
  Array(parameterTypes.size) {
    when (val parameterType = parameterTypes[it]) {
      is Class<*> -> parameterType
      is String -> Class.forName(parameterType)
      else -> throw IllegalArgumentException(parameterType.toString())
    }
  }

@Throws(ReflectiveOperationException::class)
fun getReflectedMethod(
  declaringClass: Class<*>,
  methodName: String,
  vararg parameterTypes: Class<*>,
): Method =
  declaringClass.getDeclaredMethod(methodName, *parameterTypes).also { it.isAccessible = true }

// ---------------------CharSequence-------------------------------//

operator fun String.times(@IntRange(from = 0) num: Int): String {
  require(num >= 0) {
    "Param num should >= 0"
  }
  val origin = this
  return buildString {
    for (i in 1..num) append(origin)
  }
}

fun String.fromHtml(): Spanned = parseAsHtml()

fun String.extension2MimeType(): String? =
  MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowercase())

fun String.mimeType2Extension(): String? =
  MimeTypeMap.getSingleton().getExtensionFromMimeType(lowercase())

fun String.onlyDigits(): String = replace(Regex("\\D*"), "")

fun String.removeAllSpecialCharacters(): String = replace(Regex("[^a-zA-Z]+"), "")

/**
 * Validate given text is a valid filename.
 *
 * @return true if given text is a valid filename
 */
fun String.isValidFilename(): Boolean {
  val filenameRegex =
    Pattern.compile("[\\\\\\/:\\*\\?\"<>\\|\\x01-\\x1F\\x7F]", Pattern.CASE_INSENSITIVE)

  // It's not easy to use regex to detect single/double dot while leaving valid values
  // (filename.zip) behind...
  // So we simply use equality to check them
  return !filenameRegex.matcher(this).find() && "." != this && ".." != this
}

fun Uri.withAppendedId(id: Long): Uri = ContentUris.withAppendedId(this, id)

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotNullOrEmpty(): Boolean {
  contract {
    returns(true) implies (this@isNotNullOrEmpty != null)
  }
  return this.isNullOrEmpty().not()
}

fun Spannable.withClickableSpan(clickablePart: String, onClickListener: () -> Unit): Spannable {
  val clickableSpan = object : ClickableSpan() {
    override fun onClick(widget: View) = onClickListener()
  }
  setSpan(
    clickableSpan,
    indexOf(clickablePart),
    indexOf(clickablePart) + clickablePart.length,
    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
  )
  return this
}

fun CharSequence.withColorSpan(coloredPart: String, @ColorInt color: Int): Spannable {
  return toSpannable().also {
    it.setSpan(
      ForegroundColorSpan(color),
      it.length - coloredPart.length,
      it.length,
      Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
    )
  }
}

/**
 * subString 防越界处理
 */
fun String.safeSubstring(startIndex: Int, endIndex: Int): String {
  val begin = if (startIndex < 0) 0 else startIndex
  val end = if (endIndex > length) length else endIndex
  return substring(begin, end)
}

fun String?.safeToBoolean(default: Boolean = false): Boolean =
  runCatching { toBoolean() }.getOrElse { default }

fun String?.safeToInt(default: Int = 0): Int =
  runCatching { orEmpty().toInt() }.getOrElse { default }

fun String?.safeToLong(default: Long = 0L): Long =
  runCatching { orEmpty().toLong() }.getOrElse { default }

fun String?.safeToFloat(default: Float = 0f): Float =
  runCatching { orEmpty().toFloat() }.getOrElse { default }

fun String?.safeToDouble(default: Double = 0.0): Double =
  runCatching { orEmpty().toDouble() }.getOrElse { default }

@ColorInt
fun String?.safeToColor(@ColorInt default: Int = 0): Int =
  runCatching { Color.parseColor(this) }.getOrDefault(default)

fun String?.isNetworkUrl(): Boolean = URLUtil.isNetworkUrl(this)

fun String?.isValidUrl(): Boolean = URLUtil.isValidUrl(this)

// ---------------------Calculate-------------------------------//

infix fun Double.plus(that: Double): Double {
  return (BigDecimal(this.toString()) + BigDecimal(that.toString())).toDouble()
}

infix fun Double.minus(that: Double): Double {
  return (BigDecimal(this.toString()) - BigDecimal(that.toString())).toDouble()
}

infix fun Double.times(that: Double): Double {
  return (BigDecimal(this.toString()) * BigDecimal(that.toString())).toDouble()
}

infix fun Double.div(that: Double): Double {
  return (BigDecimal(this.toString()) / BigDecimal(that.toString())).toDouble()
}

fun Number.isZero(): Boolean {
  return when (this) {
    is Byte, is Short, is Int, is Long -> this == 0
    is Float, is Double -> BigDecimal(this.toString()) == BigDecimal("0.0")
    else -> false
  }
}

fun Number.isNotZero(): Boolean = isZero().not()

fun Int?.orZero(): Int = this ?: 0

fun Int.isInvalid(invalidValue: Int = -1) = this == invalidValue

fun Int.isValid(invalidValue: Int = -1) = isInvalid(invalidValue).not()

fun Long.isInvalid(invalidValue: Long = -1) = this == invalidValue

fun Long.isValid(invalidValue: Long = -1) = isInvalid(invalidValue).not()

fun Boolean?.orTrue(): Boolean = this ?: true

fun Boolean?.orFalse(): Boolean = this ?: false

// ---------------------Collections-------------------------------//

@OptIn(ExperimentalContracts::class)
fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean {
  contract {
    returns(true) implies (this@isNotNullOrEmpty != null)
  }
  return this.isNullOrEmpty().not()
}

/**
 * 判断集合内是否仅有一个元素
 */
@OptIn(ExperimentalContracts::class)
fun <T> Collection<T>?.isSingle(): Boolean {
  contract {
    returns(true) implies (this@isSingle != null)
  }
  return this != null && size == 1
}

/**
 * 判断集合内是否有多个元素
 * @param minSize 最小为 2
 */
@OptIn(ExperimentalContracts::class)
fun <T> Collection<T>?.isMultiple(@IntRange(from = 2) minSize: Int = 2): Boolean {
  contract {
    returns(true) implies (this@isMultiple != null)
  }
  val min = if (minSize < 2) 2 else minSize
  return this != null && size >= min
}

fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> {
  val endIndex = if (toIndex > size) size else toIndex
  return subList(fromIndex, endIndex)
}

/**
 * 取集合内第二个元素
 */
fun <T> List<T>.secondOrNull(): T? {
  return if (size < 2) null else this[1]
}

/**
 * 取集合内第三个元素
 */
fun <T> List<T>.thirdOrNull(): T? {
  return if (size < 3) null else this[2]
}

fun paramMapOf(vararg pairs: Pair<String, Any>): HashMap<String, Any> =
  HashMap<String, Any>(pairs.size).apply { putAll(pairs) }

fun <K, V> MutableMap<K, V>.removeFirst(): Map.Entry<K, V> {
  val iterator = iterator()
  val element = iterator.next()
  iterator.remove()
  return element
}

fun <K, V> MutableMap<K, V>.removeFirstOrNull(predicate: (Map.Entry<K, V>) -> Boolean): Map.Entry<K, V>? =
  entries.removeFirstOrNull(predicate)

fun <T> MutableCollection<T>.removeFirstOrNull(predicate: (T) -> Boolean): T? {
  val iterator = iterator()
  while (iterator.hasNext()) {
    val element = iterator.next()
    if (predicate(element)) {
      iterator.remove()
      return element
    }
  }
  return null
}

// ---------------------File-------------------------------//

fun File.notExists(): Boolean = exists().not()

// ---------------------Intent-------------------------------//

fun Intent.getStringExtra(name: String, defaultValue: String): String =
  getStringExtra(name) ?: defaultValue

fun Intent.getCharSequenceExtra(name: String, defaultValue: CharSequence): CharSequence =
  getCharSequenceExtra(name) ?: defaultValue

inline fun <reified T : Parcelable> Intent.getParcelableExtra(name: String, defaultValue: T): T =
  getParcelableExtra(name) ?: defaultValue

inline fun <reified T : Serializable> Intent.getSerializableExtra(
  name: String,
  defaultValue: T,
): T = (getSerializableExtra(name) ?: defaultValue) as T

// ---------------------Fragment-------------------------------//

fun <T : Fragment> T.putArguments(bundle: Bundle?): T {
  arguments = bundle
  return this
}

fun <T : Fragment> T.putArguments(vararg pairs: Pair<String, Any?>): T =
  putArguments(bundleOf(*pairs))

/**
 * @param containerViewId   容器 id
 * @param fragment          要添加的 fragment
 * @param isAddToBackStack  将要添加的 fragment 是否要添加到返回栈，默认不添加
 * @param tag               fragment 的 tag
 */
@MainThread
fun FragmentManager.addFragment(
  fragment: Fragment,
  @IdRes containerViewId: Int = android.R.id.content,
  isAddToBackStack: Boolean = false,
  tag: String? = null,
) {
  if (fragment.isAdded) return
  commit {
    if (isAddToBackStack) addToBackStack(tag)
    add(containerViewId, fragment, tag)
  }
}

/**
 * @param containerViewId   容器 id
 * @param fragment          要替换的 fragment
 * @param isAddToBackStack  将要替换的 fragment 是否要添加到返回栈，默认添加
 * @param tag               fragment 的 tag
 */
@MainThread
fun FragmentManager.replaceFragment(
  fragment: Fragment,
  @IdRes containerViewId: Int = android.R.id.content,
  isAddToBackStack: Boolean = true,
  tag: String? = null,
) {
  if (fragment.isAdded) return
  commit {
    if (isAddToBackStack) addToBackStack(tag)
    replace(containerViewId, fragment, tag)
  }
}

@MainThread
fun Fragment.addFragment(
  fragment: Fragment,
  @IdRes containerViewId: Int = android.R.id.content,
  isAddToBackStack: Boolean = false,
  tag: String? = null,
) {
  childFragmentManager.addFragment(fragment, containerViewId, isAddToBackStack, tag)
}

@MainThread
fun Fragment.replaceFragment(
  fragment: Fragment,
  @IdRes containerViewId: Int = android.R.id.content,
  isAddToBackStack: Boolean = false,
  tag: String? = null,
) {
  childFragmentManager.addFragment(fragment, containerViewId, isAddToBackStack, tag)
}

// ---------------------View-------------------------------//

inline val View.attachedFragment: Fragment?
  get() = runCatching { findFragment<Fragment>() }.getOrNull()

inline val View.attachedActivity: Activity?
  get() {
    var baseContext: Context? = context
    while (baseContext is ContextWrapper) {
      if (baseContext is Activity) break
      baseContext = baseContext.baseContext
    }
    return baseContext as? Activity
  }

inline val Context?.lifecycle: Lifecycle?
  get() {
    var context: Context? = this
    while (true) {
      when (context) {
        is LifecycleOwner -> return context.lifecycle
        is ContextWrapper -> context = context.baseContext
        else -> return null
      }
    }
  }

inline val View.lifecycle: Lifecycle?
  get() = findViewTreeLifecycleOwner()?.lifecycle ?: context.lifecycle

inline val View.lifecycleScope: LifecycleCoroutineScope? get() = lifecycle?.coroutineScope

fun TextView.setOnEditorConfirmActionListener(listener: (TextView) -> Unit) {
  setOnEditorActionListener { view, actionId, event ->
    val isConfirmAction = if (event != null) {
      when (event.keyCode) {
        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER -> true
        else -> false
      } && event.action == KeyEvent.ACTION_DOWN
    } else {
      when (actionId) {
        EditorInfo.IME_NULL, EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_NEXT -> true
        else -> false
      }
    }
    if (isConfirmAction) {
      listener(view)
      true
    } else {
      false
    }
  }
}

// ---------------------Context-------------------------------//

fun Context.addDynamicShortcutCompat(id: String, shortcut: ShortcutInfoCompat) {
  if (
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 &&
    ShortcutManagerCompat.getDynamicShortcuts(this).any { it.id == id }.not()
  ) {
    try {
      ShortcutManagerCompat.addDynamicShortcuts(this, mutableListOf(shortcut))
    } catch (_: Exception) {
    }
  }
}

/**
 * 取消音频静音
 */
fun Context.setMusicUnmute() {
  setMusicMute(false)
}

/**
 * 设置音频静音
 *
 * @param mute 是否静音
 */
fun Context.setMusicMute(mute: Boolean = true) {
  getSystemService<AudioManager>()?.let {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val direction = if (mute) AudioManager.ADJUST_UNMUTE else AudioManager.ADJUST_MUTE
      it.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, 0)
    } else {
      it.setStreamMute(AudioManager.STREAM_MUSIC, mute)
    }
  }
}

// ---------------------Activity-------------------------------//

@MainThread
fun FragmentActivity.addFragment(
  fragment: Fragment,
  @IdRes containerViewId: Int = android.R.id.content,
  isAddToBackStack: Boolean = false,
  tag: String? = null,
) {
  supportFragmentManager.addFragment(fragment, containerViewId, isAddToBackStack, tag)
}

@MainThread
fun FragmentActivity.replaceFragment(
  fragment: Fragment,
  @IdRes containerViewId: Int = android.R.id.content,
  isAddToBackStack: Boolean = false,
  tag: String? = null,
) {
  supportFragmentManager.replaceFragment(fragment, containerViewId, isAddToBackStack, tag)
}
