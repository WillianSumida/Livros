package com.example.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.livros.adapter.LivrosAdapter
import com.example.livros.databinding.ActivityMainBinding
import com.example.livros.model.Livro

class MainActivity : AppCompatActivity() {

    companion object Extras{
        const val EXTRA_LIVRO = "EXTRA_LIVRO"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var livroActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarLivroActivityResultLauncher: ActivityResultLauncher<Intent>

    //data source
    private val livrosList: MutableList<Livro> = mutableListOf()

    //adapter
    /*private val livrosAdapter: ArrayAdapter<String> by lazy{
        ArrayAdapter(this, android.R.layout.simple_list_item_1, livrosList.run {
            val livroStringList = mutableListOf<String>()
            this.forEach { livro -> livroStringList.add(livro.toString()) }
            livroStringList
        })
    }*/

    private val livrosAdapter: LivrosAdapter by lazy{
        LivrosAdapter(this, R.layout.layout_livro, livrosList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        //iniciar lista de livros
        inicializarLivrosList()

        //associar adapter ao listview
        activityMainBinding.livrosLv.adapter = livrosAdapter


        registerForContextMenu(activityMainBinding.livrosLv)


        livroActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado->

            if (resultado.resultCode == RESULT_OK) {
               resultado.data?.getParcelableExtra<Livro>(EXTRA_LIVRO)?.apply {
                   livrosAdapter.add(this)
                   //livrosList.add(this)
                   //livrosAdapter.notifyDataSetChanged()
               }
            }
        }

        editarLivroActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Livro>(EXTRA_LIVRO)?.apply {
                    if(posicao != null && posicao != -1) {
                        livrosList[posicao] = this
                        livrosAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.livrosLv.setOnItemClickListener{_, _, posicao, _->
            val livro = livrosList[posicao]
            val consultarLivroIntent = Intent(this, LivroActivity::class.java)
            consultarLivroIntent.putExtra(EXTRA_LIVRO, livro)
            startActivity(consultarLivroIntent)
        }

        activityMainBinding.adicionarLivroFab.setOnClickListener{
            livroActivityResultLauncher.launch(Intent(this, LivroActivity::class.java))
        }
    }

    private fun inicializarLivrosList(){
        for (indice in 1..10){
            livrosList.add(
                Livro(
                    "Titulo ${indice}",
                    "Isbn ${indice}",
                    "Primeiro autor ${indice}",
                    "Editora ${indice}",
                    indice,
                    indice,
                )
            )
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.adicionarLivroMi -> {
            livroActivityResultLauncher.launch(Intent(this, LivroActivity::class.java))
            true
        }
        else -> {
            false
        }
    }*/

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position

        return when (item.itemId){
            R.id.editarLivroMi -> {
                //editar livro
                val livro = livrosList[posicao]
                val editarLivroIntent = Intent(this, LivroActivity::class.java)

                editarLivroIntent.putExtra(EXTRA_LIVRO, livro)
                editarLivroIntent.putExtra(EXTRA_POSICAO, posicao)
                editarLivroActivityResultLauncher.launch(editarLivroIntent)

                true
            }
            R.id.removerLivroMi -> {
                //remover livro
                livrosList.removeAt(posicao)
                livrosAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }
        }

        return super.onContextItemSelected(item)
    }

}

