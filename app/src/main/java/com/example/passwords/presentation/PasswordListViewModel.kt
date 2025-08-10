package com.example.passwords.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri
import com.example.passwords.data.file.FileHandler
import com.example.passwords.domain.entity.Password
import com.example.passwords.domain.repository.FolderRepository
import com.example.passwords.domain.use_cases.DeletePasswordUseCase
import com.example.passwords.domain.use_cases.ExportPasswordsUseCase
import com.example.passwords.domain.use_cases.GetPasswordsUseCase

@HiltViewModel
class PasswordListViewModel @Inject constructor(
    private val getPasswordsUseCase: GetPasswordsUseCase,
    private val deletePasswordUseCase: DeletePasswordUseCase,
    private val exportPasswordsUseCase: ExportPasswordsUseCase,
    private val folderRepository: FolderRepository,
    private val fileHandler: FileHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordListUiState())
    val uiState: StateFlow<PasswordListUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getPasswordsUseCase(),
                folderRepository.getAllFolders()
            ) { passwords, folders ->
                val groupedPasswords = passwords.groupBy { it.folderId }
                val passwordGroups = mutableListOf<PasswordGroup>()

                val generatedPasswords = groupedPasswords[null] ?: emptyList()
                if (generatedPasswords.isNotEmpty()) {
                    passwordGroups.add(
                        PasswordGroup(
                            name = "Generated Passwords",
                            passwords = generatedPasswords,
                            isGenerated = true
                        )
                    )
                }

                folders.forEach { folder ->
                    val folderPasswords = groupedPasswords[folder.id] ?: emptyList()
                    if (folderPasswords.isNotEmpty()) {
                        passwordGroups.add(
                            PasswordGroup(
                                name = folder.name,
                                passwords = folderPasswords,
                                folderId = folder.id,
                                isGenerated = false
                            )
                        )
                    }
                }

                _uiState.value = _uiState.value.copy(
                    passwordGroups = passwordGroups,
                    isLoading = false
                )
            }.collect { }
        }
    }

    fun deletePassword(password: Password) {
        viewModelScope.launch {
            deletePasswordUseCase(password)
        }
    }

    fun exportPasswords(group: PasswordGroup, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true)

            val passwords = exportPasswordsUseCase(group.folderId)
            val success = fileHandler.writePasswordsToFile(uri, passwords)

            _uiState.value = _uiState.value.copy(
                isExporting = false,
                showExportSuccess = success,
                showExportError = !success
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            showExportSuccess = false,
            showExportError = false
        )
    }
}

data class PasswordListUiState(
    val passwordGroups: List<PasswordGroup> = emptyList(),
    val isLoading: Boolean = true,
    val isExporting: Boolean = false,
    val showExportSuccess: Boolean = false,
    val showExportError: Boolean = false
)

data class PasswordGroup(
    val name: String,
    val passwords: List<Password>,
    val folderId: Long? = null,
    val isGenerated: Boolean = false
)