package com.lau.scalp.image

import org.openqa.selenium.WebElement

data class Rect(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) {

    fun applyPadding(padding: Padding): Rect {
        val safeLeft = minOf(padding.left, x)
        val safeTop = minOf(padding.top, y)
        return copy(
            x = x - safeLeft,
            y = y - safeTop,
            width = width + safeLeft + padding.right,
            height = height + safeTop + padding.bottom
        )
    }
}

fun rect(x: Int, y: Int, width: Int, height: Int): Rect =
    Rect(x, y, width, height)


fun WebElement.rect(): Rect =
    Rect(rect.x, rect.y, rect.width, rect.height)
