package com.plcoding.cryptocurrencyappyt.presentation.coin_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptocurrencyappyt.common.Resource
import com.plcoding.cryptocurrencyappyt.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {

    // why private and public explain - only viewModel have access to mutable state and immutable state to composables for separate of concerns: https://youtu.be/EF33KmyprEQ?t=2975s
    private val _state = mutableStateOf(CoinListState())
    val state: State<CoinListState> = _state

    // val state by mutableStateOf(CoinListState()) // n1 chatGPT diff between sintax

    init {
        getCoins()
    }

    private fun getCoins() {
        // call it like a function with invoke - exp: https://youtu.be/EF33KmyprEQ?t=3033s
        getCoinsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = CoinListState(coins = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = CoinListState(error = result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = CoinListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope) // Terminal flow operator that launches the collection of the given flow in the scope. It is a shorthand for scope.launch { flow.collect() }.
    }

}

/*

Both options accomplish the same goal of creating a mutable state in your ViewModel, but they have slight differences in terms of syntax and usage.

Option A:
```kotlin
private val _state = mutableStateOf(CoinListState())
val state: State<CoinListState> = _state
```

In this option, you create a private mutable state variable `_state` and expose it as an immutable `State` property named `state`. This approach gives you more control over the mutable state within the ViewModel.

Option B:
```kotlin
val state by mutableStateOf(CoinListState())
```

In this option, you use property delegation with `by mutableStateOf` to create a public property `state` that is backed by the mutable state. This is a more concise syntax and is commonly used when you want a simple way to create and manage mutable state in a ViewModel.

Both options are valid, and the choice between them depends on your preferences and requirements. If you prefer explicit control over the mutable state variable, Option A might be a better fit. If you want a more concise and Kotlin-like syntax, Option B is a good choice.

 */