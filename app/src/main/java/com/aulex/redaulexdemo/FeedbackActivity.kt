package com.aulex.redaulexdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aulex.redaulexdemo.ui.theme.RedAulexDemoTheme

class FeedbackActivity : ComponentActivity() {
    private val feedbackViewModel: FeedbackViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedAulexDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FeedbackScreen(feedbackViewModel)
                }
            }
        }
    }
}

@Composable
fun FeedbackScreen(viewModel: FeedbackViewModel) {

    var feedback by remember {
        mutableStateOf<String>("")
    }

    var rate by remember { mutableIntStateOf(0) }

    val allFeedback by viewModel.allFeedbackEntries.collectAsState(initial = ArrayList<FeedbackEntity>())

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
            Text(text = "Оставьте свой отзыв о приложении!", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            TextField(value = feedback,
                onValueChange = { feedback = it },
                label = { Text(text = "Отзыв") })
            Spacer(modifier = Modifier.height(20.dp))
            StarRating(
                rating = rate,
                onRatingChanged = { newRating -> rate = newRating }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { viewModel.saveFeedback(rate, feedback) }) {
                Text(text = "Отправить отзыв")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Предыдущие отзывы", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                if (allFeedback.isEmpty()) {
                    item {
                        Text("Отзывов ещё нет")
                    }
                } else {
                    items(allFeedback) {
                        Surface(
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                            {
                                Text(text = "Оценка: ${it.rate}⭐")
                                Text(it.feedback)
                            }
                        }
                    }
                }
            }

    }
}

@Composable
fun StarRating(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    filledColor: Color = Color(0xFFFFD700),
    unfilledColor: Color = Color.Gray
) {
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Star $i",
                tint = if (i <= rating) filledColor else unfilledColor,
                modifier = Modifier
                    .clickable { onRatingChanged(i) }
                    .padding(2.dp)
            )
        }
    }
}