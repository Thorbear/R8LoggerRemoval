package no.thorbear.android.slf4j

import org.slf4j.ILoggerFactory
import org.slf4j.IMarkerFactory
import org.slf4j.helpers.BasicMarkerFactory
import org.slf4j.helpers.NOPMDCAdapter
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider

class AndroidSLF4JServiceProvider : SLF4JServiceProvider {

    private lateinit var requestedApiVersion: String
    private lateinit var loggerFactory: ILoggerFactory
    private lateinit var markerFactory: IMarkerFactory
    private lateinit var mdcAdapter: MDCAdapter

    override fun getMarkerFactory(): IMarkerFactory = markerFactory

    override fun getRequesteApiVersion(): String = requestedApiVersion

    override fun getLoggerFactory(): ILoggerFactory = loggerFactory

    override fun getMDCAdapter(): MDCAdapter = mdcAdapter

    override fun initialize() {
        requestedApiVersion = "1.8.0"
        loggerFactory = AndroidLoggerFactory()
        markerFactory = BasicMarkerFactory()
        mdcAdapter = NOPMDCAdapter()
    }
}