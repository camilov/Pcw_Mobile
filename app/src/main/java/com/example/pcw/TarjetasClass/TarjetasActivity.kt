package com.example.pcw.TarjetasClass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcw.Api.ApiService
import com.example.pcw.ClienteSendResponse
import com.example.pcw.ClientesClass.ClientesActivity
import com.example.pcw.ClientesDataResponse
import com.example.pcw.DataResponse.TarjetasDataResponse
import com.example.pcw.DataResponse.TarjetasItemResponse
import com.example.pcw.databinding.ItemTarjetasBinding
import com.example.pcw.Api.ServiceBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TarjetasActivity : AppCompatActivity() {


    private lateinit var binding: ItemTarjetasBinding
    private lateinit var servicio: ApiService
    private lateinit var adapterClienteTarjeta: TarjetasAdapter
    private lateinit var adapterTarjetaCliente: TarjetasClienteAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemTarjetasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        servicio = ServiceBuilder.buildService(ApiService::class.java)
        initUI()

    }

   /* object ServiceBuilder {
        private val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") //
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        fun<T> buildService(service: Class<T>): T{
            return retrofit.create(service)
        }
    }*/


    private fun initUI() {

        binding.searchClienteTarjetas.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        adapterClienteTarjeta = TarjetasAdapter{navigateToDetailClientesTarjeta(it)}
        binding.rvClientesTarjetas .setHasFixedSize(true)
        binding.rvClientesTarjetas.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvClientesTarjetas.adapter = adapterClienteTarjeta
    }
    private fun navigateToDetailClientesTarjeta (clienteSendResponse: ClienteSendResponse){
        /*val intent = Intent(this,DetailSuperheroActivity::class.java)
        intent.putExtra(EXTRA_ID,id)
        startActivity(intent)*/
        showTarjetas(clienteSendResponse)
    }

    private fun navigateToTarjetas(tarjetasItemResponse: TarjetasItemResponse){

        val intent =  Intent(this,TarjetaOperationActivity::class.java)
        intent.putExtra("ID_TARJETA"    ,tarjetasItemResponse.idTarjeta    )
        intent.putExtra("ID_CLIENTE"    ,tarjetasItemResponse.idCliente    )
        intent.putExtra("ID_INTERES"    ,tarjetasItemResponse.interes      )
        intent.putExtra("ID_ESTADO"     ,tarjetasItemResponse.idEstado     )
        intent.putExtra("FEC_PRESTAMO"  ,tarjetasItemResponse.fechaPrestamo)
        intent.putExtra("NUM_CUOTAS"    ,tarjetasItemResponse.numCuotas    )
        intent.putExtra("VALOR_DEFECTO" ,tarjetasItemResponse.valorDefecto )
        intent.putExtra("VALOR_PRESTADO",tarjetasItemResponse.valorPrestado)
        intent.putExtra("VALOR_TOTAL"   ,tarjetasItemResponse.valorTotal   )
        startActivity(intent)

    }


    private fun showTarjetas(clienteSendResponse: ClienteSendResponse) {

        //Log.i("camilo", "entra a showTarjetas: ")
        CoroutineScope(Dispatchers.IO).launch {

            //Log.i("camilo", "${clienteSendResponse.idCliente}")

            val myResponse: Response<TarjetasDataResponse> =
            //retrofit.create(ApiService::class.java).getClienteDetail(query.toString())
                // ServiceBuilder.buildService(ApiService::class.java).getClienteDetail(query.toString())
                servicio.getClientTarjetaDetail(clienteSendResponse.idCliente)
           // Log.i("camilo", "${myResponse}: ")

            if (myResponse.isSuccessful) {
               // Log.i("camilo", "funciona: ")
                val response: TarjetasDataResponse? = myResponse.body()
                //Log.i("camilo", "response: ${response}")
                if (response != null) {
                    //Log.i("camilo", "Respuesta"+response.toString())

                    runOnUiThread {
                        createTarjetaCliente(response)
                        /*binding.progressBar.isVisible = false*/ }
                }
            } else {
                Log.i("camilo", "No funciona: ")
            }
        }
    }

    private fun createTarjetaCliente(response: TarjetasDataResponse) {

        adapterTarjetaCliente = TarjetasClienteAdapter{navigateToTarjetas(it)}
        binding.rvTarjetasClientes .setHasFixedSize(true)
        binding.rvTarjetasClientes.layoutManager = LinearLayoutManager(this)
        binding.rvTarjetasClientes.adapter = adapterTarjetaCliente


        adapterTarjetaCliente.updateList(response.lista)
    }


    private fun searchByName(query: String?) {
       // binding.progressBar.isVisible = true

        CoroutineScope(Dispatchers.IO).launch {

            val myResponse: Response<ClientesDataResponse> =
            //retrofit.create(ApiService::class.java).getClienteDetail(query.toString())
                // ServiceBuilder.buildService(ApiService::class.java).getClienteDetail(query.toString())
                servicio.getClienteDetail(query.toString())
            if (myResponse.isSuccessful) {
                Log.i("cristian", "funciona: ")
                val response: ClientesDataResponse? = myResponse.body()
                Log.i("cristian", "response: ${response}")
                if (response != null) {
                    Log.i("cristian", "Respuesta"+response.toString())
                    runOnUiThread {
                        adapterClienteTarjeta.updateList(response.Lista)
                        /*binding.progressBar.isVisible = false*/ }
                }
            } else {
                Log.i("cristian", "No funciona: ")
            }
        }
    }
}