package com.example.medamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import DAO.HistoricoDAO;
import Models.Historico;

public class HistoricoDoses extends AppCompatActivity {

    private HistoricoDAO historicoDAO;
    private TextView txtHistorico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_doses);

        txtHistorico = findViewById(R.id.txtHistorico);
        historicoDAO = new HistoricoDAO(this);

        apresentarHistorico();
    }

    private void apresentarHistorico(){
        List<Historico> registros = historicoDAO.getHistorico();

        for(Historico historico:registros){
            txtHistorico.setText(String.format("%s\nRemédio: %s\nData: %s\nAtrasou: %s", txtHistorico.getText().toString(), historico.NomeDoRemedio, historico.DataDaDose, historico.Atrasou));
            txtHistorico.setText(String.format("%s\n-----------",txtHistorico.getText().toString()));
        }

    }
}