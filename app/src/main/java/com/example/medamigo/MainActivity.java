package com.example.medamigo;

import androidx.annotation.RequiresApi;
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

import DAO.RemedioDAO;
import DAO.UsuarioDAO;
import Models.Remedio;
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

        btnIniciar = findViewById(R.id.btnIniciar);

        createNotificationChannel();
        usuarioDAO = new UsuarioDAO(this);

        btnIniciar.setText("Ver meus remédios");

        verificaSeTemCadastro();
        verificaRemedioCadastrado();
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
        Intent it;
        if (usuarioDAO.hasData()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(this);
            it = new Intent(this, ApresentacaoRemedios.class);
            it.putExtra("Usuarios", usuarioDAO.getUsuario());
        } else {
            cadastrarUsuario();
            it = verificaRemedioCadastrado();
        }
        startActivity(it);
    }

    private void verificaSeTemCadastro() {
        if (usuarioDAO.hasData()) {
            Intent it = verificaRemedioCadastrado();
            it.putExtra("Usuario", usuarioDAO.getUsuario());
            startActivity(it);
        }
        else
            btnIniciar.setText("Cadastrar usuário");
    }

    private Intent verificaRemedioCadastrado(){
        Intent it;
        RemedioDAO remedioDAO = new RemedioDAO(this);
        if(remedioDAO.getRemedio().Id == 0)
            it = new Intent(this, DadosDoRemedio.class);
        else
            it = new Intent(this, ConfirmacaoDose.class);

        return it;
    }

    private void cadastrarUsuario(){
        if(edtNome.getText().toString().isEmpty())
            Toast.makeText(this, "Insira um nome para cadastrar!", Toast.LENGTH_SHORT).show();
        else {
            Usuario user = new Usuario();
            user.Nome = edtNome.getText().toString();
            usuarioDAO.insert(user);
        }
    }

    public void createNotificationChannel(){
            CharSequence name = "Lembrete para tomar o remédio";
            String description = "Canal para notificar quando tomar o remédio";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MedAmigo", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}