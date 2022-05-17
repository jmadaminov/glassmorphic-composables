# glassmorphic-composables

GlassmorphicColumn @Composable 

![image](https://user-images.githubusercontent.com/12813066/168596336-97b5f8e9-7d99-48c6-b66a-a993a12fd087.png)


GlassmorphicRow @Composable

![image](https://user-images.githubusercontent.com/12813066/168596377-82baee30-a41e-4375-aa8c-aba678995b41.png)

Setup Gradle:

```
allprojects {
    repositories {
    maven { url 'https://jitpack.io' }
    }
}
```
```implementation 'com.github.jakhongirmadaminov:glassmorphic-composables:0.0.3'```

Usage: 

Place your background _Image_ composable in _Capturable_ composable 

  ```
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val captureController = rememberCaptureController()
        Capturable(
            controller = captureController,
            onCaptured = { bitmap, error ->
                // This is captured bitmap of a content inside Capturable Composable.
                if (bitmap != null) {
                    capturedBitmap = bitmap.asAndroidBitmap()
                    // Bitmap is captured successfully. Do something with it!
                }
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_autumn),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

// If your background image is loaded through URL you can call this after the image is loaded.
        LaunchedEffect(key1 = true, block = {
            withContext(Main) {
                captureController.capture()
            }
        })
```
        
Create a mutable list with the exact item count and populate with default value for storing child item positions and offsets
        
 ``` val childMeasures = remember {
        mutableStateListOf<Place>().apply {
            addAll(YOUR_LIST.map { Place() })
        }
    }```
 
 
Place your item Composables in either _GlassmorphicRow_ or _GlassmorphicColumn_ and update Place object from above list. Pass captured background image into the _Glassmorphic_ Composable

Note: _Capturable_ and _Glassmorphic_ composables must share the same parent Composable like a _Box_.

```
   GlassmorphicRow(
                modifier = Modifier.padding(top = 150.dp),
                scrollState = scrollState,
                childMeasures = childMeasures,
                targetBitmap = capturedBitmap,
                dividerSpace = 10,
                blurRadius = 50
            ){
                items.forEachIndexed { index, it ->
                    Box(
                        modifier = Modifier
                            .onGloballyPositioned {
                                childMeasures[index] = Place(it.size, it.positionInParent())
                            }
                            .width(cardWidthDp.dp)
                            .padding(15.dp)
                    ) {
                        Text(
                            "Item $it",
                            color = Color.White
                        )
                    }
                }
            }
```


 
        
