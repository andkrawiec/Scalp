package com.lau.scalp

import com.lau.scalp.metadata.ScreenshotMetadata
import java.awt.image.BufferedImage

data class ScalpResult(
    val image: BufferedImage,
    val metadata: ScreenshotMetadata
)
