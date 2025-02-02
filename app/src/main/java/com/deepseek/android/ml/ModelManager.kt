package com.deepseek.android.ml

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ModelManager(private val context: Context) {
    private var model: Module? = null
    private var tokenizer: HuggingFaceTokenizer? = null
    
    companion object {
        private const val TAG = "ModelManager"
        private const val MODEL_FILENAME = "deepseek_model.pt"
        private const val VOCAB_FILENAME = "tokenizer.json"
        private const val MAX_LENGTH = 512
    }

    suspend fun initialize() = withContext(Dispatchers.IO) {
        try {
            // Load the model
            val modelFile = loadModelFile()
            model = Module.load(modelFile.absolutePath)
            
            // Initialize tokenizer
            val vocabFile = loadVocabFile()
            tokenizer = HuggingFaceTokenizer(vocabFile)
            
            Log.i(TAG, "Model and tokenizer initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing model", e)
            throw e
        }
    }

    private fun loadModelFile(): File {
        return copyAssetToCache(MODEL_FILENAME)
    }

    private fun loadVocabFile(): File {
        return copyAssetToCache(VOCAB_FILENAME)
    }

    private fun copyAssetToCache(assetName: String): File {
        val cacheFile = File(context.cacheDir, assetName)
        if (cacheFile.exists()) {
            return cacheFile
        }

        context.assets.open(assetName).use { input ->
            FileOutputStream(cacheFile).use { output ->
                input.copyTo(output)
            }
        }
        return cacheFile
    }

    suspend fun generateText(prompt: String): String = withContext(Dispatchers.Default) {
        try {
            // Tokenize input
            val inputIds = tokenizer?.encode(prompt, maxLength = MAX_LENGTH)
                ?: throw IllegalStateException("Tokenizer not initialized")

            // Convert to tensor
            val inputTensor = Tensor.fromBlob(
                inputIds.toIntArray(),
                longArrayOf(1, inputIds.size.toLong())
            )

            // Run inference
            val outputs = model?.forward(IValue.from(inputTensor))
                ?: throw IllegalStateException("Model not initialized")

            // Process output tensor
            val outputTensor = outputs.toTensor()
            val outputArray = outputTensor.dataAsFloatArray

            // Decode output tokens
            val result = tokenizer?.decode(outputArray.map { it.toInt() }.toIntArray())
                ?: throw IllegalStateException("Failed to decode output")

            result
        } catch (e: Exception) {
            Log.e(TAG, "Error generating text", e)
            throw e
        }
    }

    fun release() {
        model?.destroy()
        model = null
        tokenizer = null
    }
}
