package com.lau.scalp

import com.lau.scalp.config.ScalpConfig
import com.lau.scalp.config.ScreenType
import com.lau.scalp.config.ScreenType.DESKTOP
import com.lau.scalp.config.ScreenType.PHONE
import com.lau.scalp.config.ScreenType.TABLET
import com.lau.scalp.config.ScreenshotConfigBuilder
import com.lau.scalp.driver.DriverApi
import com.lau.scalp.image.ImageProcessor
import com.lau.scalp.metadata.MetadataProcessor
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver

class Scalp(
    private val driver: ChromeDriver,
    private val config: ScalpConfig = ScalpConfig()
) {

    private val driverApi = DriverApi(driver, config)
    private val metadataProcessor = MetadataProcessor()

    fun capture(builder: ScreenshotConfigBuilder.() -> Unit): ScalpResult {
        Thread.sleep(config.delay.beforeShot.inWholeMilliseconds)
        driverApi.overrideSize()

        val screenshotConfig = builder.let { ScreenshotConfigBuilder(config).apply(it).build() }
        screenshotConfig.beforeBlock.forEach { it() }
        val enlargedElements = screenshotConfig.enlargedElements.map { it().also { it.prepare() } }
        enlargedElements.forEach {
            it.apply(driver, config.decorator.enlargeMargin)
        }

        var image = driverApi.captureScreenshot()

        val cropFragment = screenshotConfig.cropFragment()
        image = ImageProcessor.crop(image, cropFragment)
        screenshotConfig.highlightedElements.forEach {
            image = ImageProcessor.highlight(
                sourceImage = image,
                rect = it(),
                color = config.decorator.borderColor,
                lineWidth = config.decorator.borderWidth
            )
        }
        screenshotConfig.blurredElements.forEach {
            image = ImageProcessor.blur(
                sourceImage = image,
                rect = it()
            )
        }

        val metadata = metadataProcessor.createMetadata(
            cropFragment = cropFragment,
            metadata = screenshotConfig.metadata.map { it() }
        )

        with(config.metadata) {
            if (drawMetadata) {
                metadata.elements.forEach { meta ->
                    ImageProcessor.highlight(
                        sourceImage = image,
                        rect = meta.coordinates,
                        color = borderColor,
                        lineWidth = borderWidth
                    )
                    ImageProcessor.addText(
                        sourceImage = image,
                        point = meta.anchor,
                        color = textColor,
                        text = meta.index.toString(),
                        font = font
                    )
                }
            }
        }

        enlargedElements.forEach { it.reset(driver) }
        screenshotConfig.afterBlock.forEach { it() }
        driverApi.resetSize()

        return ScalpResult(image, metadata)
    }

    fun resize(type: ScreenType) {
        with(driver.manage().window()) {
            when (type) {
                PHONE -> size = Dimension(config.screenSize.mobile, size.height)
                TABLET -> size = Dimension(config.screenSize.tablet, size.height)
                DESKTOP -> maximize()
            }
        }
    }
}
