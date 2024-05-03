package com.lau.scalp.metadata

import com.lau.scalp.image.Point
import com.lau.scalp.image.Rect

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
