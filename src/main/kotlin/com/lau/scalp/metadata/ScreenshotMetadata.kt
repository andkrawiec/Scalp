package com.lau.scalp.metadata

import com.lau.scalp.image.Point
import com.lau.scalp.image.Rect

data class ScreenshotMetadata(
    val width: Int,
    val height: Int,
    val elements: List<MetadataElement>
)

data class MetadataElement(
    val index: Int,
    val coordinates: Rect,
    val anchor: Point
)
