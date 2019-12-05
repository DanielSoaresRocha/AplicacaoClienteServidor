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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Cavalo> cavalos = new ArrayList<Cavalo>();
    Cavalo cavalo;

    CavaloAdapter adapter;

    FirebaseAuth usuario;

    DocumentReference usuarioRef;

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
        adapter = new CavaloAdapter(getApplicationContext(),cavalos);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
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
                                View layout = LayoutInflater.from(contextoAtivity).inflate(R.layout.cavalo_inflater,null,false);

                                TextView nomeCavalo =(TextView) layout.findViewById(R.id.nomeCavalo);
                                TextView detalhes =(TextView) layout.findViewById(R.id.detalhesCavalo);

                                nomeCavalo.setText(cavalos.get(position).getNome());
                                detalhes.setText(cavalos.get(position).getDetalhes());

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
