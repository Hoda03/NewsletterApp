package com.newsletter.app.ui.common

import android.content.Context
import com.newsletter.app.R
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Store {

    fun getErrorMessage(e: Throwable, context: Context): String {
        return when (e) {
            is UnknownHostException -> context.getString(R.string.unknown_host_error)
            is SocketTimeoutException -> context.getString(R.string.connection_time_out)
            else -> context.getString(R.string.api_error_message)
        }
    }

    fun getDateInReadableForm(givenDate: String?): String {
        val dateInLongForm = convertStringDateToLong(givenDate)
        val mFormat = SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault())
        return mFormat.format(dateInLongForm)
    }

    private fun convertStringDateToLong(givenDate: String?): Long {
        var milliSeconds = 0L
        givenDate?.let {
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val date: Date? = formatter.parse(it)
                milliSeconds = date?.time ?: 0L
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return milliSeconds
    }

}