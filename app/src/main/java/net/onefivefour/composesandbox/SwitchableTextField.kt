package net.onefivefour.composesandbox

import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun SwitchTextField(
    initialText: String,
    textStyle: TextStyle = TextStyle.Default,
    onNewText: (String) -> Unit,
) {

    var isInEditMode by remember {
        mutableStateOf(false)
    }

    var currentText by remember {
        mutableStateOf(initialText)
    }

    if (isInEditMode) {

        InlineTextField(
            text = currentText,
            textStyle = textStyle,
            onSave = { newText ->
                currentText = newText
                onNewText(newText)
                isInEditMode = false
            }
        )

    } else {

        val interactionSource = remember { MutableInteractionSource() }

        Text(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { isInEditMode = true },
            text = currentText,
            style = textStyle
        )

    }
}

internal enum class KeyboardState {
    Opened, Closed
}

@Composable
internal fun keyboardAsState(): State<KeyboardState> {
    val keyboardState = remember { mutableStateOf(KeyboardState.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                KeyboardState.Opened
            } else {
                KeyboardState.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun InlineTextField(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    onSave: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }

    var isInitialKeyboardCheck by remember { mutableStateOf(true) }

    val keyboardState by keyboardAsState()

    val currentText = rememberTextFieldState(
        initialText = text,
        initialSelectionInChars = TextRange(0, text.length)
    )

    LaunchedEffect(keyboardState) {
        if (isInitialKeyboardCheck) {
            isInitialKeyboardCheck = false
            return@LaunchedEffect
        }
        if (keyboardState == KeyboardState.Closed) {
            onSave(currentText.text.toString())
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField2(
        modifier = modifier
            .focusRequester(focusRequester)
            .width(IntrinsicSize.Min),
        state = currentText,
        enabled = true,
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                onSave(currentText.text.toString())
                keyboardController?.hide()
            }
        ),
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Preview
@Composable
fun SwitchTextFieldPreview() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(10.dp)
    ) {
        SwitchTextField(
            "Initial Text",
            onNewText = { newText -> Log.d("+++", newText) }
        )
    }
}