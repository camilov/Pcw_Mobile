package com.example.pcw.Api

import com.example.pcw.*
import com.example.pcw.DataResponse.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /************************  RUTAS TARJETAS  *****************/
    /** Consultar cliente por nombre**/
    @GET("/api/clientes/{nombre}")
    suspend fun getClienteDetail(@Path("nombre") clienteId:String):Response<ClientesDataResponse>

    /** Crear cliente**/
    @POST("/api/create_clientes/")
    fun addUser(@Body userData: ClienteSendDataResponse): Call<ClienteSendResponse>

    /** Modificar cliente**/
    @PUT("/api/modificar_Cliente/{idCliente}")
    fun modifyClient(@Path("idCliente") clienteId:Int,
                     @Body userData: ClienteModifyResponse
    ): Call<ClienteSendModifyResponse>

    /**********************  RUTAS TARJETAS  **********************/
    /** Consultar tarjetas por id de cliente**/
    @GET("/api/tarjetas_id/{idCliente}")
    suspend fun getClientTarjetaDetail(@Path ("idCliente") tarjetaId: Number?): Response<TarjetasDataResponse>

    @POST("/api/create_tarjeta/")
    fun addTarjeta(@Body tarjetaData: TarjetasItemResponse): Call<TarjetasCreateResponse>


    /**********************  RUTAS ABONOS  **********************/
    /** Consultar abonos por id de tarjeta**/
    @GET("/api/abonos/{idTarjeta}")
    suspend fun getAbonosDetail(@Path ("idTarjeta") tarjetaId: Number?): Response<AbonosDataResponse>

    /** Crear abono**/
    @POST("/api/create_abono/")
    fun addAbono(@Body abonoData: AbonoItemResponse): Call<AbonoItemResponse>

}