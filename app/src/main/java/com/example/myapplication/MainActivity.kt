package com.example.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import uz.uzkassa.apay.glassmorphic_composables.GlassmorphicColumn
import uz.uzkassa.apay.glassmorphic_composables.GlassmorphicRow
import uz.uzkassa.apay.glassmorphic_composables.Place

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    var screenWidthPx: Int
    var screenWidthDp: Int
    var cardWidthDp: Int
    val context = LocalContext.current
    with(LocalDensity.current) {
        screenWidthDp = LocalConfiguration.current.screenWidthDp
        screenWidthPx = screenWidthDp.dp.toPx().toInt()
        cardWidthDp = screenWidthDp / 2
    }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val scrollState = rememberScrollState()
    val items = arrayListOf<Int>()
    for (i in 0 until 100) {
        items.add(i)
    }

    Box {

        val captureController = rememberCaptureController()
        Capturable(
            controller = captureController,
            onCaptured = { bitmap, error ->
                // This is captured bitmap of a content inside Capturable Composable.
                if (bitmap != null) {
                    capturedBitmap = bitmap.asAndroidBitmap()
                    // Bitmap is captured successfully. Do something with it!
                }

                if (error != null) {
                    // Error occurred. Handle it!
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
        })


        val childMeasures = remember {
            mutableStateListOf<Place>()
        }
        childMeasures.addAll(items.map { Place() })

        capturedBitmap?.let {

            GlassmorphicRow(
                modifier = Modifier.padding(horizontal = 15.dp),
                scrollState,
                items,
                childMeasures,
                it,
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
                            //                                .background(Color(0xFFFF0000))
                            .padding(15.dp)
                    ) {
                        Text(
                            "Item $it",
                            color = Color.White
                        )
                    }
                }
            }

        }


    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}