package com.lau.scalp.metadata

import com.lau.scalp.config.MetadataFragment
import com.lau.scalp.image.Point
import com.lau.scalp.image.Rect

class MetadataProcessor {

    fun createMetadata(cropFragment: Rect, metadata: List<MetadataFragment>): ScreenshotBlueprint {
        val (offsetX, offsetY) = cropFragment

        val meta = metadata
            .map {
                BlueprintElement(
                    index = it.index,
                    coordinates = it.fragment.copy(
                        x = it.fragment.x - offsetX,
                        y = it.fragment.y - offsetY
                    ),
                    anchor = it.anchor?.let { anchor ->
                        Point(
                            x = anchor.x - offsetX,
                            y = anchor.y - offsetY
                        )
                    }
                )
            }

        return ScreenshotBlueprint(
            width = cropFragment.width,
            height = cropFragment.height,
            elements = meta
        )
    }
}
