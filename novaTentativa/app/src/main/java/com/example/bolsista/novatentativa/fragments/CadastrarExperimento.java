package com.example.bolsista.novatentativa.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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

import com.example.bolsista.novatentativa.NovoExperimento;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private Date dataExperimento;

    //FireBase FireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    NovoExperimento novoExperimento;

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
        Experimento experimento = new Experimento("",novoExperimento.getUsuarioRef().getId(),
                novoExperimento.getEquinoSelecionado(), nomeExperimento.getText().toString(),
                dataExperimento, new Date(), novoExperimento.getTestes(), false);

        novoExperimento.setExperimento(experimento);
        String validacao = novoExperimento.validar();

        if (validacao.equals("ok"))
            addExperimentoToFireBase(experimento);
        else
            Toast.makeText(contextoAtivity, validacao, Toast.LENGTH_SHORT).show();
    }

    private void addExperimentoToFireBase(Experimento experimento){
        db.collection("experimentos")
                .add(experimento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        novoExperimento.setExperimento(experimento);
                        novoExperimento.getExperimento().setId(documentReference.getId());
                        Toast.makeText(contextoAtivity, "Experimento adicionado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore-add", "Error adding document", e);
                    }
                });

        getActivity().finish();
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

        novoExperimento = (NovoExperimento) getActivity();


    }
}
