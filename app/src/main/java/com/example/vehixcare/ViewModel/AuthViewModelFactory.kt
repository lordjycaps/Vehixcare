import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.ViewModel.AuthViewModel

class AuthViewModelFactory(
    private val repository: VehixcareRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
