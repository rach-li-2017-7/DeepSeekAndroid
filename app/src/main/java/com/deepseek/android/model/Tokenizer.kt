package com.deepseek.android.model

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class Tokenizer(private val context: Context) {
    private lateinit var vocabulary: Map<String, Int>
    private lateinit var reverseVocabulary: Map<Int, String>
    
    init {
        loadVocabulary()
    }
    
    private fun loadVocabulary() {
        val jsonString = context.assets.open("vocab.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        
        vocabulary = mutableMapOf<String, Int>().apply {
            jsonObject.keys().forEach { key ->
                put(key, jsonObject.getInt(key))
            }
        }
        
        reverseVocabulary = vocabulary.entries.associate { (k, v) -> v to k }
    }
    
    fun encode(text: String): List<Int> {
        val tokens = mutableListOf<Int>()
        
        // Add start token
        tokens.add(vocabulary["<s>"] ?: 0)
        
        // Tokenize the text
        // This is a simple implementation; you'll need to implement proper BPE tokenization
        text.split(" ").forEach { word ->
            vocabulary[word]?.let { tokens.add(it) } ?: run {
                // Handle unknown tokens
                tokens.add(vocabulary["<unk>"] ?: 1)
            }
        }
        
        // Add end token
        tokens.add(vocabulary["</s>"] ?: 2)
        
        return tokens
    }
    
    fun decode(tokens: List<Int>): String {
        return tokens.mapNotNull { token ->
            reverseVocabulary[token]?.takeUnless { 
                it in setOf("<s>", "</s>", "<unk>") 
            }
        }.joinToString(" ")
    }
}
