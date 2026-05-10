package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.ui.res.painterResource


// Main entry point of the app
class MainActivity : ComponentActivity() {

    // Runs when the app is first created
    override fun onCreate(savedInstanceState: Bundle?) {

        // Allows the app to draw edge-to-edge on the screen
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        // Starts the Jetpack Compose UI
        setContent {

            // Applies the app theme
            TipTimeTheme {

                // Surface acts like a container/background for the app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    // Displays the main layout composable
                    TipTimeLayout()
                }
            }
        }
    }
}

// Main UI composable for the app
@Composable
fun TipTimeLayout() {

    // Stores the bill amount input from the user
    var amountInput by remember { mutableStateOf("") }

    // Stores the tip percentage input from the user
    var tipInput by remember { mutableStateOf("") }

    // Converts text input into numbers safely
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    // Stores whether the user wants to round up the tip
    var roundUp by remember { mutableStateOf(false) }

    // Calls the tip calculation function
    val tip = calculateTip(amount, tipPercent, roundUp)

    // Column stacks all UI elements vertically
    Column(
        modifier = Modifier
            .statusBarsPadding() // keeps content below status bar
            .padding(horizontal = 40.dp) // horizontal spacing
            .safeDrawingPadding() // prevents overlap with device edges
            .verticalScroll(rememberScrollState()), // allows scrolling
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // App title text
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        // Bill amount text field
        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            value = amountInput,

            // Updates amountInput whenever user types
            onValueChange = { amountInput = it },

            // Opens numeric keyboard and moves to next field
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),

            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        // Tip percentage text field
        EditNumberField(
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            value = tipInput,

            // Updates tipInput whenever user types
            onValueChange = { tipInput = it },

            // Opens numeric keyboard and finishes input
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),

            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        // Switch row for rounding the tip
        RoundTheTipRow(
            roundUp = roundUp,

            // Updates roundUp state when switch changes
            onRoundUpChanged = { roundUp = it },

            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Displays the calculated tip amount
        Text(
            text = stringResource(R.string.tip_amount, tip),

            // Uses Material Design typography styling
            style = MaterialTheme.typography.displaySmall
        )

        // Adds spacing at the bottom
        Spacer(modifier = Modifier.height(150.dp))
    }
}


// Reusable text field composable
@Composable
fun EditNumberField(

    // Label text resource ID
    @StringRes label: Int,

    // Icon resource ID
    @DrawableRes leadingIcon: Int,

    // Keyboard settings
    keyboardOptions: KeyboardOptions,

    // Current text value
    value: String,

    // Function called whenever text changes
    onValueChange: (String) -> Unit,

    modifier: Modifier = Modifier
) {

    // Material Design text field
    TextField(

        // Current value displayed
        value = value,

        // Displays icon inside text field
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                null
            )
        },

        // Updates state when user types
        onValueChange = onValueChange,

        modifier = modifier,

        // Displays label text
        label = {
            Text(stringResource(label))
        },

        // Restricts field to one line
        singleLine = true,

        // Controls keyboard behavior
        keyboardOptions = keyboardOptions
    )
}


// Row containing the round-up text and switch
@Composable
fun RoundTheTipRow(

    // Current switch state
    roundUp: Boolean,

    // Callback when switch value changes
    onRoundUpChanged: (Boolean) -> Unit,

    modifier: Modifier =
        Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
) {

    // Text beside the switch
    Text(text = stringResource(R.string.round_up_tip))

    // Switch component
    Switch(

        // Current switch value
        checked = roundUp,

        // Updates the roundUp state
        onCheckedChange = onRoundUpChanged,
    )
}


/**
 * Function that calculates the tip amount
 */
private fun calculateTip(
    amount: Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean
): String {

    // Calculates the tip
    var tip = tipPercent / 100 * amount

    // Rounds tip upward if switch is enabled
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }

    // Formats result as currency
    return NumberFormat.getCurrencyInstance().format(tip)
}


// Preview shown in Android Studio design view
@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {

    // Displays preview with app theme
    TipTimeTheme {
        TipTimeLayout()
    }
}