package com.example.passwords.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passwords.R
import com.example.passwords.domain.entity.CharacterSetConfig
import com.example.passwords.presentation.GeneratePasswordViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratePasswordScreen(
    viewModel: GeneratePasswordViewModel = hiltViewModel(),
    onNavigateToPasswordList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.importPasswordsFromFile(it, "Imported_${System.currentTimeMillis()}")
        }
    }

    LaunchedEffect(uiState.showSaveSuccess, uiState.showImportSuccess, uiState.showImportError) {
        if (uiState.showSaveSuccess || uiState.showImportSuccess || uiState.showImportError) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.grad),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    title = { Text("Generate Password", color = Color.Black) },
                    actions = {
                        IconButton(onClick = onNavigateToPasswordList) {
                            Icon(Icons.Default.List, contentDescription = "View passwords")
                        }
                    }
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Password Configuration",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Length: ${uiState.characterSetConfig.length}")
                            Text(
                                text = getSecurityLevel(uiState.characterSetConfig.length),
                                color = getSliderColor(uiState.characterSetConfig.length),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Slider(
                            value = uiState.characterSetConfig.length.toFloat(),
                            onValueChange = {
                                viewModel.updateCharacterSetConfig(
                                    uiState.characterSetConfig.copy(length = it.toInt())
                                )
                            },
                            valueRange = 4f..50f,
                            steps = 45,
                            colors = SliderDefaults.colors(
                                thumbColor = getSliderColor(uiState.characterSetConfig.length),
                                activeTrackColor = getSliderColor(uiState.characterSetConfig.length),
                                inactiveTrackColor = getSliderColor(uiState.characterSetConfig.length).copy(
                                    alpha = 0.3f
                                )
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.characterSetConfig.includeUppercase,
                                onCheckedChange = {
                                    viewModel.updateCharacterSetConfig(
                                        uiState.characterSetConfig.copy(includeUppercase = it)
                                    )
                                }
                            )
                            Text("A-Z", fontSize = 14.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.characterSetConfig.includeLowercase,
                                onCheckedChange = {
                                    viewModel.updateCharacterSetConfig(
                                        uiState.characterSetConfig.copy(includeLowercase = it)
                                    )
                                }
                            )
                            Text("a-z", fontSize = 14.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.characterSetConfig.includeNumbers,
                                onCheckedChange = {
                                    viewModel.updateCharacterSetConfig(
                                        uiState.characterSetConfig.copy(includeNumbers = it)
                                    )
                                }
                            )
                            Text("0-9", fontSize = 14.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.characterSetConfig.includeSpecialChars,
                                onCheckedChange = {
                                    viewModel.updateCharacterSetConfig(
                                        uiState.characterSetConfig.copy(includeSpecialChars = it)
                                    )
                                }
                            )
                            Text("!@#$%", fontSize = 14.sp)
                        }
                    }
                }

                Button(
                    onClick = { viewModel.generatePassword() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = hasAtLeastOneCharacterType(uiState.characterSetConfig)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Password")
                }

                if (uiState.generatedPassword.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Generated Password",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = uiState.generatedPassword,
                                onValueChange = { },
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp
                                ),
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(uiState.generatedPassword))
                                        }
                                    ) {
                                        Icon(Icons.Default.Done, contentDescription = "Copy")
                                    }
                                }
                            )

                            uiState.entropy?.let { entropy ->
                                val decimalFormat = DecimalFormat("#.##")
                                Text(
                                    text = "Entropy: ${decimalFormat.format(entropy)} bits",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            // Character set display
                            if (uiState.characterSet.isNotBlank()) {
                                Text(
                                    text = "Character set: ${uiState.characterSet}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.saveGeneratedPassword() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Save Password")
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Import Passwords",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Load passwords from a text file. Each line should contain one password.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Button(
                            onClick = { filePickerLauncher.launch("text/*") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isImporting
                        ) {
                            if (uiState.isImporting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Importing...")
                            } else {
                                Icon(Icons.Default.ExitToApp, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Select File")
                            }
                        }
                    }
                }

                if (uiState.showSaveSuccess) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Text(
                            text = "✓ Password saved successfully!",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                if (uiState.showImportSuccess) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Text(
                            text = "✓ Passwords imported successfully!",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                if (uiState.showImportError) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "✗ Failed to import passwords. Please check the file format.",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getSliderColor(length: Int): Color {
    return when (length) {
        in 4..20 -> Color(0xFFC0342B)
        in 21..40 -> Color(0xFFC99910)
        in 41..50 -> Color(0xFF4CAF50)
        else -> MaterialTheme.colorScheme.primary
    }
}

fun getSecurityLevel(length: Int): String {
    return when (length) {
        in 4..20 -> "WEAK"
        in 21..40 -> "MEDIUM"
        in 41..50 -> "STRONG"
        else -> "UNKNOWN"
    }
}

fun hasAtLeastOneCharacterType(config: CharacterSetConfig): Boolean {
    return config.includeUppercase ||
            config.includeLowercase ||
            config.includeNumbers ||
            config.includeSpecialChars
}

