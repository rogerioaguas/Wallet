package com.example.brunocolombini.wallet.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {

    @GET("/olinda/servico/PTAX/versao/v1/odata/CotacaoMoedaDia(moeda=@moeda,dataCotacao=@dataCotacao)?%40moeda=%27EUR%27&%40dataCotacao=%2704-04-2018%27&%24format=json")
    fun getBritasPrice(): Single<BancoCentralModel>

    @GET
    fun getBtcPrice(@Url url: String = "https://www.mercadobitcoin.net/api/BTC/ticker/"): Single<MercadoBitcoinModel>


}
