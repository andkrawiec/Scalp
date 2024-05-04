# Scalp
Scalp is a Selenium-based library focused on automating the capture and cropping of web page screenshots, primarily for use in web product documentation. It provides tools for generating detailed blueprints and visual annotations of webpage elements, enhancing testing and documentation workflows within Selenium projects.

## Features
1. **Precise Fragment Capture:** Captures specific fragments of web pages by taking screenshots and cropping to the desired section.
2. **Blueprint Generation:** Generates coordinates and relative anchor points for elements within the captured fragment.
3. **Visual Annotations:** Allows highlighting or blurring specific elements to emphasize or obscure details.
4. **Selenium Integration:** Integrates seamlessly with existing Selenium projects, enhancing web automation and testing.

## Installation
Maven central coordinates: `io.github.andkrawiec:scalp:0.0.1`

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

![example.png](example%2Fexample.png)

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
