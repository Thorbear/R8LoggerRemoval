package no.thorbear.android.proguardloggerremoval

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"
    private val logger = LoggerFactory.getLogger(MainActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        androidLog("Android log")
        slf4j("SLF4J")
    }

    private fun androidLog(foo: Any) {
        Log.v(tag, "v $foo")
        Log.d(tag, "d $foo")
        Log.i(tag, "i $foo")
        Log.w(tag, "w $foo")
        Log.e(tag, "e $foo")
    }

    private fun slf4j(foo: Any) {
        logger.trace("trace {}", foo)
        logger.debug("debug {}", foo)
        logger.info("info {}", foo)
        logger.warn("warn {}", foo)
        logger.error("error {}", foo)
    }
}
