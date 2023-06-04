package com.example.medamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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

        historicoDAO = new HistoricoDAO(this);

        apresentarHistorico();
    }

    private void apresentarHistorico(){
        List<Historico> registros = historicoDAO.getHistorico();

        ListView listView = findViewById(R.id.customListView);
        CustomAdapter customAdapter = new CustomAdapter(HistoricoDoses.this, registros);

        listView.setAdapter(customAdapter);


    }
}