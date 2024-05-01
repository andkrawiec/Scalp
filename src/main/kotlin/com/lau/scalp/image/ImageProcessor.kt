package com.lau.scalp.image

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints.KEY_ANTIALIASING
import java.awt.RenderingHints.KEY_RENDERING
import java.awt.RenderingHints.VALUE_ANTIALIAS_ON
import java.awt.RenderingHints.VALUE_RENDER_QUALITY
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.awt.image.ConvolveOp
import java.awt.image.ConvolveOp.EDGE_NO_OP
import java.awt.image.Kernel

object ImageProcessor {

    private val kernel = Kernel(7, 7, FloatArray(49) { 1.0f / 49.0f })

    fun crop(sourceImage: BufferedImage, rect: Rect): BufferedImage =
        sourceImage.getSubimage(
            rect.x.coerceAtLeast(0),
            rect.y.coerceAtLeast(0),
            rect.width.coerceAtMost(sourceImage.width - rect.x.coerceAtLeast(0)),
            rect.height.coerceAtMost(sourceImage.height - rect.y.coerceAtLeast(0))
        )

    fun highlight(sourceImage: BufferedImage, rect: Rect, color: Color?, lineWidth: Int): BufferedImage {
        with(sourceImage.createGraphics()) {
            paint = color
            setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
            setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY)
            stroke = BasicStroke(lineWidth.toFloat())
            with(rect) {
                drawRoundRect(
                    x,
                    y,
                    width,
                    height,
                    10,
                    10
                )
            }
            dispose()
        }
        return sourceImage
    }

    fun blur(sourceImage: BufferedImage, rect: Rect): BufferedImage =
        BufferedImage(sourceImage.width, sourceImage.height, TYPE_INT_ARGB).apply {
            with(createGraphics()) {
                drawImage(sourceImage, 0, 0, null)
                drawImage(
                    ConvolveOp(kernel, EDGE_NO_OP, null)
                        .filter(sourceImage.getSubimage(rect.x, rect.y, rect.width, rect.height), null),
                    rect.x,
                    rect.y,
                    null
                )
                dispose()
            }
        }

    fun addText(
        sourceImage: BufferedImage,
        point: Point,
        color: Color,
        text: String,
        font: Font
    ): BufferedImage {
        with(sourceImage.createGraphics()) {
            paint = color
            this.font = font
            setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
            setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY)
            val width = fontMetrics.stringWidth(text)
            val height = fontMetrics.ascent - fontMetrics.descent
            val padding = 3

            stroke = BasicStroke(3f)
            drawRect(
                point.x - (width / 2) - padding,
                point.y - (height / 2) - padding,
                width + 2 * padding,
                height + 2 * padding
            )
            paint = Color.black
            drawString(
                text,
                point.x - (width / 2),
                point.y + (height / 2)
            )
            dispose()
        }
        return sourceImage
    }
}
