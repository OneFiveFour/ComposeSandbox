package net.onefivefour.composesandbox

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ClippedShadowButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int? = null,
    onClick: () -> Unit
) {

    val outerHorizontalPadding = 24.dp
    val outerVerticalPadding = 24.dp

    val innerVerticalPadding = 14.dp
    val innerHorizontalPadding = 24.dp

    // determines shadow width and spread
    val spacerWidth = 12.dp
    val shadowElevation = 18.dp

    val buttonShape = RoundedCornerShape(size = 16.dp)

    val shadowColor = Color(0xFF707070)
    val ambientColor = Color(0xFFFEFEFE)
    val contentColor = Color(0xFF8391A8)

    // for dynamic height calculation
    val density = LocalDensity.current
    var spacerHeight by remember { mutableStateOf(value = 0.dp) }

    val interactionSource = remember {
        MutableInteractionSource()
    }

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
                .size(width = spacerWidth, height = spacerHeight)
                .offset(y = -outerVerticalPadding / 2)
                .align(alignment = Alignment.CenterStart)
                .shadow(
                    elevation = shadowElevation,
                    shape = CircleShape,
                    ambientColor = ambientColor,
                    spotColor = shadowColor,
                    clip = true
                )
        )

        // Right shadow
        Spacer(
            modifier = Modifier
                .size(width = spacerWidth, height = spacerHeight)
                .offset(y = -outerVerticalPadding / 2)
                .align(alignment = Alignment.CenterEnd)
                .shadow(
                    elevation = shadowElevation,
                    shape = CircleShape,
                    ambientColor = ambientColor,
                    spotColor = shadowColor
                )
        )

        // Button
        Row(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .background(color = Color(0xFFFFFFFF), shape = buttonShape)
                .clip(shape = buttonShape)
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = null
                )
                .padding(horizontal = innerHorizontalPadding, vertical = innerVerticalPadding)
                .onSizeChanged {
                    spacerHeight = with(density) { it.height.toDp() + (innerVerticalPadding) }
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