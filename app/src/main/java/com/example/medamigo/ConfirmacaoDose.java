package com.example.medamigo;

import static com.example.medamigo.Notificacao.getEndTime;
import static com.example.medamigo.Notificacao.isTimerRunning;
import static com.example.medamigo.Notificacao.setEndTime;
import static com.example.medamigo.Notificacao.setStartTimeInMillis;
import static com.example.medamigo.Notificacao.setTimerRunning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import DAO.HistoricoDAO;
import DAO.RemedioDAO;
import DAO.UsuarioDAO;
import Models.Historico;
import Models.Remedio;
import Models.Usuario;

public class ConfirmacaoDose extends AppCompatActivity {

    private Button btnAlarmeConfirmacao, btnConfirma, btnAdiar;
    private Intent it;
    private Usuario usuario;
    //private Remedio remedio;
    private UsuarioDAO usuarioDAO;
    private RemedioDAO remedioDAO;
    private HistoricoDAO historicoDAO;
    private TextView txtNomeDoRemedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_dose);

        btnAlarmeConfirmacao = findViewById(R.id.btnAlarmeConfirmacao);
        btnConfirma = findViewById(R.id.btnConfirma);
        btnAdiar = findViewById(R.id.btnAdiar);
        txtNomeDoRemedio = findViewById(R.id.txtNomeDoRemedio);
        remedioDAO = new RemedioDAO(this);
        usuarioDAO = new UsuarioDAO(this);
        historicoDAO = new HistoricoDAO(this);
        usuario = usuarioDAO.getUsuario();

        txtNomeDoRemedio.setText(String.format(getString(R.string.Remedio)+" %s", remedioDAO.getRemedio().Nome));
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
                return true;

            case R.id.itemSair:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirLista(View view){
        Intent it = new Intent(this, ApresentacaoRemedios.class);
        startActivity(it);
        finishAfterTransition();
    }

    public void btnConfirma(View view) {

        AlertDialog.Builder confirmaAcao = new AlertDialog.Builder(this);
        confirmaAcao.setTitle(getString(R.string.Atencao))
                .setMessage(getString(R.string.ConfirmarDose))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmar();
                    }
                });
        confirmaAcao.setNegativeButton(getString(R.string.Nao), null);
        confirmaAcao.create().show();
    }

    @SuppressLint("SimpleDateFormat")
    private void confirmar(){
        historicoDAO = new HistoricoDAO(this);
        Historico historico = new Historico();
        Remedio remedio = remedioDAO.getRemedio();
        historico.NomeDoRemedio = remedio.Nome;
        historico.DataDaDose = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR")).format(new Date());
        historico.Atrasou = getString(R.string.Nao);
        historicoDAO.insert(historico);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Notificacao notificacao = new Notificacao(this, alarmManager);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        if (System.currentTimeMillis() > getEndTime())
            setTimerRunning(false);
        boolean timerRunning = isTimerRunning();
        if(timerRunning){
            notificacao.cancelNotificationAlarm();
        }

        setStartTimeInMillis(10000);
        setEndTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificacao.setNotificationAlarm();
        }

        it = new Intent(this, ApresentacaoRemedios.class);
        startActivity(it);
        finishAfterTransition();
    }

    public void btnAdiar(View view){
        AlertDialog.Builder confirmaAdiar = new AlertDialog.Builder(this);
        confirmaAdiar.setTitle(getString(R.string.Atencao))
                .setMessage(getString(R.string.ConfirmarAdiar))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Sim), (dialog, which) -> adiar());
        confirmaAdiar.setNegativeButton(getString(R.string.Nao), null);
        confirmaAdiar.create().show();
    }

    @SuppressLint("SimpleDateFormat")
    private void adiar(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Notificacao notificacao = new Notificacao(this, alarmManager);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (System.currentTimeMillis() > getEndTime())
            setTimerRunning(false);
        boolean timerRunning = isTimerRunning();
        if(timerRunning){
            notificacao.cancelNotificationAlarm();
        }

        Historico historico = new Historico();
        Remedio remedio = remedioDAO.getRemedio();
        historico.NomeDoRemedio = remedio.Nome;
        historico.DataDaDose = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR")).format(new Date());
        historico.Atrasou = "Sim";
        historicoDAO.insert(historico);

        //setStartTimeInMillis(30 * 1000 * 60); // -> 30 minutos
        //1 min - 60 seg
        //1 seg - 1000 milis
        setStartTimeInMillis(6000); //utilização somente para apresentação.
        setEndTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificacao.setNotificationAlarm();
        }

        it = new Intent(this, ApresentacaoRemedios.class);
        startActivity(it);
        finishAfterTransition();
    }


    public void btnAlarmeConfirmacao(View view) {
        Intent it;
        it = new Intent(this, Alarmes.class);
        startActivity(it);
        this.finish();
    }
}