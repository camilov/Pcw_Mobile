package com.example.pcw.AbonosClass

//import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pcw.Api.ApiService
import com.example.pcw.Api.ServiceBuilder
import com.example.pcw.ClientesClass.ClientesActivity
import com.example.pcw.DataResponse.*
import com.example.pcw.FuncionesClass.FuncionesResponse
import com.example.pcw.R
import com.example.pcw.constantes.Constantes
import com.example.pcw.databinding.ItemAbonosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AbonosActivity : AppCompatActivity() {

    private lateinit var binding: ItemAbonosBinding
    private lateinit var servicio: ApiService
    private var tlAbonos: TableLayout?=null
    private  var trAbono: TableRow?=null
    private var selectedRow: TableRow? = null

    private var idTarjeta: Number? = null
    private var idCliente: Number? = null
    private var valorTotal: Float? =  null
    private var valorDefecto: Number? =  null
    private var numCuotas: Number? =  null
    private var valorAbonoSelected: Number? =  null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemAbonosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idTarjeta = intent.extras?.getInt("ID_TARJETA")
        idCliente = intent.extras?.getInt("ID_CLIENTE")
        valorTotal = intent.extras?.getFloat("VALOR_TOTAL")
        valorDefecto = intent.extras?.getInt("VALOR_DEFECTO")
        numCuotas   = intent.extras?.getInt("NUM_CUOTAS")

        Log.d("scordsito","valordefecto= ${valorDefecto}")

        servicio =  ServiceBuilder.buildService(ApiService::class.java)
        initUI()
        initListeners()



        Log.d("scord1", "${idTarjeta}")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initListeners() {

        binding.btnAddAbono.setOnClickListener{addAbono()}
        binding.btnEditAbono.setOnClickListener{editAbono()}
        binding.btnDeleteAbono.setOnClickListener{deleteAbono()}

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addAbono() {

        val currentNumCuota   = binding.etNumCuotaAbonos.text.toString()
        val currentValorAbono = binding.etValorAbonoAbonos.text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val fechaActual = Date()
        val fechaFormateada = dateFormat.format(fechaActual)

        val currentValorToSum = currentValorAbono.toInt()

        valorTotal = valorTotal?.plus(currentValorToSum)
        val valorTotalD: Float? = valorTotal

        //Log.d("scordsito","$fechaFormateada")


        if(currentNumCuota != null && currentValorAbono != null){

            CoroutineScope(Dispatchers.IO).launch {
                // val myResponse:ApiService = ServiceBuilder.buildService(ApiService::class.java)
                //Log.d("scordsito", "$idTarjeta")
                /** ABONO*/
                val abonoData = AbonoItemResponse(
                    Constantes.CREATE_ID,
                    idTarjeta!!,
                    currentNumCuota.toInt(),
                    currentValorAbono.toFloat(),
                    fechaFormateada
                )


                /**
                 *val idMovimiento : Number,
                val entrada      : Float,
                val salida       : Float,
                val tipMvto      : String,
                val idTarjeta    : Number,
                val idCliente    : Number,
                val fecMvto      : String,
                val mcaAjuste    : Number*/

                /** MOVIMIENTO*/
                val movementData = AbonoMovementResponse(
                    Constantes.CREATE_ID,
                    currentValorAbono.toFloat(),
                    Constantes.CERO.toFloat(),
                    Constantes.ABONO,
                    idTarjeta!!,
                    idCliente!!,
                    fechaFormateada,
                    0
                )

                /** ACTUALIZACION DE TARJETA*/
                val tarjetaData = AbonoModifyTarjeta(valorTotalD, currentNumCuota.toInt(), fechaFormateada)

                val abonoRequestData = AbonoRequestData(abonoData, movementData, tarjetaData)//


                Log.d("scordsito","$abonoRequestData")
                servicio.addAbono(abonoRequestData).enqueue(
                    object: Callback<AbonoItemResponse> {
                        override fun onResponse(
                            call: Call<AbonoItemResponse>,
                            response: Response<AbonoItemResponse>
                        ) {
                            initUI()
                            Toast.makeText(this@AbonosActivity,"Se Añadio abono corretamente",Toast.LENGTH_LONG)
                        }


                        override fun onFailure(call: Call<AbonoItemResponse>, t: Throwable) {
                            Toast.makeText(this@AbonosActivity,"Error",Toast.LENGTH_LONG)
                        }
                    }
                )
            }

            val resultIntent = Intent()
            resultIntent.putExtra("VALOR_TOTAL", valorTotalD)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun editAbono(){

        val currentNumCuota = binding.etNumCuotaAbonos.text.toString()
        val currentValorAbono = binding.etValorAbonoAbonos.text.toString()
        val currentValorAbonoInt:Number=currentValorAbono.toInt()
        val currentIdAbono = binding.etIdAbono.text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val fechaActual = Date()
        val fechaFormateada = dateFormat.format(fechaActual)
        val abonoDifference = currentValorAbonoInt- valorAbonoSelected as Int

        Log.d("scordsito","valorAbonoSelected= ${valorAbonoSelected}")
        Log.d("scordsito","currentValorAbono= ${currentValorAbono}")
        Log.d("scordsito","valorTotal= ${valorTotal}")
        Log.d("scordsito","abonoDifference= ${abonoDifference}")

        val valorTotalD: Float? = abonoDifference?.let { valorTotal?.plus(it.toFloat()) }

        Log.d("scordsito","valorTotalD= ${valorTotalD}")

        if(currentNumCuota != null && currentValorAbono != null)
        {
            CoroutineScope(Dispatchers.IO).launch {

                /** ABONO*/
                val abonoData = AbonoModifyResponse(currentNumCuota.toInt(),currentValorAbono.toFloat())

                /** TARJETA*/
                /** ACTUALIZACION DE TARJETA*/
                val tarjetaData = AbonoModifyTarjeta(valorTotalD, currentNumCuota.toInt(), fechaFormateada)

               /**
                    val valorTotal : Float?,
                    val numCuotas: Number,
                    val fecActu: String*/

               /** MOVIMIENTO*/
               val movementData = AbonoMovementResponse(
                   Constantes.CREATE_ID,
                   Constantes.CERO.toFloat(),
                   currentValorAbono.toFloat(),
                   Constantes.ANULACION_ABONO,
                   idTarjeta!!,
                   idCliente!!,
                   fechaFormateada,
                   0
               )

                val abonoModifyRequestData = AbonoRequestModifyData(abonoData ,tarjetaData, movementData)
                //currentIdAbono.toInt(),idTarjeta,Constantes.ABONO,
                servicio.modifyAbono(currentIdAbono.toInt(),idTarjeta,Constantes.ABONO,abonoModifyRequestData).enqueue(
              //  servicio.modifyAbono(currentIdAbono.toInt(),idTarjeta,Constantes.ABONO).enqueue(
                    object: Callback<AbonoSendModifyResponse> {
                        override fun onResponse(
                            call: Call<AbonoSendModifyResponse>,
                            response: Response<AbonoSendModifyResponse>
                        ) {
                            initUI()
                            Toast.makeText(this@AbonosActivity,"Se Modifico abono corretamente",Toast.LENGTH_LONG)
                        }

                        override fun onFailure(call: Call<AbonoSendModifyResponse>, t: Throwable) {
                            Toast.makeText(this@AbonosActivity,"Error",Toast.LENGTH_LONG)
                        }

                    }
                )
            }
            //Toast.makeText(this, "${currentIdAbono}", Toast.LENGTH_SHORT).show();
        }

    }


    //////////////////////////////////////////////////////////////////////////////////VOY ACA////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun deleteAbono(){

        val currentNumCuota = binding.etNumCuotaAbonos.text.toString()
        val currentValorAbono = binding.etValorAbonoAbonos.text.toString()
        val currentValorAbonoInt:Number=currentValorAbono.toInt()
        val currentIdAbono = binding.etIdAbono.text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val fechaActual = Date()
        val fechaFormateada = dateFormat.format(fechaActual)

        val valorTotalParcial = valorTotal?.minus(currentValorAbonoInt.toFloat())
        val valorTotalD: Float? = valorTotalParcial?.toFloat()

        valorTotal = valorTotalD

        if(currentNumCuota != null && currentValorAbono != null)
        {
            CoroutineScope(Dispatchers.IO).launch {

                val AbonoDeleteResponse= AbonoDeleteResponse("")


                /** TARJETA*/
                /** ACTUALIZACION DE TARJETA*/
                val tarjetaData = AbonoModifyTarjeta(valorTotalD, currentNumCuota.toInt(), fechaFormateada)

                /** MOVIMIENTO*/
                val movementData = AbonoMovementResponse(
                    Constantes.CREATE_ID,
                    Constantes.CERO.toFloat(),
                    currentValorAbono.toFloat(),
                    Constantes.ANULACION_ABONO,
                    idTarjeta!!,
                    idCliente!!,
                    fechaFormateada,
                    0
                )
                val abonoRequestDeleteData = AbonoRequestDeleteData(tarjetaData, movementData)

                servicio.deleteAbono(currentIdAbono.toInt(),idTarjeta,abonoRequestDeleteData).enqueue(
                    object: Callback<AbonoDeleteResponse> {
                        override fun onResponse(
                            call: Call<AbonoDeleteResponse>,
                            response: Response<AbonoDeleteResponse>
                        ) {
                            initUI()
                            Toast.makeText(this@AbonosActivity,"Se Elimino abono corretamente",Toast.LENGTH_LONG)
                        }

                        override fun onFailure(call: Call<AbonoDeleteResponse>, t: Throwable) {
                            Toast.makeText(this@AbonosActivity,"Error al eliminar abono",Toast.LENGTH_LONG)
                        }

                    }
                )
            }
            //Toast.makeText(this, "${currentIdAbono}", Toast.LENGTH_SHORT).show();
        }


    }

    private fun selectedRow() {

        val tableLayout = findViewById<TableLayout>(R.id.tlAbonos)

        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow

            tableRow.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    // Deseleccionar la fila anteriormente seleccionada
                    selectedRow?.setBackgroundColor(Color.WHITE)

                    // Guardar la fila seleccionada actualmente
                    selectedRow = tableRow

                    // Cambiar el color de fondo de la fila seleccionada
                    tableRow.setBackgroundColor(Color.GRAY)
                   // var tb = view as TableRow
                    var abono = tableRow.getChildAt(0) as TextView
                    var abonoCodigo = abono.text.toString()

                    var cuota = tableRow.getChildAt(1) as TextView
                    var cuoutaCodigo = cuota.text.toString()

                    var valor = tableRow.getChildAt(2) as TextView
                    var valorCodigo = valor.text.toString()

                   // Log.d("scordsito","$controlCodigo")
                    binding.etNumCuotaAbonos.setText( cuoutaCodigo)
                    binding.etValorAbonoAbonos.setText( valorCodigo)
                    binding.etIdAbono.setText(abonoCodigo)

                    valorAbonoSelected = valorCodigo.toInt()


                }
            })
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun fillTable(abonosList: List<AbonoItemResponse>) {

        binding.tlAbonos.removeAllViews()

        Log.d("scordsito","entra a fill table")

        val headAbono = LayoutInflater.from(this).inflate(R.layout.head_table_abonos,null,false)
        binding.tlAbonos.addView(headAbono)

        if(abonosList.size > 0) {

            Log.d("scordsito","entra a if fill table")
            for (abonosLists in abonosList) {

                val registro =
                    LayoutInflater.from(this).inflate(R.layout.row_table_abonos, null, false)
                val tvIdAbono = registro.findViewById<View>(R.id.tvIdAbono) as TextView
                val tvNumCuota = registro.findViewById<View>(R.id.tvNumCuota) as TextView
                val tvValorAbono = registro.findViewById<View>(R.id.tvValorAbono) as TextView

                //val tvTrAbonos: TextView = binding.trAbonos.findViewById(com.example.pcw.R.id.tvTrAbonos)


                tvIdAbono.text = abonosLists.idAbono.toString()
                tvNumCuota.text = abonosLists.numCuota.toString()
                tvValorAbono.text = abonosLists.valorAbono.toInt().toString()

                tvIdAbono.setVisibility(View.GONE)

                binding.tlAbonos.addView(registro)

            }
        }else{

            Log.d("scordsito","entra a else de fill table")
            val registro =
                LayoutInflater.from(this).inflate(R.layout.row_table_abonos, null, false)
                binding.tlAbonos.addView(registro)
            Log.d("scordsito","valordefectodespues ${valorDefecto}")
            if(valorDefecto != 0){

                binding.etValorAbonoAbonos.setText( valorDefecto.toString())
               binding.etNumCuotaAbonos.setText( FuncionesResponse.UNO.toString())
            }

        }


    }

    private fun initUI() {

        binding.etIdAbono.setVisibility(View.GONE);

        CoroutineScope(Dispatchers.IO).launch {

            val myResponse: Response<AbonosDataResponse> =
            //retrofit.create(ApiService::class.java).getClienteDetail(query.toString())
                // ServiceBuilder.buildService(ApiService::class.java).getClienteDetail(query.toString())
               // Log.d("scord1", "${idTarjeta}")
                servicio.getAbonosDetail(idTarjeta)
            if (myResponse.isSuccessful) {
                Log.i("scord1", "funciona: ")
                val response: AbonosDataResponse? = myResponse.body()
                Log.i("scord1", "response: ${response}")
                if (response != null) {
                    Log.i("scord1", "Respuesta" + response.toString())
                    runOnUiThread {
                       // adapterClienteTarjeta.updateList(response.Lista)
                        fillTable(response.Lista)
                        selectedRow()
                        ////////////////////////

                        /*binding.progressBar.isVisible = false*/ }
                    }
                } else {
                    Log.i("scord1", "No funciona: ")
                }
        }
    }
}

private operator fun Number?.minus(toInt: Int): Number? {
    return this?.toDouble()?.minus(toInt)
}

private operator fun Number?.plus(toInt: Int): Number? {
    return this?.toDouble()?.plus(toInt)
}





