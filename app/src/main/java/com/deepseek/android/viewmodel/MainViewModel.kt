package com.deepseek.android.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deepseek.android.ml.ModelManager
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val modelManager = ModelManager(application)
    private val _generatedText = mutableStateOf("")
    val generatedText: State<String> = _generatedText

    init {
        viewModelScope.launch {
            try {
                modelManager.initialize()
            } catch (e: Exception) {
                _generatedText.value = "Error initializing model: ${e.message}"
            }
        }
    }

    suspend fun generateText(prompt: String) {
        try {
            _generatedText.value = "Generating..."
            val result = modelManager.generateText(prompt)
            _generatedText.value = result
        } catch (e: Exception) {
            _generatedText.value = "Error generating text: ${e.message}"
            throw e
        }
    }

    override fun onCleared() {
        super.onCleared()
        modelManager.release()
    }
}
