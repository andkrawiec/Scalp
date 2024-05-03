package com.lau.scalp

import com.lau.scalp.metadata.ScreenshotBlueprint
import java.awt.image.BufferedImage

data class ScalpResult(
    val image: BufferedImage,
    val blueprint: ScreenshotBlueprint
)
