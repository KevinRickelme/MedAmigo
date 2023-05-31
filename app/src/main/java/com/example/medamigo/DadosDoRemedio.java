package com.example.medamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import DAO.RemedioDAO;
import DAO.UsuarioDAO;
import Models.Remedio;
import Models.Usuario;

public class DadosDoRemedio extends AppCompatActivity {

    private TextView txtNome;
    private Intent it;
    private Usuario usuario;
    private EditText edtNomeRemedio, edtIntervalo;
    private RemedioDAO remedioDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_do_remedio);

        remedioDAO = new RemedioDAO(this);
        usuarioDAO = new UsuarioDAO(this);

        txtNome = findViewById(R.id.txtNomeConfig);
        it = getIntent();
        usuario = (Usuario)it.getSerializableExtra("Usuario");
        if(usuario == null)
            usuario = usuarioDAO.getUsuario();
        txtNome.setText("Olá, " + usuario.Nome + "!");

    }

    public void btnCadastrar(View view) {

        if(edtNomeRemedio.getText().toString().isEmpty() && edtIntervalo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!!", Toast.LENGTH_SHORT).show();
            return;
        }


        Remedio remedio = new Remedio();
        remedio.Nome = edtNomeRemedio.getText().toString();
        remedio.Intervalo = Integer.parseInt(edtIntervalo.getText().toString());


        long resultado;
        if (remedioDAO.hasData())
            resultado= remedioDAO.update(remedio);
        else
            resultado = remedioDAO.insert(remedio);


        if(resultado != -1){
            it = new Intent(this, ApresentacaoRemedios.class);
            startActivity(it);
            finishAfterTransition();
        }
    }

}