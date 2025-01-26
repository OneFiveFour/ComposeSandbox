package net.onefivefour.composesandbox

data class DefaultTaskDurationState (
    val times : List<Int?> = (1..3).map { null },
    val focusedIndex: Int? = null
)