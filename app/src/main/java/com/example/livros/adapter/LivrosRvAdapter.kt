package com.example.livros.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.livros.OnLivroClickListener
import com.example.livros.R
import com.example.livros.databinding.LayoutLivroBinding
import com.example.livros.model.Livro

class LivrosRvAdapter(
    private val  eventClickLivro: OnLivroClickListener,
    private val livrosList: MutableList<Livro>
): RecyclerView.Adapter<LivrosRvAdapter.LivroLayoutHolder>() {

    //posicao que sera recuperado pelo menu de contexto
    var posicao: Int = -1

    //view holder
    inner class LivroLayoutHolder(layoutLivroBinding: LayoutLivroBinding): RecyclerView.ViewHolder(layoutLivroBinding.root), View.OnCreateContextMenuListener{
        val tituloTv: TextView = layoutLivroBinding.tituloTv
        val primeiroAutorTv: TextView = layoutLivroBinding.primeiroAutorTv
        val editoraTv: TextView =  layoutLivroBinding.editoraTv

        init{
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_main, menu)
        }
    }

    //quando uma nova celular precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroLayoutHolder {
        //criar celula
        val layoutLivroBinding = LayoutLivroBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //criar viewHolder associado a nova celula
        return LivroLayoutHolder(layoutLivroBinding)
    }

    //quando precisar atualizar valores da celula
    override fun onBindViewHolder(holder: LivroLayoutHolder, position: Int) {
        //busca livro
        val livro = livrosList[position]

        //atualizar valor do viewHolder
        with(holder) {
            tituloTv.text = livro.titulo
            primeiroAutorTv.text = livro.primeiroAutor
            editoraTv.text = livro.editora
            itemView.setOnClickListener {
                eventClickLivro.onLivroClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }

    //retorna a quantidade de itens
    override fun getItemCount(): Int= livrosList.size
}