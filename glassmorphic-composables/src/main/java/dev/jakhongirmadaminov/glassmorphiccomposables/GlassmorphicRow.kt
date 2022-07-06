package dev.jakhongirmadaminov.glassmorphiccomposables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize


@Composable
fun GlassmorphicRow(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    childMeasures: SnapshotStateList<Place>,
    targetBitmap: ImageBitmap,
    isAlreadyBlurred: Boolean = false,// providing already blurred bitmap consumes less resources
    dividerSpace: Int = 10,
    blurRadius: Int = 100,
    childCornerRadius: Int = 10,
    drawOnTop: DrawScope.(Path) -> Unit = {},
    content: @Composable() (RowScope.() -> Unit),
) {

    if (childMeasures.isEmpty()) return
    val blurredBg: ImageBitmap = remember {
        if (isAlreadyBlurred) {
            targetBitmap
        } else {
            fastblur(targetBitmap.asAndroidBitmap(), 1f, blurRadius)?.asImageBitmap() ?: return
        }
    }


    var containerMeasures by remember { mutableStateOf(Place()) }
    val calculatedHeight = containerMeasures.size.height.dp.let { parentDp ->
        containerMeasures.offset.y.toInt().dp.let { childDp ->
            parentDp + childDp
        }
    }


    Canvas(
        modifier = modifier
            .horizontalScroll(scrollState)
            .width(containerMeasures.size.width.dp)
            .height(calculatedHeight)
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
                    blurredBg,
                    Offset(
                        scrollState.value.toFloat() - containerMeasures.offset.x,
                        -containerMeasures.offset.y
                    )
                )
            }
            drawOnTop(path)

        }

    }

    Box(modifier = modifier
        .fillMaxSize()
        .clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
        })
    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .onGloballyPositioned {
                if (containerMeasures.size.width == 0 && containerMeasures.size.height == 0) {
                    containerMeasures = Place(it.size, it.positionInParent())
                }
            },
        horizontalArrangement = Arrangement.spacedBy(dividerSpace.dp),
    ) {
        content()
    }

}


