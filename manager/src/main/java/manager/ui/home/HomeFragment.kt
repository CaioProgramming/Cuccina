package manager.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ilustris.mylibrary.databinding.FragmentHomeBinding
import com.silent.ilustriscore.core.model.ViewModelBaseState

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getAllData()
        observeViewModel()
    }

    private fun observeViewModel() {
        homeViewModel.viewModelState.observeForever {
            when (it) {
                ViewModelBaseState.RequireAuth -> Toast.makeText(
                    requireContext(),
                    "Usuário desconectado",
                    Toast.LENGTH_SHORT
                ).show()
                ViewModelBaseState.DataDeletedState -> TODO()
                ViewModelBaseState.LoadingState -> TODO()
                is ViewModelBaseState.DataRetrievedState -> TODO()
                is ViewModelBaseState.DataListRetrievedState -> {
                    Toast.makeText(
                        requireContext(),
                        "${it.dataList.size} receitas encontradas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ViewModelBaseState.DataSavedState -> TODO()
                is ViewModelBaseState.DataUpdateState -> TODO()
                is ViewModelBaseState.FileUploadedState -> TODO()
                is ViewModelBaseState.ErrorState -> Toast.makeText(
                    requireContext(),
                    "Ocorreu um erro desconhecido",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}