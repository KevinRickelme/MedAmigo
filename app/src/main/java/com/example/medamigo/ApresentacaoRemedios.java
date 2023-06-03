package com.example.medamigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.MessageFormat;

import DAO.RemedioDAO;

public class ApresentacaoRemedios extends AppCompatActivity {

    private Button btnAlarme, btnAlterar;
    private TextView txtRemedios;

    private Intent it;
    private RemedioDAO remedioDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao_remedios);

        txtRemedios = findViewById(R.id.txtRemedios);
        btnAlarme = findViewById(R.id.btnAlarme);
        btnAlterar = findViewById(R.id.btnAlterar);

        remedioDAO = new RemedioDAO(this);

        txtRemedios.setText(MessageFormat.format(getString(R.string.NomeUsuario)+" {0}\n"+getString(R.string.Intervalo)+ " {1}h", remedioDAO.getRemedio().Nome, remedioDAO.getRemedio().Intervalo));
    }


    //Método para apresentação
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Método com as ações dos botões localizados no canto superior direito da tela do app
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent it;
        switch (item.getItemId()) {
            case R.id.itemHistorico:
                it = new Intent(this, HistoricoDoses.class);
                startActivity(it);
                return true;

            case R.id.itemConfiguracoes:
                 it = new Intent(this, DadosDoRemedio.class);
                startActivity(it);
                return true;

            case R.id.itemSair:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnConfirmarDose(View view){
        Intent it;
        it = new Intent(this, ConfirmacaoDose.class);
        startActivity(it);
        this.finish();
    }

    public void btnAlterar(View view) {
        Intent it;
        it = new Intent(this, DadosDoRemedio.class);
        it.putExtra("Remedio", remedioDAO.getRemedio());
        startActivity(it);
        this.finish();
    }

    public void btnAlarme(View view) {
        Intent it;
        it = new Intent(this, Alarmes.class);
        startActivity(it);
        this.finish();
    }
}