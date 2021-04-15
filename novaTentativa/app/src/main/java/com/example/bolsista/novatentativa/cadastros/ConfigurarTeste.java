package com.example.bolsista.novatentativa.cadastros;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Desafio;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ConfigurarTeste extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText qtdQuestao, intervaloQuestoes, intervalo2,nomeConfigEdit,detalhesConfigEdit, criterioAprendEdit;
    ImageView info1, info2,info3;
    TextView info1TextView, info2TextView,info3TextView;
    Button salvarTestBtn;
    RadioButton somErro1, somErro2, somErro3, somAcerto1, somAcerto2, somAcerto3;
    CheckBox formas, preenchimento, tamanho;
    Button irPara, cadastrarTestBtn;
    Spinner qtdDesafiosSessao, qtdEnsaiosSessao, qtdRepDesafiosSessao;

    private MediaPlayer mp;
    private Context contextActivity;

    private int erroEscolhido;
    private int acertoEscolhido;
    ArrayAdapter<CharSequence> qtdDesafiosAdapter, qtdEnsaiosAdapter, qtdRepDesafiosAdapter;
    private String qtdDesText, qtdEnsTex, qtdRepDesText;

    public static Teste teste;

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
        spinners();
        listener();
        receberTeste();
    }

    @SuppressLint("SetTextI18n")
    private void receberTeste() {
        teste = (Teste) getIntent().getSerializableExtra("teste");

        if(teste != null){
            salvarTestBtn.setVisibility(View.VISIBLE);
            cadastrarTestBtn.setVisibility(View.GONE);

            nomeConfigEdit.setText(teste.getNome());
            intervaloQuestoes.setText(Integer.toString(teste.getIntervalo1()));
            intervalo2.setText(Integer.toString(teste.getIntervalo2()));

            acertoEscolhido = teste.getSomAcerto();
            erroEscolhido = teste.getSomErro();
            sonsButtons(acertoEscolhido, erroEscolhido);

            for(Desafio desafio : teste.getDesafios()){
                if(desafio.getId().equals("0"))
                    formas.setChecked(true);
                if(desafio.getId().equals("6"))
                    preenchimento.setChecked(true);
            }

            detalhesConfigEdit.setText(teste.getObservacoes());
            qtdQuestao.setText(Integer.toString(teste.getQtdQuestoesPorSessao()));
            criterioAprendEdit.setText(Integer.toString(teste.getCriterioAprendizagem()));

        }else {
            teste = new Teste();
            teste.setDesafios(new ArrayList<>());
            teste.setSessoes(new ArrayList<>());
        }
    }

    private void sonsButtons(int acertoEscolhido, int erroEscolhido){
        switch (acertoEscolhido){
            case R.raw.sucess:
                somAcerto1.setChecked(true);
                break;
            case R.raw.sucess2:
                somAcerto2.setChecked(true);
                break;
            case R.raw.sucess3:
                somAcerto2.setChecked(true);
                break;
        }

        switch (erroEscolhido){
            case R.raw.error:
                somErro1.setChecked(true);
                break;
            case R.raw.error2:
                somErro2.setChecked(true);
                break;
            case R.raw.error3:
                somErro3.setChecked(true);
                break;
        }

    }

    private void spinners() {
        // Quantidade de desafios
        qtdDesafiosAdapter = ArrayAdapter.createFromResource(contextActivity,
                R.array.qtdDesafios_array, android.R.layout.simple_spinner_item);
        qtdDesafiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtdDesafiosSessao.setAdapter(qtdDesafiosAdapter);

        // Quantidade de ensaios
        qtdEnsaiosAdapter = ArrayAdapter.createFromResource(contextActivity,
                R.array.qtdEnsaios_array, android.R.layout.simple_spinner_item);
        qtdEnsaiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtdEnsaiosSessao.setAdapter(qtdEnsaiosAdapter);

        //Quantidade máxima de repetição de cada desafio
        qtdRepDesafiosAdapter = ArrayAdapter.createFromResource(contextActivity,
                R.array.qtdDesafios_array, android.R.layout.simple_spinner_item);
        qtdRepDesafiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtdRepDesafiosSessao.setAdapter(qtdRepDesafiosAdapter);
    }

    private void listener() {
        qtdDesafiosSessao.setOnItemSelectedListener(this);
        qtdEnsaiosSessao.setOnItemSelectedListener(this);
        qtdRepDesafiosSessao.setOnItemSelectedListener(this);

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

        salvarTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerConfiguracao();
                updateTesteFireStore(teste);
            }
        });

    }

    private void updateTesteFireStore(Teste teste){
        db.collection("configuracoes")
                .document(teste.getId())
                .set(teste)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FireStore", "DocumentSnapshot successfully updated!");
                        Toast.makeText(contextActivity, "Teste Atualizado", Toast.LENGTH_SHORT).show();
                        updateExperimentosWithTeste(teste);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FireStore", "Erro ao atualizar", e);
                    }
                });
    }

    private void updateExperimentosWithTeste(Teste teste){
        db.collection("experimentos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Experimento experimento = document.toObject(Experimento.class);

                                for (int i = 0; i < experimento.getTestes().size(); i++){
                                    if(experimento.getTestes().get(i).getId().equals(teste.getId())){
                                        experimento.getTestes().set(i, teste);
                                        updateExperimento(experimento);
                                        break;
                                    }
                                }
                            }
                            finish();
                        }
                    }
                });
    }

    private void updateExperimento(Experimento experimento){
        db.collection("experimentos")
                .document(experimento.getId())
                .set(experimento)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(contextActivity, "Ocorreu algum erro ao tentar atualizar experimento",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addTestToFireBase(){
        db.collection("configuracoes")
                .add(teste)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("DataBase-FireStore-add", "DocumentSnapshot added with ID: " + documentReference.getId()
                                + "path = "+ documentReference.getPath());
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        teste.setId(documentReference.getId());
                        ListarViewModel.addConfiguracao(teste);

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
        teste.setNome(nomeConfigEdit.getText().toString());
        teste.setIntervalo1(Integer.parseInt(intervaloQuestoes.getText().toString()));
        teste.setIntervalo2(Integer.parseInt(intervalo2.getText().toString()));
        teste.setQtdDesafios(0);
        teste.setSomAcerto(acertoEscolhido);
        teste.setSomErro(erroEscolhido);
        teste.setDesafios(getDesafios());
        teste.setObservacoes(detalhesConfigEdit.getText().toString());
        teste.setTipo(2);
        teste.setAleatoriedade(3);
        teste.setPreTeste(false);
        teste.setQtdQuestoesPorSessao(Integer.parseInt(qtdQuestao.getText().toString()));
        teste.setMaxVezesConsecutivas(3);
        teste.setCriterioAprendizagem(Integer.parseInt(criterioAprendEdit.getText().toString()));
        teste.setCompleto(false);


    }

    private ArrayList<Desafio> getDesafios(){
        ArrayList<Desafio> desafios = new ArrayList<>();
            if(formas.isChecked()){
                desafios.add(new Desafio("0", R.drawable.circuloo));
                desafios.add(new Desafio("1", R.drawable.trianguloo));
                desafios.add(new Desafio("2", R.drawable.coracaoo));
                desafios.add(new Desafio("3", R.drawable.estrela22));
                desafios.add(new Desafio("4", R.drawable.hexagonoo));
                desafios.add(new Desafio("5", R.drawable.retanguloo));
            }
            if(preenchimento.isChecked()){
                desafios.add(new Desafio("6", R.drawable.b_circuloo));
                desafios.add(new Desafio("7", R.drawable.trianguloo));
                desafios.add(new Desafio("8", R.drawable.b_coracaoo));
                desafios.add(new Desafio("9", R.drawable.b_estrela22));
                desafios.add(new Desafio("10", R.drawable.b_hexagonoo));
                desafios.add(new Desafio("11", R.drawable.b_retanguloo));
            }
            return desafios;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();

        switch(parent.getId()){
            case R.id.qtdDesafiosSessao:
                qtdDesText = text;
                break;
            case  R.id.qtdEnsaiosSessao:
                qtdEnsTex = text;
                break;
            case  R.id.qtdRepDesafiosSessao:
                qtdRepDesText = text;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

        qtdDesafiosSessao = findViewById(R.id.qtdDesafiosSessao);
        qtdEnsaiosSessao = findViewById(R.id.qtdEnsaiosSessao);
        qtdRepDesafiosSessao = findViewById(R.id.qtdRepDesafiosSessao);


        contextActivity = this;
        irPara = findViewById(R.id.irPara);
        cadastrarTestBtn = findViewById(R.id.cadastrarTestBtn);
        salvarTestBtn = findViewById(R.id.salvarTestBtn);
        nomeConfigEdit = findViewById(R.id.nomeConfigEdit);
        detalhesConfigEdit = findViewById(R.id.detalhesConfigEdit);
        criterioAprendEdit = findViewById(R.id.criterioAprendEdit);
    }
}
