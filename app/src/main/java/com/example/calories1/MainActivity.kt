package com.example.calories1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieScreen()
        }
    }
}

@Composable
fun CalorieScreen() {
    var weight by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var intensity by remember { mutableIntStateOf(1) }
    var result by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Heading(title = "Calories")
        Spacer(modifier = Modifier.height(8.dp))
        WeightField(weight = weight, onWeightChange = { weight = it }) // "Enter weight" label added here
        Spacer(modifier = Modifier.height(8.dp))
        GenderChoices(isMale = isMale, onGenderChange = { isMale = it })
        Spacer(modifier = Modifier.height(8.dp))
        IntensityList(selectedIntensity = intensity, onIntensityChange = { intensity = it })
        Spacer(modifier = Modifier.height(16.dp))
        Calculation(weight = weight, isMale = isMale, intensity = intensity.toFloat(), onResultChange = { result = it })
        Text(text = "Calories: $result")
    }
}

@Composable
fun Heading(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
}

@Composable
fun WeightField(weight: String, onWeightChange: (String) -> Unit) {
    OutlinedTextField(
        value = weight,
        onValueChange = onWeightChange,
        label = { Text("Enter weight") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun GenderChoices(isMale: Boolean, onGenderChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        RadioButton(selected = isMale, onClick = { onGenderChange(true) })
        Text(text = "Male")
        Spacer(modifier = Modifier.width(16.dp))
        RadioButton(selected = !isMale, onClick = { onGenderChange(false) })
        Text(text = "Female")
    }
}

@Composable
fun IntensityList(selectedIntensity: Int, onIntensityChange: (Int) -> Unit) {
    val intensities = listOf("Light", "Usual", "Moderate", "High", "Very high")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(intensities[selectedIntensity]) }

    OutlinedTextField(
        value = selectedText,
        onValueChange = {},
        label = { Text("Select intensity") },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier.clickable { expanded = !expanded }
            )
        },
        modifier = Modifier.fillMaxWidth()
    )

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        intensities.forEachIndexed { index, label ->
            DropdownMenuItem(
                onClick = {
                    selectedText = label
                    expanded = false
                    onIntensityChange(
                        when (label) {
                            "Light" -> 1.3f
                            "Usual" -> 1.5f
                            "Moderate" -> 1.7f
                            "Hard" -> 2.0f
                            "Very hard" -> 2.2f
                            else -> 0.0f
                        }.toInt()
                    )
                },
                text = { Text(text = label) }
            )
        }
    }
}

@Composable
fun Calculation(weight: String, isMale: Boolean, intensity: Float, onResultChange: (Int) -> Unit) {
    val weightValue = weight.toFloatOrNull() ?: 0.0f

    val baseMetabolicRate = if (isMale) 879 else 795
    val weightFactor = if (isMale) 10.2f else 7.18f

    val result = ((baseMetabolicRate + weightValue * weightFactor) * intensity).toInt()
    onResultChange(result)
}

@Preview(showBackground = true)
@Composable
fun PreviewCalorieScreen() {
    CalorieScreen()
}
