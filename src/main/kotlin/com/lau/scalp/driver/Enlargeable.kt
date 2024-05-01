package com.lau.scalp.driver

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

interface Enlargeable {
    fun prepare()
    fun apply(driver: WebDriver, margin: Int)
    fun reset(driver: WebDriver)
}

class EnlargeLeft(
    private val element: WebElement
) : Enlargeable {

    private var oldStyle: String? = null
    
    override fun prepare() {
        oldStyle = element.getAttribute("style")
    }

    override fun apply(driver: WebDriver, margin: Int) {
        driver.addAttribute(element, "style", "margin-left: ${margin}px")
    }

    override fun reset(driver: WebDriver) {
        oldStyle?.run { driver.setAttribute(element, "style", this) }
    }
}

class EnlargeTop(
    private val element: WebElement
) : Enlargeable {

    private var oldStyle: String? = null
    
    override fun prepare() {
        oldStyle = element.getAttribute("style")
    }

    override fun apply(driver: WebDriver, margin: Int) {
        driver.addAttribute(element, "style", "margin-top: ${margin}px")
    }

    override fun reset(driver: WebDriver) {
        oldStyle?.run { driver.setAttribute(element, "style", this) }
    }
}

class EnlargeRight(
    private val element: WebElement
) : Enlargeable {

    private var oldStyle: String? = null
    
    override fun prepare() {
        oldStyle = element.getAttribute("style")
    }

    override fun apply(driver: WebDriver, margin: Int) {
        driver.addAttribute(element, "style", "margin-right: ${margin}px")
    }

    override fun reset(driver: WebDriver) {
        oldStyle?.run { driver.setAttribute(element, "style", this) }
    }
}

class EnlargeBottom(
    private val element: WebElement
) : Enlargeable {

    private var oldStyle: String? = null

    override fun prepare() {
        oldStyle = element.getAttribute("style")
    }

    override fun apply(driver: WebDriver, margin: Int) {
        driver.addAttribute(element, "style", "margin-bottom: ${margin}px")
    }

    override fun reset(driver: WebDriver) {
        oldStyle?.run { driver.setAttribute(element, "style", this) }
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
