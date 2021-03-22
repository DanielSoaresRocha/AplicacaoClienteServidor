package com.example.bolsista.novatentativa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bolsista.novatentativa.arquitetura.Definir;
import com.example.bolsista.novatentativa.cadastros.CadastrarEquino;
import com.example.bolsista.novatentativa.cadastros.Gerenciar;
import com.example.bolsista.novatentativa.modelo.Usuario;
import com.example.bolsista.novatentativa.othersActivities.ExperimentosAndamento;
import com.example.bolsista.novatentativa.othersActivities.ExperimentosFinalizados;
import com.example.bolsista.novatentativa.viewsModels.ExperimentoViewModel;
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

public class Inicio extends AppCompatActivity {

    private int CODIGO_LOGAR = 234;

    //banco FireBase
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //autenticação FireBase
    FirebaseAuth usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        inicializar();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.gerenciar:
                Intent telaGerenciar = new Intent(Inicio.this, Gerenciar.class);
                startActivity(telaGerenciar);
                return true;
            case R.id.logoff:
                AuthUI.getInstance().signOut(this);
                telaLogin();
                return true;
            case R.id.cadastrarCavalo:
                Intent telaCadastro = new Intent(Inicio.this, CadastrarEquino.class);
                startActivity(telaCadastro);
                return true;
            case R.id.novoExperimento:
                Intent iniciarConfiguracao = new Intent(Inicio.this, NovoExperimento.class);
                startActivity(iniciarConfiguracao);
                return true;
            case R.id.experimentosAndamento:
                Intent experimentosAndamento = new Intent(Inicio.this, ExperimentosAndamento.class);
                startActivity(experimentosAndamento);
                return true;
            case R.id.experimentosFinalizados:
                Intent experimentosFinalizados = new Intent(Inicio.this, ExperimentosFinalizados.class);
                startActivity(experimentosFinalizados);
                return true;
            case R.id.conectar:
                Intent telaDefinir = new Intent(Inicio.this, Definir.class);
                startActivity(telaDefinir);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void inicializar() {
        ListarViewModel.carregarListas();
        ExperimentoViewModel.carregarLista();
    }
}
