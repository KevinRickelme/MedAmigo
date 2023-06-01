package com.example.medamigo;

import static com.example.medamigo.Notificacao.getEndTime;
import static com.example.medamigo.Notificacao.isTimerRunning;
import static com.example.medamigo.Notificacao.setEndTime;
import static com.example.medamigo.Notificacao.setStartTimeInMillis;
import static com.example.medamigo.Notificacao.setTimerRunning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private Button btnAlarme, btnConfirma, btnAdiar;
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

        btnAlarme = findViewById(R.id.btnAlarme);
        btnConfirma = findViewById(R.id.btnConfirma);
        btnAdiar = findViewById(R.id.btnAdiar);
        txtNomeDoRemedio = findViewById(R.id.txtNomeDoRemedio);
        remedioDAO = new RemedioDAO(this);
        usuarioDAO = new UsuarioDAO(this);
        historicoDAO = new HistoricoDAO(this);
        usuario = usuarioDAO.getUsuario();

        txtNomeDoRemedio.setText(String.format("Remédio: %s", remedioDAO.getRemedio().Nome));

    }

    public void abrirLista(View view){
        Intent it = new Intent(this, ApresentacaoRemedios.class);
        startActivity(it);
        finishAfterTransition();
    }

    public void btnConfirma(View view) {

        AlertDialog.Builder confirmaAcao = new AlertDialog.Builder(this);
        confirmaAcao.setTitle("Atenção !!")
                .setMessage("Você deseja confirmar que tomou sua dose?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmar();
                    }
                });
        confirmaAcao.setNegativeButton("Não", null);
        confirmaAcao.create().show();
    }

    @SuppressLint("SimpleDateFormat")
    private void confirmar(){
        historicoDAO = new HistoricoDAO(this);
        Historico historico = new Historico();
        Remedio remedio = remedioDAO.getRemedio();
        historico.NomeDoRemedio = remedio.Nome;
        historico.DataDaDose = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR")).format(new Date());
        historico.Atrasou = "Não";
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
        notificacao.setNotificationAlarm();

        it = new Intent(this, ApresentacaoRemedios.class);
        startActivity(it);
        finishAfterTransition();
    }

    public void btnAdiar(View view){
        AlertDialog.Builder confirmaAdiar = new AlertDialog.Builder(this);
        confirmaAdiar.setTitle("Atenção !!")
                .setMessage("Tem certeza que deseja adiar a notificação em 30 minutos?")
                .setCancelable(false)
                .setPositiveButton("Sim", (dialog, which) -> adiar());
        confirmaAdiar.setNegativeButton("Não", null);
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
        notificacao.setNotificationAlarm();

        it = new Intent(this, ApresentacaoRemedios.class);
        startActivity(it);
        finishAfterTransition();
    }
}