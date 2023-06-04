package com.example.medamigo;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
    private ActivityResultLauncher<Intent> notificationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIniciar = findViewById(R.id.btnIniciar);

        createNotificationChannel();
        usuarioDAO = new UsuarioDAO(this);

        btnIniciar.setText(getString(R.string.btnVerMeusRemedios));

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

        requestNotificationPermission();

    }

    private static final int PERMISSION_REQUEST_CODE = 123;

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Verifica se a permissão já foi concedida
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, 1);;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Verifica se a permissão foi concedida
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, getString(R.string.AvisoPermissao), Toast.LENGTH_SHORT).show();
        }
    }

    //Método que é executado sempre que o usuário volta para a tela inicial
    @Override
    public void onResume(){
        super.onResume();
        if (!usuarioDAO.hasData()) {
            btnIniciar.setText(getString(R.string.btnCadastrarUsuario));
        } else {
            edtNome.setVisibility(View.GONE);
            btnIniciar.setText(getString(R.string.btnVerMeusRemedios));
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
            btnIniciar.setText(getString(R.string.btnCadastrarUsuario));
    }

    private Intent verificaRemedioCadastrado(){
        Intent it;
        RemedioDAO remedioDAO = new RemedioDAO(this);
        if(remedioDAO.getRemedio().Id == 0)
            it = new Intent(this, DadosDoRemedio.class);
        else
            it = new Intent(this, ApresentacaoRemedios.class);

        return it;
    }

    private void cadastrarUsuario(){
        if(edtNome.getText().toString().isEmpty())
            Toast.makeText(this, getString(R.string.ErroNomeInvalido), Toast.LENGTH_SHORT).show();
        else {
            Usuario user = new Usuario();
            user.Nome = edtNome.getText().toString();
            usuarioDAO.insert(user);
        }
    }

    public void createNotificationChannel(){
            CharSequence name = getString(R.string.notificationTitle);
            String description = "Canal para notificar quando tomar o remédio";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MedAmigo", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}