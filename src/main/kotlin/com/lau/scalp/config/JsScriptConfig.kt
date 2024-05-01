package com.lau.scalp.config

class JsScriptConfig(
    val maxHeight: String = DEFAULT_MAX_HEIGHT,
    val viewportHeight: String = DEFAULT_VIEWPORT_HEIGHT,
    val yScrollTo: String = DEFAULT_Y_SCROLL_TO,
    val allMetrics: String = DEFAULT_ALL_METRICS,
    val devicePixelRatio: String = DEFAULT_DEVICE_PIXEL_RATIO
) {

    companion object {
        private const val DEFAULT_MAX_HEIGHT: String =
            """
            return Math.max(
                document.body.scrollHeight, 
                document.body.offsetHeight, 
                document.documentElement.clientHeight, 
                document.documentElement.scrollHeight, 
                document.documentElement.offsetHeight
            );
            """

        private const val DEFAULT_VIEWPORT_HEIGHT: String =
            """
            return window.innerHeight || 
                document.documentElement.clientHeight || 
                document.body.clientHeight;
            """

        private const val DEFAULT_Y_SCROLL_TO: String =
            "window.scrollTo(0, arguments[0]);"

        private const val DEFAULT_ALL_METRICS: String =
            """
            ({
                width: Math.max(window.innerWidth, document.body.scrollWidth, document.documentElement.scrollWidth) | 0,
                height: Math.max(window.innerHeight, document.body.scrollHeight, document.documentElement.scrollHeight) | 0,
                deviceScaleFactor: window.devicePixelRatio || 1, 
                mobile: typeof window.orientation !== 'undefined'
            });
            """

        private const val DEFAULT_DEVICE_PIXEL_RATIO: String =
            "return window.devicePixelRatio;"
    }
}
