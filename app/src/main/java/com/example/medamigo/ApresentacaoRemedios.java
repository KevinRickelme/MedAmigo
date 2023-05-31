package com.example.medamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.MessageFormat;

import DAO.RemedioDAO;
import Models.Remedio;

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

        txtRemedios.setText(MessageFormat.format("Nome: {0}\nIntervalo: {1}", remedioDAO.getRemedio().Nome, remedioDAO.getRemedio().Intervalo));
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