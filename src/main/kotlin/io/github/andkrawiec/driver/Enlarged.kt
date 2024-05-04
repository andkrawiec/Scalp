package io.github.andkrawiec.driver

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

abstract class Enlarged(
    private val element: WebElement
) {

    private var oldStyle: String? = null

    fun prepare() {
        oldStyle = element.getAttribute("style")
    }

    fun reset(driver: WebDriver) {
        oldStyle?.run { driver.setAttribute(element, "style", this) }
    }

    abstract fun apply(driver: WebDriver, margin: Int)
}

class EnlargeLeft(
    private val element: WebElement
) : Enlarged(element) {

    override fun apply(driver: WebDriver, margin: Int) {
        driver.addAttribute(element, "style", "margin-left: ${margin}px")
    }
}

class EnlargeTop(
    private val element: WebElement
) : Enlarged(element) {

    override fun apply(driver: WebDriver, margin: Int) {
        driver.addAttribute(element, "style", "margin-top: ${margin}px")
    }
}

class EnlargeRight(
    private val element: WebElement
) : Enlarged(element) {

    override fun apply(driver: WebDriver, margin: Int) {
        driver.addAttribute(element, "style", "margin-right: ${margin}px")
    }
}

class EnlargeBottom(
    private val element: WebElement
) : Enlarged(element) {

    override fun apply(driver: WebDriver, margin: Int) {
        driver.addAttribute(element, "style", "margin-bottom: ${margin}px")
    }
}

fun WebDriver.setAttribute(element: WebElement, name: String, value: String) {
    val executor = this as JavascriptExecutor
    executor.executeScript(
        "arguments[0].setAttribute(arguments[1], arguments[2]);",
        *listOf(element, name, value).toTypedArray()
    )
}

fun WebDriver.addAttribute(element: WebElement, name: String, value: String) {
    val executor = this as JavascriptExecutor
    executor.executeScript(
        """
        var currentAttribute = arguments[0].getAttribute(arguments[1]) || "";
        if (!currentAttribute.endsWith(';')) {
            currentAttribute += ';';
        }
        arguments[0].setAttribute(arguments[1], currentAttribute + arguments[2]);
        """,
        *listOf(element, name, value).toTypedArray()
    )
}
