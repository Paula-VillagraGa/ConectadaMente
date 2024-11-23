package com.example.conectadamente.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns

fun getImageSize(uri: Uri, context: Context): Long {
    val contentResolver: ContentResolver = context.contentResolver
    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
    val sizeIndex = cursor?.getColumnIndex(OpenableColumns.SIZE) ?: -1
    return if (cursor != null && sizeIndex != -1) {
        cursor.moveToFirst()
        val size = cursor.getLong(sizeIndex)
        cursor.close()
        size
    } else {
        cursor?.close()
        0L // Si no se puede determinar el tamaño
    }
}
