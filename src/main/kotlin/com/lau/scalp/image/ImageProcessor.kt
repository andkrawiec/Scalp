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
import kotlin.math.exp

object ImageProcessor {

    private const val KERNEL_SIZE = 7
    private const val SIGMA = 5.0
    private val kernel = createGaussianKernel()

    private fun createGaussianKernel(): Kernel {
        val center = KERNEL_SIZE / 2
        var sum = 0.0
        val matrix = FloatArray(KERNEL_SIZE * KERNEL_SIZE) { 0.0f }

        for (x in 0 until KERNEL_SIZE) {
            for (y in 0 until KERNEL_SIZE) {
                val xDistance = x - center
                val yDistance = y - center
                val value = exp(-(xDistance * xDistance + yDistance * yDistance) / (2 * SIGMA * SIGMA)).toFloat()
                matrix[y * KERNEL_SIZE + x] = value
                sum += value
            }
        }

        for (i in matrix.indices) {
            matrix[i] /= sum.toFloat()
        }

        return Kernel(KERNEL_SIZE, KERNEL_SIZE, matrix)
    }

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

    fun blur(sourceImage: BufferedImage, rect: Rect): BufferedImage {
        val margin = KERNEL_SIZE / 2 + 2
        val paddedRect = Rect(
            x = rect.x - margin,
            y = rect.y - margin,
            width = rect.width + 2 * margin,
            height = rect.height + 2 * margin
        )

        val clippedRect = Rect(
            0.coerceAtLeast(paddedRect.x),
            0.coerceAtLeast(paddedRect.y),
            (sourceImage.width - paddedRect.x).coerceAtMost(paddedRect.width),
            (sourceImage.height - paddedRect.y).coerceAtMost(paddedRect.height)
        )

        val subImage = sourceImage.getSubimage(
            clippedRect.x,
            clippedRect.y,
            clippedRect.width,
            clippedRect.height
        )
        val blurredSubImage = BufferedImage(
            subImage.width,
            subImage.height,
            TYPE_INT_ARGB
        )

        with(blurredSubImage.createGraphics()) {
            drawImage(subImage, 0, 0, null)
            dispose()
        }

            ConvolveOp(kernel, EDGE_NO_OP, null)
                .filter(subImage, blurredSubImage)

        with(sourceImage.createGraphics()) {
            drawImage(blurredSubImage, clippedRect.x, clippedRect.y, null)
            dispose()
        }

        return sourceImage
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
