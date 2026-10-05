package com.example.myai

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    private val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-3.8-flash",
        apiKey = BuildConfig.API_KEY
    )

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun sendMessage(question : String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) {
                            text(it.message)
                        }
                    }.toList()
                )

                messageList.add(MessageModel(question, "user"))
                messageList.add(MessageModel("Typing...", "model"))

                val response = chat.sendMessage(question)

                messageList.removeLast()
                messageList.add(MessageModel(response.text.toString(), "model"))
            } catch (e : Exception) {
                if (messageList.isNotEmpty() && messageList.last().message == "Typing...") {
                    messageList.removeLast()
                }

                val errorMessage = if (e.message?.contains("503") == true || e.message?.contains("high demand") == true) {
                    "AI server is currently busy. Please try sending again in a few moments!"
                } else {
                    "Error connecting. Please try again."
                }

                messageList.add(MessageModel(errorMessage, "model"))
            }
        }
    }
}