package com.lau.scalp.metadata

enum class AnchorPosition(val raw: Int) {
    TOP(1),
    BOTTOM(2),
    LEFT(4),
    RIGHT(8),
    INSIDE(16),
    OUTSIDE(32)
}
