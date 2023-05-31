package com.example.medamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DAO.UsuarioDAO;
import Models.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText edtNome;
    private Button btnIniciar;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        usuarioDAO = new UsuarioDAO(this);

        btnIniciar.setText("Ver meus remédios");

        verificaSeTemCadastro();

        usuario = new Usuario();
        edtNome = findViewById(R.id.edtNome);
        btnIniciar = findViewById(R.id.btnIniciar);

        edtNome.setOnKeyListener((view, i, keyEvent) -> {
            btnIniciar.setEnabled(edtNome.getText().length() > 0);
            usuario.Nome = String.valueOf(edtNome.getText());
            return false;
        });
    }

    //Método que é executado sempre que o usuário volta para a tela inicial
    @Override
    public void onResume(){
        super.onResume();
        if (!usuarioDAO.hasData()) {
            btnIniciar.setText("Cadastrar usuário");
        } else {
            edtNome.setVisibility(View.GONE);
            btnIniciar.setText("Ver meus remédios");
        }
        btnIniciar.setEnabled(true);
    }

    public void btnIniciar(View view) {
        if (usuarioDAO.hasData()) {
            Intent it;
            UsuarioDAO usuarioDAO = new UsuarioDAO(this);
            it = new Intent(this, ApresentacaoRemedios.class);
            it.putExtra("Usuarios", usuarioDAO.getUsuario());
            startActivity(it);
        } else {
            if(edtNome.getText().toString().isEmpty())
                Toast.makeText(this, "Insira um nome para cadastrar!", Toast.LENGTH_SHORT).show();
            else {
                Usuario user = new Usuario();
                user.Nome = edtNome.getText().toString();
                usuarioDAO.insert(user);
            }
        }
    }

    public void verificaSeTemCadastro() {
        if (usuarioDAO.hasData()) {
            Intent it = new Intent(this, ApresentacaoRemedios.class);
            it.putExtra("Usuario", usuarioDAO.getUsuario());
            startActivity(it);
        }
        else
            btnIniciar.setText("Cadastrar usuário");
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Lembrete para tomar o remédio";
            String description = "Canal para notificar quando tomar o remédio";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("MedAmigo", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}