package com.example.myai

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.example.myai.ui.theme.ColorRed
import com.example.myai.ui.theme.ColorUserMessage
import com.example.myai.ui.theme.ColorWhite
import com.example.myai.ui.theme.fontFamily
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        AppHeader()

        MessageList(
            modifier = Modifier.weight(1f),
            messageList = viewModel.messageList
        )

        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            MessageInput(
                onMessageSend = { viewModel.sendMessage(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LogoutButton { logout(context) }
        }
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(ColorBlack)
            .border(3.dp, ColorBlack, shape = RoundedCornerShape(24.dp))
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = ColorRed),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Logout",
                color = ColorWhite,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
        }
    }
}

fun logout(context: Context) {
    val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    with(sharedPref.edit()) {
        putBoolean("IS_LOGGED_IN", false)
        apply()
    }

    val intent = Intent(context, LoginActivity::class.java)

    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    context.startActivity(intent)
}

@Composable
fun AppHeader() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(ColorNavy, darkIcons = true)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorNavy)
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "My AI",
            color = ColorWhite,
            fontSize = 32.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {
    var message by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = if (isFocused) ColorNavy else ColorBlack
    val borderWidth = if (isFocused) 3.dp else 1.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(ColorWhite, shape = RoundedCornerShape(24.dp))
            .border(borderWidth, borderColor, shape = RoundedCornerShape(24.dp))
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
                        text = "Type a message . . . .",
                        fontFamily = fontFamily,
                        color = ColorGrey,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .padding(start = 4.dp)
                    )
                }
                BasicTextField(
                    value = message,
                    onValueChange = { message = it },
                    textStyle = TextStyle(fontSize = 16.sp, color = ColorBlack, fontFamily = fontFamily),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .padding(start = 4.dp),
                    interactionSource = interactionSource
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
                    imageVector = Icons.AutoMirrored.Filled.Send,
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
            Image(
                modifier = Modifier.size(100.dp).padding(bottom = 16.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Icon"
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
                        top = 12.dp,
                        bottom = 12.dp
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