package com.example.livros;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.livros.databinding.ActivityLivroBinding;
import com.example.livros.model.Livro;

public class LivroActivity extends AppCompatActivity {

    private ActivityLivroBinding activityLivroBinding;
    private int posicao = -1;
    private Livro livro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLivroBinding = ActivityLivroBinding.inflate(getLayoutInflater());
        setContentView(activityLivroBinding.getRoot());

        activityLivroBinding.salvarBt.setOnClickListener(
                (View view ) -> {
                     livro = new Livro(
                            activityLivroBinding.tituloEt.getText().toString(),
                            activityLivroBinding.isbnEt.getText().toString(),
                            activityLivroBinding.primeiroAutorEt.getText().toString(),
                            activityLivroBinding.editoraEt.getText().toString(),
                            Integer.parseInt(activityLivroBinding.edicaoEt.getText().toString()),
                            Integer.parseInt(activityLivroBinding.paginasEt.getText().toString())
                    );

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.EXTRA_LIVRO, livro);

                    //se foi uma edicao retornar a posicao
                    if (posicao != -1){
                        resultIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
        );

        //verificar se e uma edicao ou consulta e preencher os campos
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        livro = getIntent().getParcelableExtra(MainActivity.EXTRA_LIVRO);
        if (livro != null){
            activityLivroBinding.tituloEt.setEnabled(false) ;
            activityLivroBinding.tituloEt.setText(livro.getTitulo());
            activityLivroBinding.isbnEt.setText(livro.getIsbn());
            activityLivroBinding.primeiroAutorEt.setText(livro.getPrimeiroAutor());
            activityLivroBinding.editoraEt.setText(livro.getEditora());
            activityLivroBinding.edicaoEt.setText(String.valueOf(livro.getEdicao()));
            activityLivroBinding.paginasEt.setText(String.valueOf(livro.getPaginas()));

            if (posicao == -1){
                for (int i=0; i<activityLivroBinding.getRoot().getChildCount(); i++){
                    activityLivroBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityLivroBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}
