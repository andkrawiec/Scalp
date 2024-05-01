package com.lau.scalp.metadata

import com.lau.scalp.config.MetadataFragment
import com.lau.scalp.image.Point
import com.lau.scalp.image.Rect

class MetadataProcessor {

    fun createMetadata(cropFragment: Rect, metadata: List<MetadataFragment>): ScreenshotMetadata {
        val (offsetX, offsetY) = cropFragment

        val meta = metadata
            .map {
                MetadataElement(
                    index = it.index,
                    coordinates = it.fragment.copy(
                        x = it.fragment.x - offsetX,
                        y = it.fragment.y - offsetY
                    ),
                    anchor = Point(
                        x = it.anchor.x - offsetX,
                        y = it.anchor.y - offsetY
                    )
                )
            }

        return ScreenshotMetadata(
            width = cropFragment.width,
            height = cropFragment.height,
            elements = meta
        )
    }
}
