# glassmorphic-composables

GlassmorphicColumn @Composable 

![image](https://user-images.githubusercontent.com/12813066/168596336-97b5f8e9-7d99-48c6-b66a-a993a12fd087.png)


GlassmorphicRow @Composable

![image](https://user-images.githubusercontent.com/12813066/168596377-82baee30-a41e-4375-aa8c-aba678995b41.png)


Usage: 

Place your background Image @Composable in Capturable @Composable 

  ```val captureController = rememberCaptureController()
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

        LaunchedEffect(key1 = true, block = {
            withContext(Main) {
                captureController.capture()
            }
        })```
        
        
Create a mutable list for storing child item positions and offsets 
        
 ```val childMeasures = remember { mutableStateListOf<Place>() }```
 
 
Place your item @Composables in either GlassmorphicRow or GlassmorphicColumn and update Place object from above list. Pass capturade background image into the Glassmorphic @Composable


            ```GlassmorphicRow(
                modifier = Modifier.padding(top = 150.dp),
                scrollState,
                items,
                childMeasures,
                capturedImage,
                dividerSpace = 10
            )
            {
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
            }```


 
        
