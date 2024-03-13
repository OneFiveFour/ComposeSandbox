package net.onefivefour.composesandbox.ui

import android.graphics.BlurMaskFilter
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ClippedShadowButton2(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int? = null,
    onClick: () -> Unit
) {

    val outerHorizontalPadding = 20.dp
    val outerVerticalPadding = 20.dp

    val innerVerticalPadding = 16.dp
    val innerHorizontalPadding = 24.dp

    val buttonShape = RoundedCornerShape(size = 16.dp)

    val contentColor = Color(0xFF8391A8)

    // for dynamic height calculation
    val density = LocalDensity.current
    var spacerHeight by remember { mutableStateOf(value = 0.dp) }

    Box(
        modifier = modifier
            .padding(
                horizontal = outerHorizontalPadding,
                vertical = outerVerticalPadding
            )
            .wrapContentSize()
    ) {

        // Left shadow
        Spacer(
            modifier = Modifier
                .width(161.dp)
                .height(spacerHeight)
                .align(alignment = Alignment.CenterStart)
                .advanceShadow(
                    Color(0xFFFFFFFF),
                    borderRadius = 16.dp,
                    blurRadius = 8.dp,
                    offsetX = (-2).dp,
                    offsetY = (-2).dp
                )
        )

        // Right shadow
        Spacer(
            modifier = Modifier
                .width(160.dp)
                .height(spacerHeight)
                .align(alignment = Alignment.CenterStart)
                .advanceShadow(
                    Color(0xFFAEAEC0),
                    borderRadius = 16.dp,
                    blurRadius = 12.dp,
                    offsetX = 4.dp,
                    offsetY = 8.dp
                )
        )

        // Button
        Row(
            modifier = Modifier
                .width(160.dp)
                .align(alignment = Alignment.Center)
                .background(color = Color(0xFFE6E6E6), shape = buttonShape)
                .clip(shape = buttonShape)
                .clickable(onClick = onClick)
                .padding(horizontal = innerHorizontalPadding, vertical = innerVerticalPadding)
                .onSizeChanged {
                    spacerHeight = with(density) { it.height.toDp() + (innerVerticalPadding * 2) }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = contentColor
            )
        }
    }
}


fun Modifier.advanceShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 16.dp,
    blurRadius: Dp = 16.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Float = 1f,
) = drawBehind {
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        val spreadPixel = spread.dp.toPx()
        val leftPixel = (0f - spreadPixel) + offsetX.toPx()
        val topPixel = (0f - spreadPixel) + offsetY.toPx()
        val rightPixel = (this.size.width)
        val bottomPixel = (this.size.height + spreadPixel)

        if (blurRadius != 0.dp) {
            /*
                The feature maskFilter used below to apply the blur effect only works
                with hardware acceleration disabled.
             */
            frameworkPaint.maskFilter =
                (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
        }

        frameworkPaint.color = color.toArgb()
        it.drawRoundRect(
            left = leftPixel,
            top = topPixel,
            right = rightPixel,
            bottom = bottomPixel,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE1E1E1
)
@Composable
fun StButtonPreview() {
    ClippedShadowButton2(
        text = "New Session",
        iconRes = android.R.drawable.ic_input_add,
        onClick = {}
    )
}