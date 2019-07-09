package no.thorbear.android.slf4j

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import org.slf4j.helpers.MarkerIgnoringBase
import org.slf4j.helpers.MessageFormatter
import java.io.PrintWriter
import java.io.StringWriter

class AndroidLogger(name: String) : MarkerIgnoringBase() {

    @SuppressLint("ObsoleteSdkInt") // The SDK jar used for junit tests has SDK_INT = 0
    private val isRunningOnAndroid: Boolean = Build.VERSION.SDK_INT > 0

    init {
        this.name = name
    }

    override fun trace(msg: String) = v(msg)
    override fun trace(format: String, arg: Any?) = v(format, arg)
    override fun trace(format: String, arg1: Any?, arg2: Any?) = v(format, arg1, arg2)
    override fun trace(format: String, vararg arguments: Any?) = v(format, *arguments)
    override fun trace(msg: String, t: Throwable?) = v(msg, t)
    private fun v(format: String, vararg args: Any?) = format(Log.VERBOSE, format, *args) { name, message ->
        if (isRunningOnAndroid) {
            Log.v(name, message)
        } else {
            println("(TRACE) $name: $message")
        }
    }

    override fun debug(msg: String) = d(msg)
    override fun debug(format: String, arg: Any?) = d(format, arg)
    override fun debug(format: String, arg1: Any?, arg2: Any?) = d(format, arg1, arg2)
    override fun debug(format: String, vararg arguments: Any?) = d(format, *arguments)
    override fun debug(msg: String, t: Throwable?) = d(msg, t)
    private fun d(format: String, vararg args: Any?) = format(Log.DEBUG, format, *args) { name, message ->
        if (isRunningOnAndroid) {
            Log.d(name, message)
        } else {
            println("(DEBUG) $name: $message")
        }
    }

    override fun info(msg: String) = i(msg)
    override fun info(format: String, arg: Any?) = i(format, arg)
    override fun info(format: String, arg1: Any?, arg2: Any?) = i(format, arg1, arg2)
    override fun info(format: String, vararg arguments: Any?) = i(format, *arguments)
    override fun info(msg: String, t: Throwable?) = i(msg, t)
    private fun i(format: String, vararg args: Any?) = format(Log.INFO, format, *args) { name, message ->
        if (isRunningOnAndroid) {
            Log.i(name, message)
        } else {
            println("(INFO) $name: $message")
        }
    }

    override fun warn(msg: String) = w(msg)
    override fun warn(format: String, arg: Any?) = w(format, arg)
    override fun warn(format: String, vararg arguments: Any?) = w(format, *arguments)
    override fun warn(format: String, arg1: Any?, arg2: Any?) = w(format, arg1, arg2)
    override fun warn(msg: String, t: Throwable?) = w(msg, t)
    private fun w(format: String, vararg args: Any?) = format(Log.WARN, format, *args) { name, message ->
        if (isRunningOnAndroid) {
            Log.w(name, message)
        } else {
            println("(WARN) $name: $message")
        }
    }

    override fun error(msg: String) = e(msg)
    override fun error(format: String, arg: Any?) = e(format, arg)
    override fun error(format: String, arg1: Any?, arg2: Any?) = e(format, arg1, arg2)
    override fun error(format: String, vararg arguments: Any?) = e(format, *arguments)
    override fun error(msg: String, t: Throwable?) = e(msg, t)
    private fun e(format: String, vararg args: Any?) = format(Log.ERROR, format, *args) { name, message ->
        if (isRunningOnAndroid) {
            Log.e(name, message)
        } else {
            println("(ERROR) $name: $message")
        }
    }

    override fun isTraceEnabled(): Boolean = isLoggable(getName(), Log.VERBOSE)
    override fun isDebugEnabled(): Boolean = isLoggable(getName(), Log.DEBUG)
    override fun isInfoEnabled(): Boolean = isLoggable(getName(), Log.INFO)
    override fun isWarnEnabled(): Boolean = isLoggable(getName(), Log.WARN)
    override fun isErrorEnabled(): Boolean = isLoggable(getName(), Log.ERROR)

    private fun format(level: Int, format: String, vararg args: Any?, log: (name: String, message: String) -> Unit) {
        MessageFormatter.arrayFormat(format, args).run {
            if (throwable != null) {
                AndroidLoggerFactory.onThrowable(getName(), message, level, throwable)
            }
            log(getName(), createLogMessage(message, throwable))
        }
    }

    private fun createLogMessage(message: String, throwable: Throwable?): String {
        return "$message\n${getStackTraceString(throwable)}"
    }

    /**
     * Mirror of [Log.getStackTraceString] without the UnknownHostException filter.
     */
    private fun getStackTraceString(throwable: Throwable?): String {
        if (throwable == null) {
            return ""
        }

        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        throwable.printStackTrace(printWriter)
        printWriter.flush()
        return stringWriter.toString()
    }

    private fun isLoggable(name: String, level: Int): Boolean {
        return if (isRunningOnAndroid) {
            Log.isLoggable(name, level)
        } else {
            true
        }
    }
}