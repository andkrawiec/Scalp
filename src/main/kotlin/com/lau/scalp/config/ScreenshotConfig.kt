package com.lau.scalp.config

import com.lau.scalp.driver.Enlargeable
import com.lau.scalp.image.Point
import com.lau.scalp.image.Rect

data class ScreenshotConfig(
    val cropFragment: () -> Rect,
    val highlightedElements: List<() -> Rect>,
    val blurredElements: List<() -> Rect>,
    val enlargedElements: List<() -> Enlargeable>,
    val metadata: List<() -> MetadataFragment>,
    val beforeBlock: MutableList<() -> Unit>,
    val afterBlock: MutableList<() -> Unit>
)

data class MetadataFragment(
    val index: Int?,
    val fragment: Rect,
    val anchor: Point?
)
