package com.lau.scalp.driver

import com.lau.scalp.config.ScalpConfig
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.util.Base64
import javax.imageio.ImageIO
import kotlin.math.ceil
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.CommandInfo
import org.openqa.selenium.remote.HttpCommandExecutor
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.Response
import org.openqa.selenium.remote.http.HttpMethod.POST

class DriverApi(
    private val driver: ChromeDriver,
    private val config: ScalpConfig
) {

    fun captureScreenshot(): BufferedImage =
        sendCommand(
            "Page.captureScreenshot", mapOf(
                "format" to "png",
                "fromSurface" to true
            )
        ).toBufferedImage()

    fun overrideSize() {
        val devicePixelRatio = devicePixelRatio()

        defineCustomSendCommand()
        val docHeight = docHeight(devicePixelRatio)
        val viewportHeight = viewportHeight(devicePixelRatio)
        val verticalIterations = ceil(docHeight.toDouble() / viewportHeight).toInt()
        repeat(verticalIterations) {
            scrollTo(it * viewportHeight(devicePixelRatio), devicePixelRatio)
            Thread.sleep(config.delay.scrollStep.inWholeMilliseconds)
        }
        setDeviceMetricsOverride()
    }

    fun resetSize() {
        sendCommand("Emulation.clearDeviceMetricsOverride", emptyMap<Any, Any>())
    }

    private fun setDeviceMetricsOverride() {
        sendCommand("Emulation.setDeviceMetricsOverride", evaluate(config.jsScript.allMetrics))
    }

    private fun defineCustomSendCommand() {
        HttpCommandExecutor::class.java
            .getDeclaredMethod(
                "defineCommand",
                String::class.java,
                CommandInfo::class.java
            )
            .apply { isAccessible = true }
            .invoke(
                (driver as RemoteWebDriver).commandExecutor,
                "sendCommand",
                CommandInfo("/session/:sessionId/chromium/send_command_and_get_result", POST)
            )
    }

    private fun devicePixelRatio(): Double =
        executeJsScript(config.jsScript.devicePixelRatio)
            .let { if (it is Double) it else it as Long * 1.0 }

    private fun viewportHeight(devicePixelRatio: Double): Int =
        (executeJsScript(config.jsScript.viewportHeight) as Long * devicePixelRatio).toInt()

    private fun docHeight(devicePixelRatio: Double): Int =
        (executeJsScript(config.jsScript.maxHeight) as Long * devicePixelRatio).toInt()

    private fun scrollTo(y: Int, devicePixelRatio: Double) {
        executeJsScript(config.jsScript.yScrollTo, y / devicePixelRatio)
    }

    private fun evaluate(script: String): Any =
        sendCommand("Runtime.evaluate", mapOf("returnByValue" to true, "expression" to script))
            .let { (it as Map<*, *>)["result"] }
            .let { (it as Map<*, *>)["value"]!! }

    private fun sendCommand(cmd: String, params: Any): Any =
        RemoteWebDriver::class.java
            .getDeclaredMethod("execute", String::class.java, MutableMap::class.java)
            .apply { isAccessible = true }
            .let { it.invoke(driver, "sendCommand", mapOf("cmd" to cmd, "params" to params)) as Response }
            .value

    private fun executeJsScript(script: String, vararg arg: Any?): Any? =
        driver.executeScript(script, *arg)

    private fun Any.toBufferedImage(): BufferedImage =
        ((this as Map<*, *>)["data"] as String)
            .let { Base64.getDecoder().decode(it) }
            .let { ByteArrayInputStream(it) }
            .let { ImageIO.read(it) }
}
