package com.lau.scalp.config

import com.lau.scalp.driver.EnlargeBottom
import com.lau.scalp.driver.EnlargeLeft
import com.lau.scalp.driver.EnlargeRight
import com.lau.scalp.driver.EnlargeTop
import com.lau.scalp.driver.Enlargeable
import com.lau.scalp.image.Padding
import com.lau.scalp.image.Rect
import com.lau.scalp.image.rect
import com.lau.scalp.metadata.AnchorPosition
import com.lau.scalp.metadata.AnchorPositionCalculator
import org.openqa.selenium.WebElement

class ScreenshotConfigBuilder(
    scalpConfig: ScalpConfig
) {

    private val calculator = AnchorPositionCalculator(scalpConfig.metadata.baseOffset)
    private var fragment: (() -> Rect)? = null
    private val metadata = mutableListOf<() -> MetadataFragment>()
    private val highlightedElements = mutableListOf<() -> Rect>()
    private val blurredElements = mutableListOf<() -> Rect>()
    private val enlargedElements = mutableListOf<() -> Enlargeable>()
    private val beforeBlock = mutableListOf<() -> Unit>()
    private val afterBlock = mutableListOf<() -> Unit>()

    fun beforeShot(block: () -> Unit) {
        beforeBlock += block
    }

    fun afterShot(block: () -> Unit) {
        afterBlock += block
    }

    fun crop(fragment: WebElement, f: (CropModifierBuilder.() -> Unit)? = null) {
        val modifier = f?.let { CropModifierBuilder().apply(it) }

        lateinit var evaluatedFragment: Rect

        this.fragment = {
            val rect = (modifier
                ?.padding
                ?.let { fragment.rect().applyPadding(it) }
                ?: fragment.rect())
            evaluatedFragment = rect
            rect
        }

        if (modifier != null) {
            highlightedElements.addAll(
                modifier.highlightedElements.map {
                    {
                        val element = it()
                        element.copy(
                            x = element.x - evaluatedFragment.x,
                            y = element.y - evaluatedFragment.y
                        )
                    }
                }
            )

            blurredElements.addAll(
                modifier.blurredElements.map {
                    {
                        val element = it()
                        element.copy(
                            x = element.x - evaluatedFragment.x,
                            y = element.y - evaluatedFragment.y
                        )
                    }
                }
            )

            enlargedElements.addAll(modifier.enlargedElements)
        }
    }

    fun metadata(
        index: Int,
        fragment: WebElement,
        anchor: AnchorPosition,
        f: (MetadataModifierBuilder.() -> Unit)? = null
    ) {
        metadata(index, fragment, anchor.raw, f)
    }

    fun metadata(
        index: Int,
        fragment: WebElement,
        anchor: Int,
        f: (MetadataModifierBuilder.() -> Unit)? = null
    ) {
        metadata += {
            val rect = fragment.rect()
            val modifier = f?.let { MetadataModifierBuilder().apply(it) }
            val padding = modifier?.padding ?: Padding()

            MetadataFragment(
                index = index,
                fragment = rect.applyPadding(padding),
                anchor = calculator.getPosition(
                    baseRect = rect,
                    anchor = anchor,
                    padding = padding,
                    offsetX = modifier?.offsetX ?: 0,
                    offsetY = modifier?.offsetY ?: 0
                )
            )
        }
    }

    operator fun AnchorPosition.plus(right: AnchorPosition): Int =
        raw + right.raw

    operator fun Int.plus(anchor: AnchorPosition): Int =
        this + anchor.raw

    internal fun build(): ScreenshotConfig {
        require(fragment != null) { "Crop fragment must be specified" }

        return ScreenshotConfig(
            cropFragment = fragment!!,
            highlightedElements = highlightedElements,
            blurredElements = blurredElements,
            enlargedElements = enlargedElements,
            metadata = metadata,
            beforeBlock = beforeBlock,
            afterBlock = afterBlock
        )
    }
}

open class ModifierBuilder {

    internal var padding: Padding? = null


    fun padding(all: Int = 0) {
        padding = Padding(all, all, all, all)
    }

    fun padding(vertical: Int = 0, horizontal: Int = 0) {
        padding = Padding(horizontal, horizontal, vertical, vertical)
    }

    fun padding(left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) {
        padding = Padding(left, right, top, bottom)
    }
}

class CropModifierBuilder : ModifierBuilder() {

    internal val highlightedElements = mutableListOf<() -> Rect>()
    internal val blurredElements = mutableListOf<() -> Rect>()
    internal val enlargedElements = mutableListOf<() -> Enlargeable>()

    fun highlight(fragment: WebElement) {
        highlightedElements += { fragment.rect() }
    }

    fun blur(fragment: WebElement) {
        blurredElements += { fragment.rect() }
    }

    fun enlargeLeft(fragment: WebElement) {
        enlargedElements += { EnlargeLeft(fragment) }
    }

    fun enlargeTop(fragment: WebElement) {
        enlargedElements += { EnlargeTop(fragment) }
    }

    fun enlargeRight(fragment: WebElement) {
        enlargedElements += { EnlargeRight(fragment) }
    }

    fun enlargeBottom(fragment: WebElement) {
        enlargedElements += { EnlargeBottom(fragment) }
    }
}

class MetadataModifierBuilder : ModifierBuilder() {

    internal var offsetX = 0
    internal var offsetY = 0

    fun moveAnchorBy(x: Int = 0, y: Int = 0) {
        offsetX = x
        offsetY = y
    }
}
