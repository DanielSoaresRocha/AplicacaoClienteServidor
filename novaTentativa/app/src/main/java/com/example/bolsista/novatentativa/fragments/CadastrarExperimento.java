package com.example.bolsista.novatentativa.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.IniciarConfiguracao;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.modelo.Experimento;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class CadastrarExperimento extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ListarViewModel mViewModel;
    private View v;
    private Context contextoAtivity;

    private Button finalizarExperimento;
    private EditText nomeExperimento;
    private ImageView dataExperimentoI;
    private TextView dataExperimentoT;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    Date dataExperimento;

    //FireBase FireStore
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference usuarioRef;
    //FireBase autenth
    private FirebaseAuth usuario;

    private static int numberExperiments = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cadastrar_experimento, container, false);
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ListarViewModel.class);
        // TODO: Use the ViewModel
        inicializar();
        listener();
    }

    private void listener() {
        finalizarExperimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instanciarExperimento();
            }
        });

        dataExperimentoI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void instanciarExperimento(){
        getActivity().finish();
        /*
        DocumentReference configuracaoRef = db.collection("configuracoes")
                .document(IniciarConfiguracao.configuracaoSelecionada.getId());
        DocumentReference equinoRef = db.collection("equinos")
                .document(IniciarConfiguracao.cavaloSelecionado.getId());

        IniciarConfiguracao.experimento = new Experimento("", configuracaoRef, usuarioRef, equinoRef, new Date(),
                nomeExperimento.getText().toString(), numberExperiments);

        addExperimentoToFireBase();
        ListarViewModel.addExperimento(IniciarConfiguracao.experimento);*/
    }

    private void addExperimentoToFireBase(){
        db.collection("experimentos")
                .add(IniciarConfiguracao.experimento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        IniciarConfiguracao.experimento.setId(documentReference.getId());
                        Toast.makeText(contextoAtivity, "Experimento adicionado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore-add", "Error adding document", e);
                    }
                });
        iniciarServidor();
    }

    public static void verififyNumberExperiments(String id){
        System.out.println("=>>>> ID = "+ id);
        DocumentReference equinoRef = db.collection("equinos")
                .document(id);

        db.collection("experimentos")
                .whereEqualTo("equino", equinoRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            numberExperiments = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("entrou "+numberExperiments+" vezes", document.getId()
                                        + " => " + document.getData());
                                numberExperiments++;
                            }

                            if(numberExperiments == 0)
                                numberExperiments = 1;
                        } else {
                            Log.d("notOK", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void iniciarServidor() {
        getActivity().finish();
        Intent iniciarServidor = new Intent(contextoAtivity, Servidor.class);
        startActivity(iniciarServidor);
    }

    private void showDatePickerDialog(){
        Calendar hoje = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                contextoAtivity,
                this,
                hoje.get(Calendar.YEAR),
                hoje.get(Calendar.MONTH),
                hoje.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Toast.makeText(contextoAtivity, dayOfMonth+"/"+(month+1)+"/"+year,Toast.LENGTH_SHORT).show();
        String dataRecebida = dayOfMonth+"/"+(month+1)+"/"+year;

        Date dataFormatada = formateDate(dataRecebida);
        dataExperimento = dataFormatada;
        dataExperimentoT.setText(dataRecebida);
    }

    private Date formateDate(String dataRecebida){
        Date dataFormatada = null;
        try {
            dataFormatada = formato.parse(dataRecebida);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dataFormatada;
    }

    private void inicializar() {
        finalizarExperimento = v.findViewById(R.id.finalizarExperimento);
        nomeExperimento = v.findViewById(R.id.nomeExperimento);
        contextoAtivity = getActivity();
        dataExperimentoT = v.findViewById(R.id.dataExperimentoT);
        dataExperimentoI = v.findViewById(R.id.dataExperimentoI);

        usuario = FirebaseAuth.getInstance();
        try {
            usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
        }catch (java.lang.NullPointerException e){
            usuarioRef = db.collection("users").document("zl1hFltVOlJONAVUeIsY");
        }
    }
}
