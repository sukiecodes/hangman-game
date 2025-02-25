package com.example.cs501hangmangame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs501hangmangame.ui.theme.CS501HangmanGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CS501HangmanGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HangmanGameScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// panel 1
@Composable
fun HangmanGameScreen(modifier: Modifier = Modifier) {
    // state for selected letters
    var selectedLetters by remember { mutableStateOf(setOf<Char>()) }

    // all the letters of alphabet A-Z
    val alphabet = ('A'..'Z').toList()

    val maxColumns = 7

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // panel 1: alphabet grid of buttons
        Text(
            text = "Choose a letter",
            style = TextStyle(fontSize = 20.sp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(maxColumns),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            items(alphabet.size) { index ->
                val letter = alphabet[index]
                val isSelected = selectedLetters.contains(letter)

                // button for each letter
                Button(
                    onClick = {
                        // add letter to selected set when clicked
                        selectedLetters = selectedLetters + letter
                    },
                    enabled = !isSelected,  // disable the button if it has already been selected
                    modifier = Modifier.padding(1.dp)
                ) {
                    Text(
                        text = letter.toString(),
                        style = TextStyle(fontSize = 15.sp),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // new game button and resets selected letters set
        Button(onClick = { selectedLetters = emptySet() }) {
            Text("New Game")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CS501HangmanGameTheme {
        HangmanGameScreen()
    }
}