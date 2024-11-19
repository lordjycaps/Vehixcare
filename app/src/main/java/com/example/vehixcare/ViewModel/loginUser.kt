package com.example.vehixcare.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.Model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: VehixcareRepository) : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> get() = _loginSuccess

    private val _showLoginError = MutableStateFlow(false)
    val showLoginError: StateFlow<Boolean> get() = _showLoginError

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    fun login(correo: String, password: String) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(correo)
            if (user != null && user.password == password) {
                _loginSuccess.value = true
                _currentUser.value = user // Guardar usuario autenticado
                _showLoginError.value = false
            } else {
                _loginSuccess.value = false
                _showLoginError.value = true
            }
        }
    }
}
