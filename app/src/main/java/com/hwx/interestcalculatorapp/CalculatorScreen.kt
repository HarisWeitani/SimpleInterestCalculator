package com.hwx.interestcalculatorapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hwx.interestcalculatorapp.ui.theme.InterestCalculatorAppTheme
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    val snackbarHostState = SnackbarHostState()
    var amountMoney by remember {
        mutableStateOf(0.0)
    }
    var amountInterest by remember {
        mutableStateOf(0.0)
    }
    var amountMonths by remember {
        mutableStateOf(0)
    }
    var result by remember {
        mutableStateOf("")
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Simple Interest Calculator",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text =  "Amount Money")
                InputField(
                    modifier = Modifier.padding(start = 16.dp),
                    placeHolder = "Rp 100,000",
                    input = { inputValue ->
                        amountMoney = inputValue
                    }
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text =  "Interest p.a")
                InputField(
                    modifier = Modifier.padding(start = 16.dp),
                    placeHolder = "x %",
                    input = { inputValue ->
                        amountInterest = inputValue
                    }
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text =  "Month Invest")
                InputField(
                    modifier = Modifier.padding(start = 16.dp),
                    placeHolder = "x Months",
                    input = { inputValue ->
                        amountMonths = inputValue.toInt()
                    }
                )
            }
            Row(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Interest Result"
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = result
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                onClick = {
                    result = calculateInterestResult(amountMoney,amountInterest,amountMonths)
                }
            ) {
                Text(text = "Wadaw")
            }
        }
    }
}

fun calculateInterestResult(
    money: Double,
    rate: Double,
    month: Int
) : String {
    return ((month/12)*(rate/100)*money).toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    placeHolder: String = "",
    input: (Double) -> Unit,
) {
    var data by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = modifier,
        value = try {
            data.toDouble().toFormat()
        }catch (e:Exception) {""},
        onValueChange = { onValueChange ->
            try {
                data = onValueChange
                input(onValueChange.toDouble())
            } catch (e:Exception) { }
        },
        placeholder = {
            Text(text = placeHolder)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        visualTransformation = CurrencyVisualTransformation(),
        singleLine = true
    )
}

fun Double.toFormat(): String {
    val nf = NumberFormat.getNumberInstance(Locale.ENGLISH)
    val formatter = nf as DecimalFormat
    formatter.applyPattern("#,###,###,###,###.###")
    return "Rp " + formatter.format(this)
}

class CurrencyVisualTransformation() : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += "-"
        }

        /**
         * The offset translator should ignore the hyphen characters, so conversion from
         *  original offset to transformed text works like
         *  - The 4th char of the original text is 5th char in the transformed text.
         *  - The 13th char of the original text is 15th char in the transformed text.
         *  Similarly, the reverse conversion works like
         *  - The 5th char of the transformed text is 4th char in the original text.
         *  - The 12th char of the transformed text is 10th char in the original text.
         */
        val creditCardOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }

        return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InterestCalculatorAppTheme {
        CalculatorScreen()
    }
}

/**
 * Requirement Interest Calculator App
 *
 *  User Input :
 *  - Amount Money
 *  - Interest Rate ( p.a )
 *  - Month Deposit
 *
 *  Output :
 *  - Amount interest
 *  - Total money
 *  - Daily interest money
 *  - Monthly interest money
 */