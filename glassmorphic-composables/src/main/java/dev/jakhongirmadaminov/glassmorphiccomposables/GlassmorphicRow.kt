package dev.jakhongirmadaminov.glassmorphiccomposables

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize


@Composable
fun GlassmorphicRow(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    childMeasures: SnapshotStateList<Place>,
    targetBitmap: Bitmap,
    dividerSpace: Int = 10,
    blurRadius: Int = 100,
    content: @Composable (RowScope.() -> Unit),
) {
    val blurredBg = remember { fastblur(targetBitmap, 1f, blurRadius)?.asImageBitmap() }

    var containerMeasures by remember { mutableStateOf<Place?>(null) }

    val strokeColor = Color(0x80ffffff)
    val transparent = Color.Transparent
    val t = containerMeasures?.size?.height?.dp?.let { parentDp ->
        containerMeasures?.offset?.y?.toInt()?.dp?.let { childDp ->
            parentDp + childDp
        } ?: run { 0.dp }
    } ?: run { 0.dp }

    blurredBg?.let { bg ->

        Canvas(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .width(containerMeasures?.size?.width?.dp ?: 0.dp)
                .height(t)
        ) {
            for (i in childMeasures.indices) {
                val path = Path()

                path.addRoundRect(
                    RoundRect(
                        Rect(
                            offset = Offset(
                                (containerMeasures?.offset?.x ?: 0f) + childMeasures[i].offset.x,
                                (containerMeasures?.offset?.y ?: 0f) + childMeasures[i].offset.y
                            ),
                            size = childMeasures[i].size.toSize(),
                        ),
                        CornerRadius(10.dp.toPx())
                    )
                )

                clipPath(path, clipOp = ClipOp.Intersect) {
                    drawImage(
                        bg,
                        Offset(scrollState.value.toFloat(), 0f)
                    )
                }
//                drawPath(
//                    path = path,
//                    color = strokeColor,
//                    style = Stroke(1f),
//                    blendMode = BlendMode.Luminosity
////                blendMode = BlendMode.Luminosity
//                )
//                drawPath(
//                    path = path,
//                    brush = Brush.verticalGradient(listOf(strokeColor, transparent)),
//                    blendMode = BlendMode.Overlay
////                blendMode = BlendMode.Plus
////                blendMode = BlendMode.Screen
////                blendMode = BlendMode.Luminosity
//                )


            }
        }

    }

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .onGloballyPositioned {
                if (containerMeasures == null) {
                    containerMeasures = Place(it.size, it.positionInParent())
                }
            },
        horizontalArrangement = Arrangement.spacedBy(dividerSpace.dp),
    ) {
        content()
    }

}

@Composable
fun GlassmorphicColumn(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    childMeasures: SnapshotStateList<Place>,
    targetBitmap: Bitmap,
    dividerSpace: Int = 10,
    blurRadius: Int = 100,
    content: @Composable (ColumnScope.() -> Unit),
) {

    val blurredBg = remember { fastblur(targetBitmap, 1f, blurRadius)?.asImageBitmap() }

    var containerMeasures by remember { mutableStateOf<Place?>(null) }

    val strokeColor = Color(0x80ffffff)
    val transparent = Color.Transparent
    val t = containerMeasures?.size?.width?.dp?.let { parentDp ->
        containerMeasures?.offset?.x?.toInt()?.dp?.let { childDp ->
            parentDp + childDp
        } ?: run { 0.dp }
    } ?: run { 0.dp }


    blurredBg?.let { bg ->

        Canvas(
            modifier = Modifier
                .verticalScroll(scrollState)
                .width(t)
                .height(containerMeasures?.size?.height?.dp ?: 0.dp)
        ) {
            for (i in childMeasures.indices) {
                val path = Path()

                path.addRoundRect(
                    RoundRect(
                        Rect(
                            offset = Offset(
                                (containerMeasures?.offset?.x ?: 0f) + childMeasures[i].offset.x,
                                (containerMeasures?.offset?.y ?: 0f) + childMeasures[i].offset.y
                            ),
                            size = childMeasures[i].size.toSize(),
                        ),
                        CornerRadius(10.dp.toPx())
                    )
                )

                clipPath(path, clipOp = ClipOp.Intersect) {
                    drawImage(
                        bg,
                        Offset(0f, scrollState.value.toFloat())
                    )
                }
//            drawPath(
//                path = path,
//                color = strokeColor,
//                style = Stroke(1f),
//            )
//            drawPath(
//                path = path,
//                brush = Brush.verticalGradient(listOf(strokeColor, transparent)),
//                blendMode = BlendMode.Overlay
////                blendMode = BlendMode.Plus
////                blendMode = BlendMode.Screen
////                blendMode = BlendMode.Luminosity
//            )


            }
        }

    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .onGloballyPositioned {
                if (containerMeasures == null) {
                    containerMeasures = Place(it.size, it.positionInParent())
                }
            },
        verticalArrangement = Arrangement.spacedBy(dividerSpace.dp),
    ) {
        content()
    }

}

@Immutable
data class Place(var size: IntSize = IntSize(0, 0), var offset: Offset = Offset(0f, 0f))
