package com.example.pcw.Api

import com.example.pcw.*
import com.example.pcw.DataResponse.AbonosDataResponse
import com.example.pcw.DataResponse.TarjetasDataResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /************************  RUTAS TARJETAS  *****************/
    @GET("/api/clientes/{nombre}")
    suspend fun getClienteDetail(@Path("nombre") clienteId:String):Response<ClientesDataResponse>


    @POST("/api/create_clientes/")
    fun addUser(@Body userData: ClienteSendDataResponse): Call<ClienteSendResponse>

    @PUT("/api/modificar_Cliente/{idCliente}")
    fun modifyClient(@Path("idCliente") clienteId:Int,
                     @Body userData: ClienteModifyResponse
    ): Call<ClienteSendModifyResponse>

    /**********************  RUTAS TARJETAS  **********************/
    @GET("/api/tarjetas_id/{idCliente}")
    suspend fun getClientTarjetaDetail(@Path ("idCliente") tarjetaId: Number?): Response<TarjetasDataResponse>


    /**********************  RUTAS ABONOS  **********************/
    @GET("/api/abonos/{idTarjeta}")
    suspend fun getAbonosDetail(@Path ("idTarjeta") tarjetaId: Number?): Response<AbonosDataResponse>




}