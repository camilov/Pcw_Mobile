package com.example.pcw.TarjetasClass

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pcw.ClienteItemResponse
import com.example.pcw.ClienteSendResponse
import com.example.pcw.DataResponse.TarjetasItemResponse
import com.example.pcw.databinding.ItemClienteTarjetaBinding

class TarjetasViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemClienteTarjetaBinding.bind(view)

    fun bind(item: ClienteItemResponse, onItemSelected: (ClienteSendResponse) -> Unit) {
        binding.tvClienteTarjeta.text = item.name
        binding.root.setOnClickListener { onItemSelected(ClienteSendResponse(item.id.toInt(),item.name))}
    }

}