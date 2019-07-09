package no.thorbear.android.slf4j

import org.slf4j.ILoggerFactory
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.regex.Pattern

class AndroidLoggerFactory : ILoggerFactory {

    /**
     * Extracts first 23 characters of the last part of a full class name. The 23-character limit is because Android may throw an exception
     * for longer tags in production.
     *
     * Example: `com.example.SomeClass$0$InnerOneWithAVeryLongName` becomes `InnerOneWithAVeryLongNa`.
     */
    private val classnamePattern = Pattern.compile("^(?:.*[.$])?([^.$]{0,23}).*$")
    private val loggerMap: ConcurrentMap<String, Logger> = ConcurrentHashMap()

    override fun getLogger(name: String): Logger {
        val tag = tagFromName(name)
        return loggerMap[tag] ?: makeNewLogger(tag)
    }

    private fun makeNewLogger(tag: String): Logger {
        val newInstance = AndroidLogger(tag)
        val oldInstance = loggerMap.putIfAbsent(tag, newInstance)
        return oldInstance ?: newInstance
    }

    private fun tagFromName(name: String): String {
        val matcher = classnamePattern.matcher(name)
        return if (matcher.matches()) {
            matcher.group(1)
        } else {
            name
        }
    }

    companion object {

        var throwableListener: ((name: String, message: String, level: Int, throwable: Throwable) -> Unit)? = null

        internal fun onThrowable(name: String, message: String, level: Int, t: Throwable) {
            throwableListener?.run {
                invoke(name, message, level, t)
            }
        }
    }
}