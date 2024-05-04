package io.github.andkrawiec.config

import io.github.andkrawiec.driver.Enlarged
import io.github.andkrawiec.image.Point
import io.github.andkrawiec.image.Rect

data class ScreenshotConfig(
    val cropFragment: () -> Rect,
    val highlightedElements: List<() -> Rect>,
    val blurredElements: List<() -> Rect>,
    val enlargedElements: List<() -> Enlarged>,
    val metadata: List<() -> MetadataFragment>,
    val beforeBlock: MutableList<() -> Unit>,
    val afterBlock: MutableList<() -> Unit>
)

data class MetadataFragment(
    val index: Int?,
    val fragment: Rect,
    val anchor: Point?
)
