package com.plcoding.cryptocurrencyappyt.domain.use_case.get_coin

import com.plcoding.cryptocurrencyappyt.common.Resource
import com.plcoding.cryptocurrencyappyt.data.remote.dto.toCoinDetail
import com.plcoding.cryptocurrencyappyt.domain.model.CoinDetail
import com.plcoding.cryptocurrencyappyt.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// use case can only have one public function - https://youtu.be/EF33KmyprEQ?t=1936s
class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository
){

    // operator in general is required whenever you wish to be able to use a function as if it were an operator, since operator usages are simply compiled to function calls (except on primitive types, etc.)
    // That is, foo += bar, for example, is equivalent to foo.plusAssign(bar), foo[bar] = baz is equivalent to foo.set(bar, baz), etc.
    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading())
            val coin = repository.getCoinById(coinId).toCoinDetail()

            // data we need to forward to ViewModel
            emit(Resource.Success<CoinDetail>(coin))
        } catch (e: HttpException) {
            emit(Resource.Error<CoinDetail>(e.localizedMessage ?: "An unexpected error occured"))
            // explain: https://youtu.be/EF33KmyprEQ?t=2100s
        } catch (e: IOException) {
            // internet connection failure, server offline
            emit(Resource.Error<CoinDetail>("Couldn't reach server. Check your internet connection."))
        }
    }
}