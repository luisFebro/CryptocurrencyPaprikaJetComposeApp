package com.plcoding.cryptocurrencyappyt.presentation.coin_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptocurrencyappyt.common.Constants
import com.plcoding.cryptocurrencyappyt.common.Resource
import com.plcoding.cryptocurrencyappyt.domain.use_case.get_coin.GetCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    // contains nav parameters exp - don't need pass parameters: https://youtu.be/EF33KmyprEQ?t=3325s
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // why private and public explain - only viewModel have access to mutable state and immutable state to composables for separate of concerns: https://youtu.be/EF33KmyprEQ?t=2975s
    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            getCoin(coinId)
        }

    }

    private fun getCoin(coinId: String) {
        // call it like a function with invoke - exp: https://youtu.be/EF33KmyprEQ?t=3033s
        getCoinUseCase(coinId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = CoinDetailState(coin = result.data)
                }
                is Resource.Error -> {
                    _state.value = CoinDetailState(error = result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = CoinDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope) // Terminal flow operator that launches the collection of the given flow in the scope. It is a shorthand for scope.launch { flow.collect() }.
    }

}