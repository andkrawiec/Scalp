# Scalp
Scalp is a Selenium-based library focused on automating the capture and cropping of web page screenshots, primarily for use in web product documentation. It provides tools for generating detailed blueprints and visual annotations of webpage elements, enhancing testing and documentation workflows within Selenium projects.

## Features
1. **Precise Fragment Capture:** Captures specific fragments of web pages by taking screenshots and cropping to the desired section.
2. **Blueprint Generation:** Generates coordinates and relative anchor points for elements within the captured fragment.
3. **Visual Annotations:** Allows highlighting or blurring specific elements to emphasize or obscure details.
4. **Selenium Integration:** Integrates seamlessly with existing Selenium projects, enhancing web automation and testing.

## Installation
Maven central coordinates: `io.github.andkrawiec:scalp:0.0.2`

## Quick Start
Here's a simple example demonstrating how to use Scalp with Selenium to capture a screenshot of a specific fragment of a webpage and apply visual annotations:

```kotlin
val driver: ChromeDriver = TODO() // Set up ChromeDriver

val scalp = Scalp(driver)

driver.navigate().to("http://example.com/")

// Locate the elements you want to capture
val fragment = driver.findElement(By.tagName("div"))
val title = driver.findElement(By.tagName("h1"))
val description = driver.findElement(By.xpath("//p[1]"))
val moreInfo = driver.findElement(By.tagName("a"))

// Use Scalp to capture the fragment and create blueprint
val (image, blueprint) = scalp.capture {
    crop(fragment) {
        // Optional 'decorations'
        highlight(description)
        blur(moreInfo)
        padding(all = 10)
    }

    // Save position and size of title element in blueprint
    metadata(fragment = title)
    // Like above but with additional 'decorations' 
    metadata(
        fragment = moreInfo,
        index = 1,
        anchor = BOTTOM + OUTSIDE
    ) {
        padding(vertical = 3, horizontal = 5)
        moveAnchorBy(x = -20)
    }
}
```

The example above produces a `ScalpResult`, which includes an image represented as a `java.awt.image.BufferedImage`

![example.png](images%2Fbasic_example.png)

And a blueprint that, when serialized to JSON looks as follows:
```json
{
  "width": 684,
  "height": 254,
  "elements": [
    {
      "coordinates": {
        "x": 42,
        "y": 63,
        "width": 600,
        "height": 38
      }
    },
    {
      "index": 1,
      "coordinates": {
        "x": 37,
        "y": 173,
        "width": 147,
        "height": 25
      },
      "anchor": {
        "x": 90,
        "y": 209
      }
    }
  ]
}
```

## Advanced configuration

Scalp allows for optional configuration settings that can be used to modify the library's default behavior. Each configuration value can be adjusted individually.

```kotlin
val scalpConfig = ScalpConfig(
    screenSize = ScreenSizeConfig(
        mobile = 600, // Screen width for simulating a phone (default 600 pixels)
        tablet = 1500 // Screen width for simulating a tablet (default 1500 pixels)
    ),
    decorator = DecoratorConfig(
        borderColor = Color.RED, // Border color for highlighting elements
        borderWidth = 3, // Border width for highlighted elements
        enlargeMargin = 30 // General margin for cropped elements, used with enlargeLeft, Top, Right, Bottom for temporary CSS styles
    ),
    metadata = MetadataConfig(
        drawMetadata = false, // Determines if metadata should be visible on the generated image, used mainly for development and debugging
        baseOffset = 11, // Specifies the anchor offset, used for positioning the anchor as AnchorPosition.INSIDE or AnchorPosition.OUTSIDE
        borderColor = Color.RED, // Border color for metadata elements, active only when drawMetadata is true
        borderWidth = 3, // Border width for metadata elements, active only when drawMetadata is true
        textColor = Color.RED, // Text color for metadata, active only when drawMetadata is true
        font = Font("Sans", Font.PLAIN, 20) // Font for metadata text, active only when drawMetadata is true
    ),
    delay = DelayConfig(
        beforeShot = 500.milliseconds, // Waiting time before performing each screenshot
        scrollStep = 100.milliseconds // Duration of each scroll step, useful for animations
    ),
    jsScript = JsScriptConfig(
        maxHeight = DEFAULT_MAX_HEIGHT, // JavaScript for calculating the maximum height of the document
        viewportHeight = DEFAULT_VIEWPORT_HEIGHT, // JavaScript for calculating the height of the visible area in the browser window
        yScrollTo = DEFAULT_Y_SCROLL_TO, // JavaScript for scrolling the window to a specified position on the Y-axis
        allMetrics = DEFAULT_ALL_METRICS, // JavaScript for collecting and returning metrics related to browser window and screen sizes, device scaling, and mobility
        devicePixelRatio = DEFAULT_DEVICE_PIXEL_RATIO // JavaScript for returning the device pixel ratio, used to determine screenshot quality on different devices
    )
)
val scalp = Scalp(driver, scalpConfig)
```

## Limitations

Scalp was developed as a "side project" to address specific issues related to capturing and manipulating web page screenshots for documentation purposes. As such, it currently has the following limitations:

- **Browser Support**: Scalp is designed to work with ChromeDriver. This means it currently supports only Google Chrome browsers. If you are interested in extending support to browsers like Firefox using GeckoDriver, or implementing support for RemoteWebDriver, contributions are highly welcomed.

- **Features Set**: While Scalp handles its primary functions well, there may be advanced use cases it does not yet cover. If there's a feature you feel would benefit Scalp, please consider contributing or suggesting it.

Additionally, there is a need for a web technology tool that consumes the 'blueprint' metadata and highlights sections during a hover on the image. This would greatly enhance the interactivity and usability of the documentation produced with Scalp. Contributions in this area are particularly welcome.

## License

Scalp is released under the [Apache 2.0 license](LICENSE).

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
