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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private Remedio remedio;
    private EditText edtNomeRemedio, edtIntervalo;
    private RemedioDAO remedioDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_do_remedio);

        remedioDAO = new RemedioDAO(this);
        usuarioDAO = new UsuarioDAO(this);

        edtNomeRemedio = findViewById(R.id.edtNomeRemedio);
        edtIntervalo = findViewById(R.id.edtIntervalo);

        txtNome = findViewById(R.id.txtNomeConfig);
        it = getIntent();
        usuario = (Usuario)it.getSerializableExtra("Usuario");
        remedio = (Remedio)it.getSerializableExtra("Remedio");
        if(usuario == null)
            usuario = usuarioDAO.getUsuario();

        if(remedio == null)
            remedio = remedioDAO.getRemedio();
        txtNome.setText(txtNome.getText() +" " + usuario.Nome + "!");

        if(remedio.Id != 0){
            edtNomeRemedio.setText(remedio.Nome);
            edtIntervalo.setText(String.valueOf(remedio.Intervalo));
        }

    }

    public void btnCadastrar(View view) {
        boolean a = edtIntervalo.getText().toString().isEmpty();
        String b = edtIntervalo.getText().toString();

        boolean c = edtNomeRemedio.getText().toString().isEmpty();
        String d = edtNomeRemedio.getText().toString();

        if(edtNomeRemedio.getText().toString().isEmpty() || edtIntervalo.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.AvisoCampos), Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.parseInt(edtIntervalo.getText().toString()) < 1){
            Toast.makeText(this, getString(R.string.AvisoIntervalo), Toast.LENGTH_SHORT).show();
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


        if(resultado != -1){
            it = new Intent(this, ApresentacaoRemedios.class);
            startActivity(it);
            finishAfterTransition();
        }
    }

}