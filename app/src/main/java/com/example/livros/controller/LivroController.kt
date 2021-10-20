package com.example.livros.controller

import com.example.livros.MainActivity
import com.example.livros.model.Livro
import com.example.livros.model.LivroDao
import com.example.livros.model.LivroSqlite

class LivroController(mainActivity: MainActivity) {
    private val livroDAO: LivroDao= LivroSqlite(mainActivity)

    fun inserirLivro(livro: Livro) = livroDAO.criarLivro(livro)
    fun buscarLivro(titulo: String) = livroDAO.recuperarLivro(titulo)
    fun buscarLivros() = livroDAO.recuperarLivros()
    fun modificarLivro(livro: Livro) = livroDAO.atualizarLivro(livro)
    fun apagarLivro(titulo: String) = livroDAO.removerLivro(titulo)
}