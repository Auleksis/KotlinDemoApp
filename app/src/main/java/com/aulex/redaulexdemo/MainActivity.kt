package com.aulex.redaulexdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aulex.redaulexdemo.ui.theme.RedAulexDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedAulexDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TestCompose()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Composable
fun Link(text: String = "", url: String = "") {
    val mContext = LocalContext.current
    val annotatedString: AnnotatedString = buildAnnotatedString {
        append(text)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ),
            start = 0,
            end = text.length
        )
        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = 0,
            end = text.length
        )
    }

    Text(
        text = annotatedString,
        fontSize = 18.sp,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            mContext.startActivity(intent)
        }
    )
}

@Preview
@Composable
fun TestCompose() {
    val mContext = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(Color.White)
        .padding(20.dp, 20.dp)
        .fillMaxSize()) {
        Text("RED AULEX DEMO", style = MaterialTheme.typography.headlineLarge)
        Text("Добро пожаловать в демо-приложение от разработчика red_aulex (Ганиев Александр N3349)!", textAlign = TextAlign.Center)
        Link(text = "Перейти к Github профилю", url = "https://github.com/Auleksis")

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
            Button(onClick = { mContext.startActivity(Intent(mContext, RestApiActivity::class.java))}) {
                Text(text = "Посмотреть погоду")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = { mContext.startActivity(Intent(mContext, FeedbackActivity::class.java))}) {
                Text(text = "Дать обратную связь")
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RedAulexDemoTheme {
        Greeting("Android")
    }
}