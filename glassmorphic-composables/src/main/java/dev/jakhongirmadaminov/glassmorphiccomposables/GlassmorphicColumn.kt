package dev.jakhongirmadaminov.glassmorphiccomposables

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun GlassmorphicColumn(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    childMeasures: SnapshotStateList<Place>,
    targetBitmap: Bitmap,
    dividerSpace: Int = 10,
    blurRadius: Int = 100,
    childCornerRadius: Int = 10,
    drawOnTop: DrawScope.(Path) -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit),
) {

    val blurredBg = remember { fastblur(targetBitmap, 1f, blurRadius)?.asImageBitmap() }

    var containerMeasures by remember { mutableStateOf<Place?>(null) }

    val calculatedWidth = containerMeasures?.size?.width?.dp?.let { parentDp ->
        containerMeasures?.offset?.x?.toInt()?.dp?.let { childDp ->
            parentDp + childDp
        } ?: run { 0.dp }
    } ?: run { 0.dp }

    blurredBg?.let { bg ->
        Canvas(
            modifier = modifier
                .verticalScroll(scrollState)
                .width(calculatedWidth)
                .height(containerMeasures?.size?.height?.dp ?: 0.dp)
        ) {
            for (i in childMeasures.indices) {
                val path = Path()
                path.addRoundRect(
                    RoundRect(
                        Rect(
                            offset = Offset(
                                childMeasures[i].offset.x,
                                childMeasures[i].offset.y
                            ),
                            size = childMeasures[i].size.toSize(),
                        ),
                        CornerRadius(childCornerRadius.dp.toPx())
                    )
                )

                clipPath(path, clipOp = ClipOp.Intersect) {
                    drawImage(
                        bg,
                        Offset(
                            -(containerMeasures?.offset?.x ?: 0f),
                            scrollState.value.toFloat() - (containerMeasures?.offset?.y ?: 0f)
                        )
                    )
                }
                drawOnTop(path)
            }
        }

    }

    Box(modifier = modifier
        .fillMaxSize()
        .clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
        })

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