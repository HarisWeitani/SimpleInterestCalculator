package com.hwx.interestcalculatorapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hwx.interestcalculatorapp.ui.theme.InterestCalculatorAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    val snackbarHostState = SnackbarHostState()
    var name by remember {
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
                Text(text =  "Input Amount Money")
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                    }
                )
            }

        }
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