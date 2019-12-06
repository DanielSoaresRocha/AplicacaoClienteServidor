package com.example.bolsista.novatentativa.banco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.drakeet.materialdialog.MaterialDialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.CavaloAdapter;
import com.example.bolsista.novatentativa.modelo.Cavalo;
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

public class ListarCavalos extends AppCompatActivity {
    RecyclerView recyclerView;


    List<Cavalo> cavalos = new ArrayList<Cavalo>();
    Cavalo cavalo;

    CavaloAdapter adapter;

    //FireBase FireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    Context contextoAtivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cavalos);

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());

        contextoAtivity = this;

        inicializar();
        getCavalosFireStore();
    }

    public void getCavalosFireStore(){

        Source source = Source.CACHE;

        db.collection("equinos")
                .whereEqualTo("referencia", usuarioRef)//referencia do usuario que adicionou o cavalo
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cavalo = document.toObject(Cavalo.class);
                                cavalos.add(cavalo);
                                Log.i("DataBase-FireStore-get", "referencia de => ." +
                                        cavalo.getNome() + " = " +
                                        document.getDocumentReference("referencia").getId());
                                }
                                implementsRecycle();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void implementsRecycle(){
        adapter = new CavaloAdapter(contextoAtivity,cavalos);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextoAtivity,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layout);

        recyclerView.addOnItemTouchListener(
                new ListarCavalosOnItemTouch(
                        getApplicationContext(),
                        recyclerView,
                        new ListarCavalosOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(getApplicationContext(),"Clique simples",
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

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public void inicializar(){
        recyclerView = findViewById(R.id.recycleView);
    }
}
