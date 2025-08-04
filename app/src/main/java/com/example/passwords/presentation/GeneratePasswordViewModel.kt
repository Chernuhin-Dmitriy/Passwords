package com.example.passwords.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri
import com.example.passwords.data.file.FileHandler
import com.example.passwords.domain.entity.CharacterSetConfig
import com.example.passwords.domain.entity.Password
import com.example.passwords.domain.use_cases.GeneratePasswordUseCase
import com.example.passwords.domain.use_cases.ImportPasswordsUseCase
import com.example.passwords.domain.use_cases.SavePasswordUseCase

@HiltViewModel
class GeneratePasswordViewModel @Inject constructor(
    private val generatePasswordUseCase: GeneratePasswordUseCase,
    private val savePasswordUseCase: SavePasswordUseCase,
    private val importPasswordsUseCase: ImportPasswordsUseCase,
    private val fileHandler: FileHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(GeneratePasswordUiState())
    val uiState: StateFlow<GeneratePasswordUiState> = _uiState.asStateFlow()

    fun updateCharacterSetConfig(config: CharacterSetConfig) {
        _uiState.value = _uiState.value.copy(characterSetConfig = config)
    }

    fun generatePassword() {
        val config = _uiState.value.characterSetConfig
        val generatedPassword = generatePasswordUseCase(config)

        _uiState.value = _uiState.value.copy(
            generatedPassword = generatedPassword.password,
            entropy = generatedPassword.entropy,
            characterSet = generatedPassword.characterSet
        )
    }

    fun saveGeneratedPassword() {
        val state = _uiState.value
        if (state.generatedPassword.isNotBlank()) {
            viewModelScope.launch {
                val password = Password(
                    value = state.generatedPassword,
                    entropy = state.entropy,
                    characterSet = state.characterSet
                )
                savePasswordUseCase(password)
                _uiState.value = state.copy(showSaveSuccess = true)
            }
        }
    }

    fun importPasswordsFromFile(uri: Uri, fileName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isImporting = true)

            val passwords = fileHandler.readPasswordsFromFile(uri)
            if (passwords.isNotEmpty()) {
                importPasswordsUseCase(fileName, passwords)
                _uiState.value = _uiState.value.copy(
                    isImporting = false,
                    showImportSuccess = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isImporting = false,
                    showImportError = true
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            showSaveSuccess = false,
            showImportSuccess = false,
            showImportError = false
        )
    }
}

data class GeneratePasswordUiState(
    val characterSetConfig: CharacterSetConfig = CharacterSetConfig(),
    val generatedPassword: String = "",
    val entropy: Double? = null,
    val characterSet: String = "",
    val isImporting: Boolean = false,
    val showSaveSuccess: Boolean = false,
    val showImportSuccess: Boolean = false,
    val showImportError: Boolean = false
)