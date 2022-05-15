package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.myapplication.ui.theme.MyApplicationTheme

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
    val cardMarginDp = 15

    with(LocalDensity.current) {
        screenWidthDp = LocalConfiguration.current.screenWidthDp
        screenWidthPx = screenWidthDp.dp.toPx().toInt()
        cardWidthDp = screenWidthDp / 2
    }


    val bg = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.samarkand
        ), screenWidthPx, screenWidthPx, true
    )
    val bgBlurred = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.samarkand_dimmed_blurred
        ), screenWidthPx, screenWidthPx, true
    )


    val scrollState = rememberScrollState()
    val items = arrayListOf<Int>()
    for (i in 0 until 100) {
        items.add(i)
    }

    Box {

        Image(
            bitmap = bg.asImageBitmap(),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )

        val childMeasures = remember {
            mutableStateListOf<Place>()
        }
        var containerMeasures by remember { mutableStateOf<Place?>(null) }
        childMeasures.addAll(items.map { Place() })


        GlassmorphicView(scrollState, containerMeasures, items, childMeasures, bgBlurred){
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .onGloballyPositioned {
                        if (containerMeasures == null) {
                            containerMeasures = Place(it.size, it.positionInParent())
                        }
                    },
                horizontalArrangement = Arrangement.spacedBy(cardMarginDp.dp),
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                items.forEachIndexed { index, it ->
                    Box(
                        modifier = Modifier
                            .onGloballyPositioned {
                                childMeasures[index] = Place(it.size, it.positionInParent())
                            }
                            .width(cardWidthDp.dp)
//                        .background(Color.Red)
                            .padding(15.dp)

                    ) {
                        Text("Item $it")
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                3

            }

        }



    }

}

@Composable
private fun GlassmorphicView(
    scrollState: ScrollState,
    containerMeasures: Place?,
    items: ArrayList<Int>,
    childMeasures: SnapshotStateList<Place>,
    bgBlurred: Bitmap,
    content : @Composable () ->Unit
) {
    Canvas(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .width(containerMeasures?.size?.width?.dp ?: 0.dp)
            .height(containerMeasures?.size?.height?.dp ?: 0.dp)
    ) {
        for (i in items.indices) {
            val path = Path()

            path.addRoundRect(
                RoundRect(
                    Rect(
                        offset = childMeasures[i].offset,
                        size = childMeasures[i].size.toSize(),
                    ),
                    CornerRadius(10.dp.toPx())
                )
            )

            clipPath(path, clipOp = ClipOp.Intersect) {
                drawImage(
                    bgBlurred.asImageBitmap(),
                    Offset(scrollState.value.toFloat(), 0f)
                )
            }


        }

    }
    content()

}

@Immutable
data class Place(var size: IntSize = IntSize(0, 0), var offset: Offset = Offset(0f, 0f)) {

}


@Composable
fun MyBlurLayout(content: @Composable () -> Unit) {


//    Layout(content = content, modifier = Modifier) { measurables, constraints ->
//
//
//        val itemWidth = constraints.maxWidth / 10
//        val itemConstraints = constraints.copy(minWidth = itemWidth, maxWidth = itemWidth)
//
//        val placeables = measurables.map {
//            it.measure(itemConstraints)
//        }
//
//
//    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}