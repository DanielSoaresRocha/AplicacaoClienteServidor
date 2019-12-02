package com.example.bolsista.novatentativa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListarCavalos extends AppCompatActivity {
    RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Cavalo> cavalos = new ArrayList<Cavalo>();
    Cavalo cavalo;

    CavaloAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cavalos);

        inicializar();

        getCavalosFireStore();
    }

    public void getCavalosFireStore(){

        db.collection("equinos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cavalo = document.toObject(Cavalo.class);
                                cavalos.add(cavalo);
                                }
                                adapter = new CavaloAdapter(getApplicationContext(),cavalos);
                                recyclerView.setAdapter(adapter);

                                LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                                recyclerView.setLayoutManager(layout);
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void inicializar(){
        recyclerView = findViewById(R.id.recycleView);
    }
}
