package net.onefivefour.composesandbox

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

val outerHorizontalPadding = 24.dp

@Composable
fun ClippedShadowButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int? = null,
    onClick: () -> Unit
) {


    val outerVerticalPadding = 24.dp

    val innerVerticalPadding = 14.dp
    val innerHorizontalPadding = 24.dp

    val buttonShape = RoundedCornerShape(size = 16.dp)

    val contentColor = Color(0xFF8391A8)

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
                    indication = ScaleIndication
                )
                .background(color = Color(0xFFFFFFFF), shape = buttonShape)
                .clip(shape = buttonShape)
                .padding(horizontal = innerHorizontalPadding, vertical = innerVerticalPadding)
        ) {

            ButtonContent(
                iconRes = iconRes,
                contentColor = contentColor,
                text = text,
                alpha = 0f
            )
        }

        ButtonContent(
            iconRes,
            contentColor,
            text
        )
    }
}

@Composable
private fun BoxScope.ButtonContent(
    iconRes: Int?,
    contentColor: Color,
    text: String,
    alpha: Float = 1f
) {
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


object ScaleIndication : Indication {
    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val instance = remember(interactionSource) { ScaleIndicationInstance() }

        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> instance.animateToPressed()
                    is PressInteraction.Release -> instance.animateToResting()
                    is PressInteraction.Cancel -> instance.animateToResting()
                }
            }
        }

        return instance
    }
}

private class ScaleIndicationInstance : IndicationInstance {
    val animatedPercent = Animatable(1f)

    suspend fun animateToPressed() {
        animatedPercent.animateTo(0f, spring())
    }

    suspend fun animateToResting() {
        animatedPercent.animateTo(1f, spring())
    }


    override fun ContentDrawScope.drawIndication() {

        val radius = size.minDimension / 1.8f

        val offsetLeft = Offset(
            x = outerHorizontalPadding.toPx() / 2f,
            y = size.height / 2f
        )

        val offsetRight = Offset(
            x = size.width - (outerHorizontalPadding.toPx() / 2f),
            y = size.height / 2f
        )

        val animatedAlpha = 0.6f + (0.4f * animatedPercent.value)

        val brushLeft = Brush.radialGradient(
            colors = listOf(
                Color(color = 0xFFA2A2A2),
                Color(color = 0xFFD3D3D3),
                Color.Transparent
            ),
            radius = radius,
            center = offsetLeft
        )

        val brushRight = Brush.radialGradient(
            colors = listOf(
                Color(color = 0xFFA2A2A2),
                Color(color = 0xFFD3D3D3),
                Color.Transparent
            ),
            radius = radius,
            center = offsetRight
        )

        scale(
            scaleX = 0.95f + (0.05f * animatedPercent.value),
            scaleY = 1f
        ) {
            drawCircle(
                brush = brushLeft,
                radius = radius,
                center = offsetLeft,
                alpha = animatedAlpha
            )
            drawCircle(
                brush = brushRight,
                radius = radius,
                center = offsetRight,
                alpha = animatedAlpha
            )

            this@drawIndication.drawContent()
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