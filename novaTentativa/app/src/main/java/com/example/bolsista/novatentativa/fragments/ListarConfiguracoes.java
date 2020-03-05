package com.example.bolsista.novatentativa.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.drakeet.materialdialog.MaterialDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.IniciarConfiguracao;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.CavaloAdapter;
import com.example.bolsista.novatentativa.adapters.ConfiguracaoAdapter;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.ListarCavalosOnItemTouch;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ListarConfiguracoes extends Fragment {
    private ListarViewModel mViewModel;
    private View v;
    private Context contextoAtivity;

    private RecyclerView configuracaoRecycle;
    private ProgressBar progressBarConfig;

    @SuppressLint("StaticFieldLeak")
    public static ConfiguracaoAdapter adapter;

    //FireBase FireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_listar_configuracoes, container, false);
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ListarViewModel.class);
        // TODO: Use the ViewModel
        inicializar();

        if(mViewModel.cavalos.getValue().size() == 0){ // se a lista estiver vazia
            getConfiguracoesFireStore();// pegar do banco
        }else{// se não
            implementsRecycle();// apenas implemente o recycle
        }
    }

    private void getConfiguracoesFireStore() {
        db.collection("configuracoes")
                //.whereEqualTo("users", usuarioRef)//referencia do usuario que adicionou o cavalo
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Configuracao configuracao = document.toObject(Configuracao.class);
                                mViewModel.addConfiguracao(configuracao);
                                Log.i("DataBase-FireStore-get", "referencia de => ." +
                                        configuracao.getNome() + " = " +
                                        document.getDocumentReference("usuario").getId());
                            }
                            implementsRecycle();
                            observerList();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void implementsRecycle(){
        adapter = new ConfiguracaoAdapter(contextoAtivity,mViewModel.configuracoes.getValue());
        configuracaoRecycle.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextoAtivity,LinearLayoutManager.VERTICAL,false);
        configuracaoRecycle.setLayoutManager(layout);

        configuracaoRecycle.addOnItemTouchListener(
                new ListarCavalosOnItemTouch(
                        contextoAtivity,
                        configuracaoRecycle,
                        new ListarCavalosOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                IniciarConfiguracao.configuracaoSelecionada = mViewModel.configuracoes.getValue().get(position);
                                Toast.makeText(contextoAtivity,"Configuracao selecionada",
                                        Toast.LENGTH_SHORT).show();
                                Log.i("Teste", "onSingleTapUp2");
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {
                                View layout = LayoutInflater.from(contextoAtivity)
                                        .inflate(R.layout.configuracaoinformacao_inflater,null,false);

                                TextView detalhes = layout.findViewById(R.id.detalhesConfigInfo);
                                TextView intervalo1 = layout.findViewById(R.id.intervalo1ConfigInfo);
                                TextView intervalo2 = layout.findViewById(R.id.intervalo2ConfigInfo);
                                TextView qtdQuestoes = layout.findViewById(R.id.qtdQuestoesConfigInfo);

                                detalhes.setText(mViewModel.configuracoes.getValue().get(position)
                                        .getDetalhes());
                                intervalo1.setText(mViewModel.configuracoes.getValue().get(position)
                                        .getIntervalo1()+" segundos");
                                intervalo2.setText(mViewModel.configuracoes.getValue().get(position)
                                        .getIntervalo2()+" segundos");
                                qtdQuestoes.setText(mViewModel.configuracoes.getValue().get(position)
                                        .getQtdQuestoes()+" segundos");

                                MaterialDialog m = new MaterialDialog(contextoAtivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();
                            }
                        })
        );

        progressBarConfig.setVisibility(View.GONE);
    }

    private void observerList() {
        mViewModel.configuracoes.observe(this, configuracoes ->{
            // update UI
            adapter.notifyDataSetChanged();
        });
    }

    private void inicializar() {
        configuracaoRecycle = v.findViewById(R.id.configuracaoRecycle);
        progressBarConfig = v.findViewById(R.id.progressBarConfig);

        contextoAtivity = getActivity();

        usuario = FirebaseAuth.getInstance();
        try {
            usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
        }catch (java.lang.NullPointerException e){
            usuarioRef = db.collection("users").document("zl1hFltVOlJONAVUeIsY");
        }
    }
}
