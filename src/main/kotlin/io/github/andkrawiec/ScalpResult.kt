package io.github.andkrawiec

import io.github.andkrawiec.metadata.ScreenshotBlueprint
import java.awt.image.BufferedImage

data class ScalpResult(
    val image: BufferedImage,
    val blueprint: ScreenshotBlueprint
)
