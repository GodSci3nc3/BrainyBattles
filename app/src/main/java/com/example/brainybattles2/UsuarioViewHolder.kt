package com.example.brainybattles2

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
    val tvPuntuacion: TextView = itemView.findViewById(R.id.tvPuntuacion)

    fun bind(usuario: Usuario) {
        tvNombre.text = usuario.nombre
        tvPuntuacion.text = usuario.puntuacion.toString()
    }
}
