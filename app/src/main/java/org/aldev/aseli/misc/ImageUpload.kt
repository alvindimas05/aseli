package org.aldev.aseli.misc

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import java.util.Locale

class ImageUpload {
    companion object {
        fun getMimeType(avt: Activity, uri: Uri): String? {
            val mimeType: String? = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
                val cr: ContentResolver = avt.contentResolver
                cr.getType(uri)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.lowercase(Locale.getDefault())
                )
            }
            return mimeType
        }
    }
}