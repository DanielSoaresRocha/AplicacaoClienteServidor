package com.example.bolsista.novatentativa.arquitetura;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.IniciarConfiguracao;
import com.example.bolsista.novatentativa.banco.CadastrarCavalo;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.configuracao.ConfigurarTeste;
import com.example.bolsista.novatentativa.modelo.Usuario;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;


public class Definir extends AppCompatActivity {
    ImageView controle,mestre,escravo;

    private int CODIGO_LOGAR = 234;

    //banco FireBase
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //autenticação FireBase
    FirebaseAuth usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir);

        inicializar();
        listener();

        usuario = FirebaseAuth.getInstance();

        if (usuario.getCurrentUser() != null) {
            Toast.makeText(this, "Olá " + usuario.getCurrentUser().getDisplayName(),
                    Toast.LENGTH_SHORT).show();
        }else{
            telaLogin();
        }
    }

    private void telaLogin(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build()
                                )
                        )
                        .build(),
                CODIGO_LOGAR
        );
    }

    //checar se este usuário existe no banco de dados
    private void CheckUsuario(){
        String idUsuario = usuario.getUid();

        DocumentReference docRef = db.collection("users").document(idUsuario);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("CheckUserToFareBase", "Usuario encontrado: " + document.getData());
                    } else {
                        Log.d("CheckUserToFareBase", "Usuario não existe");
                        adicionarUsuario();
                    }
                } else {
                    Log.d("CheckUserToFareBase", "get failed with ", task.getException());
                }
            }
        });
    }

    //adicionar o usuario atual ao banco de dados
    private void adicionarUsuario(){
        String nomeUsuarioAtual = this.usuario.getCurrentUser().getDisplayName();
        String uidUsuarioAtual = this.usuario.getUid();

        Usuario usuario = new Usuario(uidUsuarioAtual,nomeUsuarioAtual);

        db.collection("users").document(uidUsuarioAtual)
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addUserFireBase", "Usuario adicionado com sucesso!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addUserFireBase", "Error ao adicionar usuario", e);
                    }
                });
    }


    private void listener() {

        controle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaControle = new Intent(Definir.this, Remoto.class);
                startActivity(telaControle);
            }
        });

        mestre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent telaMestre = new Intent(Definir.this, Servidor.class);
                startActivity(telaMestre);*/
                Intent iniciarConfiguracao = new Intent(Definir.this, IniciarConfiguracao.class);
                startActivity(iniciarConfiguracao);
            }
        });

        escravo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaEscravo = new Intent(Definir.this, ClienteActivity.class);
                startActivity(telaEscravo);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoff:
                AuthUI.getInstance().signOut(this);
                telaLogin();
                return true;
            case R.id.cadastrarCavalo:
                Intent telaCadastro = new Intent(this, CadastrarCavalo.class);
                startActivity(telaCadastro);
                return true;
            case R.id.novoExperimento:
                Intent iniciarConfiguracao = new Intent(Definir.this, IniciarConfiguracao.class);
                startActivity(iniciarConfiguracao);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_LOGAR) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bem-vindo "+
                        usuario.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                CheckUsuario();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Você está anônimo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void inicializar() {
        controle = findViewById(R.id.controleImgView);
        mestre = findViewById(R.id.mestreImgView);
        escravo = findViewById(R.id.escravoImgView);

        ListarViewModel.carregarListas();
    }
}
