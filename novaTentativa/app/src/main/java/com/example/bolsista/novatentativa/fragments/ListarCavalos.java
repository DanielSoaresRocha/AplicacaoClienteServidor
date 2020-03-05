package com.example.bolsista.novatentativa.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.bolsista.novatentativa.modelo.Cavalo;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.ListarCavalosOnItemTouch;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class ListarCavalos extends Fragment {
    private ListarViewModel mViewModel;
    private View v;
    private Context contextoAtivity;

    private RecyclerView cavalosRecycle;
    private ProgressBar progressBarCavalos;

    @SuppressLint("StaticFieldLeak")
    public static CavaloAdapter adapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    //FireBase FireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_listar_cavalos, container, false);
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ListarViewModel.class);
        // TODO: Use the ViewModel
        inicializar();

        if(mViewModel.cavalos.getValue().size() == 0){ // se a lista estiver vazia
            getCavalosFireStore();// pegar do banco
        }else{// se não
            implementsRecycle();// apenas implemente o recycle
        }
    }

    private void getCavalosFireStore() {
        db.collection("equinos")
                //.whereEqualTo("users", usuarioRef)//referencia do usuario que adicionou o cavalo
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Cavalo cavalo = document.toObject(Cavalo.class);
                                mViewModel.addCavalo(cavalo);
                                Log.i("DataBase-FireStore-get", "referencia de => ." +
                                        cavalo.getNome() + " = " +
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
        adapter = new CavaloAdapter(contextoAtivity,mViewModel.cavalos.getValue());
        cavalosRecycle.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextoAtivity,LinearLayoutManager.VERTICAL,false);
        cavalosRecycle.setLayoutManager(layout);

        cavalosRecycle.addOnItemTouchListener(
                new ListarCavalosOnItemTouch(
                        contextoAtivity,
                        cavalosRecycle,
                        new ListarCavalosOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                IniciarConfiguracao.cavaloSelecionado = mViewModel.cavalos.getValue().get(position);
                                Toast.makeText(contextoAtivity,"Cavalo selecionado",
                                        Toast.LENGTH_SHORT).show();
                                Log.i("Teste", "onSingleTapUp2");
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {
                                View layout = LayoutInflater.from(contextoAtivity)
                                        .inflate(R.layout.cavaloinformacao_inflater,null,false);

                                TextView nomeCavalo = layout.findViewById(R.id.nomeEquinoInfo);
                                TextView detalhes = layout.findViewById(R.id.detalhesEquinoInfo);
                                TextView idade = layout.findViewById(R.id.idadeEquinoInfo);
                                TextView raca = layout.findViewById(R.id.racaEquinoInfo);

                                nomeCavalo.setText(mViewModel.cavalos.getValue().get(position).getNome());
                                detalhes.setText(mViewModel.cavalos.getValue().get(position).getDetalhes());
                                idade.setText(calculaIdade(mViewModel.cavalos.getValue()
                                        .get(position).getDataNascimento()) + " anos");
                                raca.setText(mViewModel.cavalos.getValue().get(position).getRaca());

                                MaterialDialog m = new MaterialDialog(contextoAtivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();
                            }
                        })
        );

        progressBarCavalos.setVisibility(View.GONE);
    }

    // Retorna o calculo da idade atual a partir de uma data
    // Código disponível em https://www.devmedia.com.br/calcule-a-idade-corretamente-em-java/4729
    public static int calculaIdade(java.util.Date dataNasc){
        Calendar dateOfBirth = new GregorianCalendar();
        dateOfBirth.setTime(dataNasc);
        // Cria um objeto calendar com a data atual
        Calendar today = Calendar.getInstance();
        // Obtém a idade baseado no ano
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        dateOfBirth.add(Calendar.YEAR, age);
        //se a data de hoje é antes da data de Nascimento, então diminui 1(um)
        if (today.before(dateOfBirth)) {
            age--;
        }
        return age;
    }

    private void observerList() {
        mViewModel.cavalos.observe(this, clientes ->{
            // update UI
            adapter.notifyDataSetChanged();
        });
    }

    private void inicializar() {
        cavalosRecycle = v.findViewById(R.id.cavalosRecycle);
        progressBarCavalos = v.findViewById(R.id.progressBarCavalos);

        contextoAtivity = getActivity();

        usuario = FirebaseAuth.getInstance();
        try {
            usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
        }catch (java.lang.NullPointerException e){
            usuarioRef = db.collection("users").document("zl1hFltVOlJONAVUeIsY");
        }
    }

}
