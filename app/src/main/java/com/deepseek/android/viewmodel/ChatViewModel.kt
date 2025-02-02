package com.deepseek.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepseek.android.model.ChatMessage
import com.deepseek.android.model.DeepSeekModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isGenerating: Boolean = false
)

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    private val model = DeepSeekModel()

    fun sendMessage(text: String) {
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )

        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMessage,
                isGenerating = true
            )
        }

        viewModelScope.launch {
            try {
                val response = model.generateResponse(text)
                val assistantMessage = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    text = response,
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + assistantMessage,
                        isGenerating = false
                    )
                }
            } catch (e: Exception) {
                val errorMessage = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    text = "Error: ${e.message}",
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + errorMessage,
                        isGenerating = false
                    )
                }
            }
        }
    }
}
