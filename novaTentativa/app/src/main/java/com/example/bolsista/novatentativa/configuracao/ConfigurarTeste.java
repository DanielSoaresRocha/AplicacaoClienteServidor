package com.example.bolsista.novatentativa.configuracao;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ConfigurarTeste extends AppCompatActivity {
    EditText qtdQuestao, intervaloQuestoes, intervalo2,nomeConfigEdit,detalhesConfigEdit;
    ImageView info1, info2,info3;
    TextView info1TextView, info2TextView,info3TextView;
    Button somErro1, somErro2, somErro3, somAcerto1, somAcerto2, somAcerto3;
    CheckBox formas, preenchimento, tamanho;
    Button irPara, cadastrarTestBtn;

    private MediaPlayer mp;

    private int erroEscolhido;
    private int acertoEscolhido;

    public static Configuracao configuracao;

    //FireBase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //Fire Base Auth
    FirebaseAuth usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_teste);

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());

        inicializar();
        listener();

    }

    private void listener() {
        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info1TextView.getVisibility() == View.GONE){
                    info1TextView.setVisibility(View.VISIBLE);
                }else{
                    info1TextView.setVisibility(View.GONE);
                }
            }
        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info2TextView.getVisibility() == View.GONE){
                    info2TextView.setVisibility(View.VISIBLE);
                }else{
                    info2TextView.setVisibility(View.GONE);
                }
            }
        });

        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info3TextView.getVisibility() == View.GONE){
                    info3TextView.setVisibility(View.VISIBLE);
                }else{
                    info3TextView.setVisibility(View.GONE);
                }
            }
        });

        //Sons

        somErro1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.error);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                erroEscolhido = R.raw.error;
            }
        });

        somErro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.error2);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                erroEscolhido = R.raw.error2;
            }
        });

        somErro3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.error3);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                erroEscolhido = R.raw.error3;
            }
        });

        somAcerto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.sucess);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                acertoEscolhido = R.raw.sucess;
            }
        });

        somAcerto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.sucess2);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                acertoEscolhido = R.raw.sucess2;
            }
        });

        somAcerto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.sucess3);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                acertoEscolhido = R.raw.sucess3;
            }
        });

        irPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
                    fazerConfiguracao();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","OK");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();

                }catch(java.lang.NumberFormatException e){
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }*/
                finish();
            }
        });

        cadastrarTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerConfiguracao();
                addTestToFireBase();
                Toast.makeText(getApplicationContext(), "Teste Adicionado",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addTestToFireBase(){
        db.collection("configuracoes")
                .add(configuracao)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("DataBase-FireStore-add", "DocumentSnapshot added with ID: " + documentReference.getId()
                                + "path = "+ documentReference.getPath());
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        configuracao.setId(documentReference.getId());
                        ListarViewModel.addConfiguracao(configuracao);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore-add", "Error adding document", e);
                    }
                });

        finish();
    }

    private void fazerConfiguracao(){

        configuracao = new Configuracao();
        configuracao.setNome(nomeConfigEdit.getText().toString());
        configuracao.setDetalhes(detalhesConfigEdit.getText().toString());
        configuracao.setQtdQuestoes(Integer.parseInt(qtdQuestao.getText().toString()));
        configuracao.setIntervalo1(Integer.parseInt(intervaloQuestoes.getText().toString()));
        configuracao.setIntervalo2(Integer.parseInt(intervalo2.getText().toString()));
        configuracao.setSomErro(erroEscolhido);
        configuracao.setSomAcerto(acertoEscolhido);
        configuracao.setImagens(getImagens());
        configuracao.setUsuario(usuarioRef);
    }

    private ArrayList<Integer> getImagens(){
        ArrayList<Integer> imagens = new ArrayList<>();
            if(formas.isChecked()){
                imagens.add(R.drawable.circulo);
                imagens.add(R.drawable.triangulo);
                imagens.add(R.drawable.coracao);
                imagens.add(R.drawable.estrela2);
                imagens.add(R.drawable.hexagono);
                imagens.add(R.drawable.retangulo);
            }
            if(preenchimento.isChecked()){
                imagens.add(R.drawable.b_circulo);
                imagens.add(R.drawable.b_triangle);
                imagens.add(R.drawable.b_coracao);
                imagens.add(R.drawable.b_estrela);
                imagens.add(R.drawable.b_hexagono);
                imagens.add(R.drawable.b_retangulo);
            }
            return imagens;
    }

    private void inicializar() {
        qtdQuestao = findViewById(R.id.qtdQuestao);
        intervaloQuestoes = findViewById(R.id.intervaloQuestoes);
        intervalo2 = findViewById(R.id.intervalo2);

        info1 = findViewById(R.id.info1);
        info2 = findViewById(R.id.info2);
        info3 = findViewById(R.id.info3);

        info1TextView = findViewById(R.id.info1TextView);
        info2TextView = findViewById(R.id.info2TextView);
        info3TextView = findViewById(R.id.info3TextView);

        somErro1 = findViewById(R.id.somErro1);
        somErro2 = findViewById(R.id.somErro2);
        somErro3 = findViewById(R.id.somErro3);
        somAcerto1 = findViewById(R.id.somAcerto1);
        somAcerto2 = findViewById(R.id.somAcerto2);
        somAcerto3 = findViewById(R.id.somAcerto3);

        formas = findViewById(R.id.formasBtn);
        preenchimento = findViewById(R.id.preenchimentoBtn);
        tamanho = findViewById(R.id.tamanhoBtn);

        irPara = findViewById(R.id.irPara);
        cadastrarTestBtn = findViewById(R.id.cadastrarTestBtn);
        nomeConfigEdit = findViewById(R.id.nomeConfigEdit);
        detalhesConfigEdit = findViewById(R.id.detalhesConfigEdit);
    }
}
