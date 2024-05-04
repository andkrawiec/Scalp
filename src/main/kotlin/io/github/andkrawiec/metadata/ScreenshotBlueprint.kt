package io.github.andkrawiec.metadata

import io.github.andkrawiec.image.Point
import io.github.andkrawiec.image.Rect

data class ScreenshotBlueprint(
    val width: Int,
    val height: Int,
    val elements: List<BlueprintElement>
)

data class BlueprintElement(
    val index: Int?,
    val coordinates: Rect,
    val anchor: Point?
)
