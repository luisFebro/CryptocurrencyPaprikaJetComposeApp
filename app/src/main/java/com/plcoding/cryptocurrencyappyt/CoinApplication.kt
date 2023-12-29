package com.plcoding.cryptocurrencyappyt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// gives dagger hilt info about your app - explain: https://youtu.be/EF33KmyprEQ?t=2700s
@HiltAndroidApp
class CoinApplication: Application()