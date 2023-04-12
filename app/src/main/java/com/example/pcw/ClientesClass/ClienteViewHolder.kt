package com.example.pcw.ClientesClass

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pcw.ClienteItemResponse
import com.example.pcw.ClienteSendResponse

import com.example.pcw.databinding.ItemClienteBinding



class ClienteViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val binding = ItemClienteBinding.bind(view)

    fun bind(item: ClienteItemResponse, onItemSelected: (ClienteSendResponse) -> Unit) {
        binding.tvClienteNombre.text = item.name
        binding.root.setOnClickListener { onItemSelected(ClienteSendResponse(item.id.toInt(),item.name))}
    }


}