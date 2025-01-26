package net.onefivefour.composesandbox

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultTaskDurationViewModel: ViewModel() {

    private val _state = MutableStateFlow(DefaultTaskDurationState())
    val state = _state.asStateFlow()

    fun onAction(action: DefaultTaskDurationAction) {
        when(action) {
            is DefaultTaskDurationAction.OnFocusChange-> {
                _state.update { it.copy(
                    focusedIndex = action.index
                ) }
            }
            is DefaultTaskDurationAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }
            DefaultTaskDurationAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                _state.update { it.copy(
                    times = it.times.mapIndexed { index, number ->
                        if(index == previousIndex) {
                            null
                        } else {
                            number
                        }
                    },
                    focusedIndex = previousIndex
                ) }
            }
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newTimes = state.value.times.mapIndexed { currentIndex, currentNumber ->
            if(currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _state.update { it.copy(
            times = newTimes,
            focusedIndex = if(wasNumberRemoved || it.times.getOrNull(index) != null) {
                it.focusedIndex
            } else {
                getNextFocusedTextFieldIndex(
                    currentTime = it.times,
                    currentFocusedIndex = it.focusedIndex
                )
            }
        ) }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentTime: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if(currentFocusedIndex == null) {
            return null
        }

        if(currentFocusedIndex == 2) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            times = currentTime,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        times: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        times.forEachIndexed { index, number ->
            if(index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if(number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }
}