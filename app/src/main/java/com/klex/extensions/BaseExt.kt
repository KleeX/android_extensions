package com.klex.extensions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

val String.isEmailFormat: Boolean
    get() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun ImageView.setImage(image: Drawable?, errorResource: Int? = null, placeholder: Int? = null) {
    load(image) {
        crossfade(true)
        if (errorResource != null) {
            error(errorResource)
        }
        if (placeholder != null) {
            placeholder(placeholder)
        }
    }
}

fun ImageView.setImage(file: File, errorResource: Int? = null, placeholder: Int? = null) {
    load(file) {
        crossfade(true)
        if (errorResource != null) {
            error(errorResource)
        }
        if (placeholder != null) {
            placeholder(placeholder)
        }
        scale(Scale.FILL)
    }
}

fun ImageView.setImageCircle(
    imageUrl: String?,
    errorResource: Int? = null,
    placeholder: Int? = null
) {
    load(imageUrl) {
        crossfade(true)
        if (errorResource != null) {
            error(errorResource)
        }
        if (placeholder != null) {
            placeholder(placeholder)
        }
        scale(Scale.FILL)
        transformations(CircleCropTransformation())
    }
}

fun ImageView.setImageCircle(file: File, errorResource: Int? = null, placeholder: Int? = null) {
    load(file) {
        crossfade(true)
        if (errorResource != null) {
            error(errorResource)
        }
        if (placeholder != null) {
            placeholder(placeholder)
        }
        scale(Scale.FIT)
        transformations(CircleCropTransformation())
    }
}

fun Context.saveImage(myBitmap: Bitmap): String {
    val bytes = ByteArrayOutputStream()
    myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
    val file = createImageFile()
    val fo = FileOutputStream(file)
    fo.write(bytes.toByteArray())
    MediaScannerConnection.scanFile(
        this,
        arrayOf(file.path),
        arrayOf("image/jpeg"), null
    )
    fo.close()
    Log.d("file", "File Saved::--->" + file.absolutePath)
    return file.absolutePath
}

fun Context.showDialog(title: String, message: String) {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setTitle(title)
    dialogBuilder.setMessage(message)
    dialogBuilder.setPositiveButton(
        R.string.klex_base_ok
    ) { dialog, _ -> dialog.dismiss() }
    dialogBuilder.setCancelable(true)
    dialogBuilder.create().show()
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}

var View.show: Boolean
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else GONE
    }