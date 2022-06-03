package tmidev.customerbase.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormattedDate(): String {
    val date = Date(this)
    val dateFormat = SimpleDateFormat(
        "HH:mm:ss' - 'yyyy/MMM/dd",
        Locale.getDefault()
    )
    return dateFormat.format(date)
}