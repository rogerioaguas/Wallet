package com.example.brunocolombini.wallet.data

data class BancoCentralModel(val value: List<FiatPrice>)

data class FiatPrice(val paridadeCompra: Double,
                     val paridadeVenda: Double,
                     val cotacaoCompra: Double,
                     val cotacaoVenda: Double,
                     val dataHoraCotacao: String,
                     val tipoBoletim: String)