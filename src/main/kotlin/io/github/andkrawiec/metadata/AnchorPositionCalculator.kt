package io.github.andkrawiec.metadata

import io.github.andkrawiec.image.Padding
import io.github.andkrawiec.image.Point
import io.github.andkrawiec.image.Rect
import io.github.andkrawiec.metadata.AnchorPosition.BOTTOM
import io.github.andkrawiec.metadata.AnchorPosition.INSIDE
import io.github.andkrawiec.metadata.AnchorPosition.LEFT
import io.github.andkrawiec.metadata.AnchorPosition.OUTSIDE
import io.github.andkrawiec.metadata.AnchorPosition.RIGHT
import io.github.andkrawiec.metadata.AnchorPosition.TOP

class AnchorPositionCalculator(
    private val offset: Int
) {

    fun getPosition(
        baseRect: Rect,
        anchor: Int,
        padding: Padding,
        offsetX: Int,
        offsetY: Int
    ): Point {
        checkConflictingFlags(anchor, TOP, BOTTOM)
        checkConflictingFlags(anchor, LEFT, RIGHT)
        checkConflictingFlags(anchor, INSIDE, OUTSIDE)

        var x = calculateCoordinate(
            anchor = anchor,
            flag1 = LEFT,
            flag2 = RIGHT,
            base = baseRect.x,
            length = baseRect.width,
            offset = offsetX,
            padding1 = padding.left,
            padding2 = padding.right
        )
        var y = calculateCoordinate(
            anchor = anchor,
            flag1 = TOP,
            flag2 = BOTTOM,
            base = baseRect.y,
            length = baseRect.height,
            offset = offsetY,
            padding1 = padding.top,
            padding2 = padding.bottom
        )

        x += adjustOffset(anchor, LEFT, RIGHT)
        y += adjustOffset(anchor, TOP, BOTTOM)

        return Point(x, y)
    }

    private fun checkConflictingFlags(anchor: Int, flag1: AnchorPosition, flag2: AnchorPosition) {
        if (anchor and flag1.raw == flag1.raw && anchor and flag2.raw == flag2.raw) {
            throw IllegalArgumentException("Cannot use mutually exclusive flags")
        }
    }

    private fun calculateCoordinate(
        anchor: Int,
        flag1: AnchorPosition,
        flag2: AnchorPosition,
        base: Int,
        length: Int,
        offset: Int,
        padding1: Int,
        padding2: Int
    ): Int {
        var coordinate = base + length / 2
        if (anchor and flag1.raw == flag1.raw) {
            coordinate = base - padding1
        } else if (anchor and flag2.raw == flag2.raw) {
            coordinate = base + length + padding2
        }
        return coordinate + offset
    }

    private fun adjustOffset(anchor: Int, flag1: AnchorPosition, flag2: AnchorPosition): Int {
        var adjustment = 0
        if (anchor and INSIDE.raw == INSIDE.raw) {
            if (anchor and flag1.raw == flag1.raw) adjustment += offset
            if (anchor and flag2.raw == flag2.raw) adjustment -= offset
        }
        if (anchor and OUTSIDE.raw == OUTSIDE.raw) {
            if (anchor and flag1.raw == flag1.raw) adjustment -= offset
            if (anchor and flag2.raw == flag2.raw) adjustment += offset
        }
        return adjustment
    }
}
