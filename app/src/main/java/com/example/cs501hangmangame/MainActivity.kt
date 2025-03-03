package com.example.cs501hangmangame

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun HangmanGameScreen(modifier: Modifier = Modifier) {
    // all game state management

    // word bank for hangman
    val wordList = listOf("KOTLIN", "ANDROID", "COMPOSE", "JETPACK", "HACKING", "COMMAND", "BACKEND")
    // choosing a random word to do the main game play on
    var wordToGuess by remember { mutableStateOf(wordList.random()) }
    // already guessed letters on keyboard using a set
    var selectedLetters by remember { mutableStateOf(setOf<Char>()) }
    // total of 6 guesses in the hangman game
    var remainingTurns by remember { mutableIntStateOf(6) }
    // keeping track of how many hints used
    var hintUsed by remember { mutableIntStateOf(0) }
    // boolean value for whether game is over or ongoing
    var gameOver by remember { mutableStateOf(false) }
    // keeping track of hint message
    var hintMessage by remember { mutableStateOf("") }
    // hide hint button after specific number of guesses
    var showHintButton by remember { mutableStateOf(true) }

    // hangman visuals to show the state of the game
    val hangmanPics = listOf(
        "+---+\n" +
                "  |   |\n" +
                "      |\n" +
                "      |\n" +
                "      |\n" +
                "      |\n" +
                "=========",
        "+---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                "      |\n" +
                "      |\n" +
                "      |\n" +
                "=========",
        "+---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                "  |   |\n" +
                "      |\n" +
                "      |\n" +
                "=========",
        "+---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                " /|   |\n" +
                "      |\n" +
                "      |\n" +
                "=========",
        "+---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                " /|\\  |\n" +
                "      |\n" +
                "      |\n" +
                "=========",
        "+---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                " /|\\  |\n" +
                " /    |\n" +
                "      |\n" +
                "=========",
        "+---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                " /|\\  |\n" +
                " / \\  |\n" +
                "      |\n" +
                "========="
    )

    // all the letters of alphabet A-Z
    val alphabet = ('A'..'Z').toList()

    val configuration = LocalConfiguration.current
    val landscape = Configuration.ORIENTATION_LANDSCAPE
    val portrait = Configuration.ORIENTATION_PORTRAIT

    // PORTRAIT MODE DEFINING LAYOUT
    if (configuration.orientation == portrait) {
        // no hint button if portrait
        showHintButton = false

        Column(
            modifier = modifier
                // fill max
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // hangman visualization
            Text(
                // choose the image based on remaining turns
                text = hangmanPics[6 - remainingTurns],
                style = TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 30.sp),
                modifier = Modifier.padding(16.dp)
            )

            // panel 3: main gameplay
            Text(
                // '_' for each letter in the word to guess
                text = wordToGuess.map {
                    if (it in selectedLetters) it else '_'
                }.joinToString(" "),
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(16.dp)
            )

            // panel 1: alphabet grid of buttons
            Text(
                text = "CHOOSE A LETTER",
                style = TextStyle(fontSize = 20.sp),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(alphabet.size) { index ->
                    val letter = alphabet[index]
                    val isSelected = selectedLetters.contains(letter)

                    // button for each letter
                    Button(
                        onClick = {
                            if (!gameOver) {
                                // add letter to selected set when clicked
                                selectedLetters += letter
                                // if the letter is not in the word to guess, subtract a turn
                                if (letter !in wordToGuess) remainingTurns--
                            }
                        },
                        // disable the button if it has already been selected
                        enabled = !isSelected && !gameOver,
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

            // hangman status aka remaining turns left
            Text(
                text = "$remainingTurns tries left",
                style = TextStyle(fontSize = 18.sp, color = Color.Red),
                modifier = Modifier.padding(16.dp)
            )

            // check if game is over
            if (remainingTurns <= 0 || wordToGuess.all { it in selectedLetters }) {
                // if the remaining turns go below 0 or if the word is fully guessed, the game is over
                gameOver = true
                // display win message if the player guessed the word and game over if not
                val resultMessage = if (wordToGuess.all { it in selectedLetters }) "YOU WIN!" else "GAME OVER!"
                Text(
                    text = resultMessage,
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Green),
                    modifier = Modifier.padding(16.dp)
                )
            }

            // new game button
            Button(
                onClick = {
                    wordToGuess = wordList.random()
                    selectedLetters = emptySet()
                    remainingTurns = 6
                    hintUsed = 0
                    gameOver = false
                    hintMessage = ""
                    showHintButton = true
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("New Game")
            }
        }
    }

    // LANDSCAPE ORIENTATION LAYOUT DEFINITION
    if (configuration.orientation == landscape) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                // keyboard and hint on left side
                // panel 1: alphabet grid of buttons
                Text(
                    text = "CHOOSE A LETTER",
                    style = TextStyle(fontSize = 50.sp),
                )

                val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                val halfScreenWidth = screenWidth / 2

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(100.dp), // setting an adaptive size instead of fixed
                    modifier = Modifier
                        .width(halfScreenWidth)
                        .padding(top = 16.dp)
                ) {
                    items(alphabet.size) { index ->
                        val letter = alphabet[index]
                        val isSelected = selectedLetters.contains(letter)

                        // button for each letter
                        Button(
                            onClick = {
                                if (!gameOver) {
                                    // add letter to selected set when clicked
                                    selectedLetters += letter
                                    // if the letter is not in the word to guess, subtract a turn
                                    if (letter !in wordToGuess) remainingTurns--
                                }
                            },
                            // disable the button if it has already been selected
                            enabled = !isSelected && !gameOver,
                            modifier = Modifier
                                .padding(1.dp)
                        ) {
                            Text(
                                text = letter.toString(),
                                style = TextStyle(fontSize = 15.sp),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                // panel 2: hint button
                if (showHintButton) {
                    Button(
                        onClick = {
                            when (hintUsed) {
                                0 -> {
                                    // first hint used is just a normal hint, but no turns deducted
                                    hintMessage = "The word is related to programming"
                                    hintUsed++
                                }
                                1 -> {
                                    // give a random letter for the second hint, 1 turn deducted
                                    val remainingLetters = alphabet.filter { it !in wordToGuess && it !in selectedLetters }
                                    remainingLetters.take(remainingLetters.size / 2).forEach {
                                        selectedLetters += it
                                    }
                                    remainingTurns--
                                    hintUsed++
                                }
                                2 -> {
                                    // last hint available, show all vowels, 1 turn deducted
                                    val vowels = listOf('A', 'E', 'I', 'O', 'U')
                                    vowels.forEach { selectedLetters += it }
                                    remainingTurns--
                                    showHintButton = false
                                }
                            }
                        },
                        // allow the hint button to be enabled if there are still turns and hints left and game is not over
                        enabled = remainingTurns > 0 && hintUsed < 3 && !gameOver,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Hint")
                    }
                    Text(text = hintMessage,
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // hangman visualization
                Text(
                    // choose the image based on remaining turns
                    text = hangmanPics[6 - remainingTurns],
                    style = TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 50.sp),
                    modifier = Modifier.padding(16.dp)
                )

                // panel 3: main gameplay
                Text(
                    // '_' for each letter in the word to guess
                    text = wordToGuess.map {
                        if (it in selectedLetters) it else '_'
                    }.joinToString(" "),
                    style = TextStyle(fontSize = 50.sp),
                    modifier = Modifier.padding(16.dp)
                )

                // hangman status aka remaining turns left
                Text(
                    text = "$remainingTurns tries left",
                    style = TextStyle(fontSize = 18.sp, color = Color.Red),
                    modifier = Modifier.padding(16.dp)
                )

                Row {
                    // new game button
                    Button(
                        onClick = {
                            wordToGuess = wordList.random()
                            selectedLetters = emptySet()
                            remainingTurns = 6
                            hintUsed = 0
                            gameOver = false
                            hintMessage = ""
                            showHintButton = true
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("New Game")
                    }

                    // check if game is over
                    if (remainingTurns <= 0 || wordToGuess.all { it in selectedLetters }) {
                        // if the remaining turns go below 0 or if the word is fully guessed, the game is over
                        gameOver = true
                        // display win message if the player guessed the word and game over if not
                        val resultMessage = if (wordToGuess.all { it in selectedLetters }) "YOU WIN!" else "GAME OVER!"
                        Text(
                            text = resultMessage,
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Green),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
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