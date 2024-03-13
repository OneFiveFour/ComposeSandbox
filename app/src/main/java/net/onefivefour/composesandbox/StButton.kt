package net.onefivefour.composesandbox

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClippedShadowButton(
    text: String,
    @DrawableRes iconRes: Int? = null,
    onClick: () -> Unit
) {

    val outerHorizontalPadding = 24.dp
    val outerHorizontalPaddingPx = with(LocalDensity.current) {
        outerHorizontalPadding.toPx()
    }
    val outerVerticalPadding = 24.dp

    val innerVerticalPadding = 14.dp
    val innerHorizontalPadding = 24.dp

    val buttonShape = RoundedCornerShape(size = 16.dp)

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .padding(
                horizontal = outerHorizontalPadding,
                vertical = outerVerticalPadding
            )
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = ScaleIndication(outerHorizontalPaddingPx)
                )
                .background(
                    color = Color(0xFFFFFFFF),
                    shape = buttonShape
                )
                .clip(
                    shape = buttonShape
                )
                .padding(
                    horizontal = innerHorizontalPadding,
                    vertical = innerVerticalPadding
                )
        ) {

            ButtonContent(
                iconRes = iconRes,
                text = text,
                alpha = 0f
            )
        }

        ButtonContent(
            iconRes,
            text
        )
    }
}

@Composable
private fun ButtonContent(
    iconRes: Int?,
    text: String,
    alpha: Float = 1f
) {

    val contentColor = Color(0xFF8391A8)

    Row(
        modifier = Modifier.alpha(alpha = alpha),
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

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun StButtonPreview() {
    ClippedShadowButton(
        text = "New Session",
        iconRes = android.R.drawable.ic_input_add,
        onClick = {}
    )
}