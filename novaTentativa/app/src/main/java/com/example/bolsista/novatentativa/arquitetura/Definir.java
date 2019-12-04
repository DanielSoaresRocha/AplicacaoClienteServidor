package com.example.bolsista.novatentativa.arquitetura;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.bolsista.novatentativa.banco.CadastrarCavalo;
import com.example.bolsista.novatentativa.banco.ListarCavalos;
import com.example.bolsista.novatentativa.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Definir extends AppCompatActivity {
    ImageView controle,mestre,escravo;
    Button testeBtn, listarBtn;

    private int CODIGO_LOGAR = 234;

    //autenticação FireBase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir);

        inicializar();
        listener();

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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


        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    private void onSignInInitialize(String nome){
        //inicia a aplicação
    }

    private void onSignOutCleanUp(){
        //fecha a aplicação
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
                Intent telaMestre = new Intent(Definir.this, Servidor.class);
                startActivity(telaMestre);
            }
        });

        escravo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaEscravo = new Intent(Definir.this, ClienteActivity.class);
                startActivity(telaEscravo);
            }
        });

        testeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaCadastro = new Intent(Definir.this, CadastrarCavalo.class);
                startActivity(telaCadastro);
            }
        });

        listarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaListar = new Intent(Definir.this, ListarCavalos.class);
                startActivity(telaListar);
            }
        });

    }


    private void inicializar() {
        controle = findViewById(R.id.controleImgView);
        mestre = findViewById(R.id.mestreImgView);
        escravo = findViewById(R.id.escravoImgView);
        testeBtn = findViewById(R.id.testeBtn);
        listarBtn = findViewById(R.id.listarBtn);
    }
}
