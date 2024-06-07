package com.example.brainybattles2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val title: LiveData<String> = _index.map { index ->
        when (index) {
            1 -> "¿Qué son las categorías de BrainyBattles?"
            2 -> "Ciencia"
            3 -> "Cine"
            else -> "Título desconocido"
        }
    }

    val text: LiveData<String> = _index.map { index ->
        when (index) {
            1 -> "BrainyBattles tiene varias categorías como Ciencia, Cine, etc."
            2 -> "La categoría de Ciencia incluye preguntas sobre física, química, biología, etc."
            3 -> "La categoría de cine cubre filmes de todo tipo y también su contenido."
            else -> "Texto desconocido"
        }
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}