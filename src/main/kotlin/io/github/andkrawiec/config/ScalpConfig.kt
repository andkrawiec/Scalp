package io.github.andkrawiec.config

import java.awt.Color
import java.awt.Font
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class ScalpConfig(
    val screenSize: ScreenSizeConfig = ScreenSizeConfig(),
    val decorator: DecoratorConfig = DecoratorConfig(),
    val metadata: MetadataConfig = MetadataConfig(),
    val delay: DelayConfig = DelayConfig(),
    val jsScript: JsScriptConfig = JsScriptConfig()
)

enum class ScreenType {
    PHONE,
    TABLET,
    DESKTOP
}

data class DecoratorConfig(
    val borderColor: Color = Color.RED,
    val borderWidth: Int = 3,
    val enlargeMargin: Int = 30
)

data class MetadataConfig(
    val drawMetadata: Boolean = false,
    val baseOffset: Int = 11,
    val borderColor: Color = Color.RED,
    val borderWidth: Int = 3,
    val textColor: Color = Color.RED,
    val font: Font = Font("Sans", Font.PLAIN, 20)
)

data class ScreenSizeConfig(
    val phone: Int = 600,
    val tablet: Int = 1500
)

data class DelayConfig(
    val beforeShot: Duration = 500.milliseconds,
    val scrollStep: Duration = 100.milliseconds
)
