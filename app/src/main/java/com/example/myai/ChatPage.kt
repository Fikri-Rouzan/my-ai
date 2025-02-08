package com.example.myai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myai.ui.theme.ColorBlack
import com.example.myai.ui.theme.ColorGrey
import com.example.myai.ui.theme.ColorModelMessage
import com.example.myai.ui.theme.ColorNavy
import com.example.myai.ui.theme.ColorPurple
import com.example.myai.ui.theme.ColorUserMessage
import com.example.myai.ui.theme.ColorWhite
import com.example.myai.ui.theme.fontFamily
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(modifier = Modifier) {
        AppHeader()
        MessageList(
            modifier = Modifier.weight(1f),
            messageList = viewModel.messageList
        )
        MessageInput(
            onMessageSend = {
            viewModel.sendMessage(it)
            }
        )
    }
}

@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(ColorNavy, darkIcons = false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorNavy)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .height(56.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My AI",
            color = ColorWhite,
            fontSize = 22.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(bottom = 24.dp)
            .background(ColorWhite, shape = RoundedCornerShape(24.dp))
            .border(4.dp, ColorNavy, shape = RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                if (message.isEmpty()) {
                    Text(
                        text = "Type a message...",
                        fontFamily = fontFamily,
                        color = ColorGrey,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                BasicTextField(
                    value = message,
                    onValueChange = { message = it },
                    textStyle = TextStyle(fontSize = 16.sp, color = ColorBlack, fontFamily = fontFamily),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .padding(start = 4.dp)
                )
            }
            IconButton(
                onClick = {
                    if (message.isNotEmpty()) {
                        onMessageSend(message)
                        message = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (message.isNotEmpty()) ColorPurple else ColorGrey
                )
            }
        }
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList : List<MessageModel>) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.baseline_chat_24),
                contentDescription = "Icon",
                tint = ColorPurple
            )
            Text(
                text = "Ask me anything",
                fontSize = 22.sp,
                fontFamily = fontFamily
            )
        }
    } else {
        LazyColumn (
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()) {
                MessageRow(messageModel = it)
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"
    val bubbleShape = RoundedCornerShape(16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(if (isModel) Alignment.CenterStart else Alignment.CenterEnd)
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .border(3.dp, ColorBlack, bubbleShape)
                    .background(if (isModel) ColorModelMessage else ColorUserMessage, bubbleShape)
                    .sizeIn(minHeight = 40.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                SelectionContainer {
                    Text(
                        text = messageModel.message.trim(),
                        fontWeight = FontWeight.W500,
                        color = ColorBlack,
                        fontFamily = fontFamily
                    )
                }
            }
        }
    }
}