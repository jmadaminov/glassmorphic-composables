package dev.jakhongirmadaminov.glassmorphic_sample

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jakhongirmadaminov.glassmorphic_sample.ui.theme.MyApplicationTheme
import dev.jakhongirmadaminov.glassmorphiccomposables.GlassmorphicColumn
import dev.jakhongirmadaminov.glassmorphiccomposables.Place
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

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
                    Sample()
                }
            }
        }
    }
}

@Composable
fun Sample() {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val cardWidthDp = screenWidthDp / 2
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val scrollState = rememberScrollState()
    val items = arrayListOf<Int>()
    for (i in 0 until 100) {
        items.add(i)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val captureController = rememberCaptureController()
        Capturable(
            controller = captureController,
            onCaptured = { bitmap, _ ->
                // This is captured bitmap of a content inside Capturable Composable.
                if (bitmap != null) {
                    capturedBitmap = bitmap.asAndroidBitmap()
                    // Bitmap is captured successfully. Do something with it!
                }
            }
        ) {
            val bgColor = Color(0xffE1E1E1)
            val box1 = Color(0xff40B850)
            val box2 = Color(0xffCA181A)
            val box3 = Color(0xff035FE5)
            val box4 = Color(0xffA46ABF)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor)
            )

            Row() {

                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(box4)

                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(box2)

                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(box3)

                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(box1)

                    )

                }

                Column(modifier = Modifier.weight(1f)) {


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(box2)

                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(box1)

                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(box3)

                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(box4)

                    )


                }
            }

//            Image(
//                painter = painterResource(id = R.drawable.bg_autumn),
//                contentDescription = "",
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
        }

        LaunchedEffect(key1 = true, block = {
            withContext(Main) {
                captureController.capture()
            }
        })

        val childMeasures = remember { mutableStateListOf<Place>() }
        childMeasures.addAll(items.map { Place() })

        capturedBitmap?.let { capturedImage ->
            GlassmorphicColumn(
                modifier = Modifier.padding(start = 100.dp),
                scrollState = scrollState,
                childMeasures = childMeasures,
                targetBitmap = capturedImage,
                dividerSpace = 10,
                blurRadius = 100,
                drawOnTop = { path ->
                    val strokeColor = Color(0x80ffffff)
                    val transparent = Color.Transparent
                    drawPath(
                        path = path,
                        color = strokeColor,
                        style = Stroke(1f),
                    )
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(listOf(strokeColor, transparent)),
                        blendMode = BlendMode.Overlay
//                blendMode = BlendMode.Plus
//                blendMode = BlendMode.Screen
//                blendMode = BlendMode.Luminosity
                    )

                },
                content = {
                    items.forEachIndexed { index, it ->
                        Box(
                            modifier = Modifier
                                //                            .background(Color(0x80FF0000))
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
                },
            )

        }


    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Sample()
    }
}