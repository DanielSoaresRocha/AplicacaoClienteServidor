package com.example.bolsista.novatentativa.banco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.drakeet.materialdialog.MaterialDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.CavaloAdapter;
import com.example.bolsista.novatentativa.adapters.ConfiguracaoAdapter;
import com.example.bolsista.novatentativa.modelo.Cavalo;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.ListaConfigOnTouch;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.ListarCavalosOnItemTouch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class SelectConf_Cav extends AppCompatActivity {
    RecyclerView recyclerCavalos;
    RecyclerView selectConfigRecycle;

    Cavalo cavalo;
    public static Configuracao configuracao;

    //list cavalos
    List<Cavalo> cavalos = new ArrayList<Cavalo>();
    CavaloAdapter cavalosAdapter;

    //list configs
    List<Configuracao> configs = new ArrayList<Configuracao>();
    ConfiguracaoAdapter configuracaoAdapter;

    //FireBase FireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    Context contextoAtivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_conf__cav);

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());

        contextoAtivity = this;

        inicializar();
        getCavalosFireStore();
        getConfigsFireStore();
    }

    public void getConfigsFireStore(){
        Source source = Source.CACHE;
        Log.i("---------------------","vai pegar");

        db.collection("configuracoes")
                //.whereEqualTo("referencia", usuarioRef)//referencia do usuario que adicionou o cavalo
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("---------------------","pegou");
                                configuracao = document.toObject(Configuracao.class);
                                configs.add(configuracao);
                                Log.i("DataBase-FireStore-get", "referencia de => ." +
                                        configuracao.getImagens().get(0) + " = " +
                                        document.getDocumentReference("referencia").getId());
                            }
                            implementsRecycleConfig();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void implementsRecycleConfig(){
        configuracaoAdapter = new ConfiguracaoAdapter(getApplicationContext(),configs);
        selectConfigRecycle.setAdapter(configuracaoAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        selectConfigRecycle.setLayoutManager(layout);

        selectConfigRecycle.setItemAnimator(new DefaultItemAnimator());

        selectConfigRecycle.addOnItemTouchListener(new ListaConfigOnTouch(getApplicationContext(),
                recyclerCavalos,
                new ListaConfigOnTouch.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getApplicationContext(),"Clique simples",
                        //        Toast.LENGTH_SHORT).show();
                        configuracao = configs.get(position);
                        retonarActivity();//volta para activity anterior
                        Log.i("Teste", "onSingleTapUp2");
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        View layout = LayoutInflater.from(contextoAtivity)
                                .inflate(R.layout.configuracaoinformacao_inflater,null,false);

                        TextView detalhes =(TextView) layout.findViewById(R.id.detalhesConfigInfo);
                        TextView intervalo1 =(TextView) layout.findViewById(R.id.intervalo1ConfigInfo);
                        TextView intervalo2 =(TextView) layout.findViewById(R.id.intervalo2ConfigInfo);
                        TextView qtdQuestoes =(TextView) layout.findViewById(R.id.qtdQuestoesConfigInfo);

                        detalhes.setText(configs.get(position).getDetalhes());
                        intervalo1.setText(configs.get(position).getIntervalo1()+"");
                        intervalo2.setText(configs.get(position).getIntervalo2()+"");
                        qtdQuestoes.setText(configs.get(position).getQtdQuestoes()+"");

                        MaterialDialog m = new MaterialDialog(contextoAtivity)
                                .setContentView(layout)
                                .setCanceledOnTouchOutside(true);

                        m.show();
                    }
                })
        );
    }

    private void retonarActivity(){
        try {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","OK");
            setResult(Activity.RESULT_OK,returnIntent);
            finish();

        }catch(java.lang.NumberFormatException e){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    public void getCavalosFireStore(){
        Source source = Source.CACHE;

        db.collection("equinos")
                //.whereEqualTo("referencia", usuarioRef)//referencia do usuario que adicionou o cavalo
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cavalo = document.toObject(Cavalo.class);
                                cavalos.add(cavalo);
                                Log.i("DataBase-FireStore-get", "referencia de => ." +
                                        cavalo.getReferencia() + " = " +
                                        document.getDocumentReference("referencia").getId());
                            }
                            implementsRecycleCavalos();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void implementsRecycleCavalos(){
        cavalosAdapter = new CavaloAdapter(getApplicationContext(),cavalos);
        recyclerCavalos.setAdapter(cavalosAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerCavalos.setLayoutManager(layout);

        recyclerCavalos.addOnItemTouchListener(
                new ListarCavalosOnItemTouch(
                        getApplicationContext(),
                        recyclerCavalos,
                        new ListarCavalosOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                cavalo = cavalos.get(position);
                                Toast.makeText(getApplicationContext(),"Cavalo selecionado",
                                        Toast.LENGTH_SHORT).show();
                                Log.i("Teste", "onSingleTapUp2");
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                View layout = LayoutInflater.from(contextoAtivity)
                                        .inflate(R.layout.cavaloinformacao_inflater,null,false);

                                TextView nomeCavalo =(TextView) layout.findViewById(R.id.nomeEquinoInfo);
                                TextView detalhes =(TextView) layout.findViewById(R.id.detalhesEquinoInfo);
                                TextView idade =(TextView) layout.findViewById(R.id.idadeEquinoInfo);
                                TextView raca =(TextView) layout.findViewById(R.id.racaEquinoInfo);

                                nomeCavalo.setText(cavalos.get(position).getNome());
                                detalhes.setText(cavalos.get(position).getDetalhes());
                                idade.setText(cavalos.get(position).getIdade() + "");
                                raca.setText(cavalos.get(position).getRaca());

                                MaterialDialog m = new MaterialDialog(contextoAtivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();
                            }
                        })
        );

        recyclerCavalos.setItemAnimator(new DefaultItemAnimator());

    }

    public void inicializar(){
        recyclerCavalos = findViewById(R.id.selectCavalosRecycle);
        selectConfigRecycle = findViewById(R.id.selectConfigRecycle);
    }

}
