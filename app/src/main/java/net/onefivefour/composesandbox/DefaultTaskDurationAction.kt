package net.onefivefour.composesandbox

sealed interface DefaultTaskDurationAction {
    data class OnEnterNumber(val number: Int?, val index: Int): DefaultTaskDurationAction
    data class OnFocusChange(val index: Int): DefaultTaskDurationAction
    data object OnKeyboardBack: DefaultTaskDurationAction
}