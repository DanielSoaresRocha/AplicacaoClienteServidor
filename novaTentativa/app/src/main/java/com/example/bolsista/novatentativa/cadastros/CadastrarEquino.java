package com.example.bolsista.novatentativa.cadastros;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Equino;
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

/*
* sexo
* atividade
*/

public class CadastrarEquino extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    Button cadastrarCavaloBtn,cancelarBtn;
    EditText nome, detalhes;
    Context contextActivity;
    ImageView dataNascimentoI;
    TextView dataNascimentoE;
    Spinner ativitiesSpinner, racasSpinner;
    LinearLayout problemaSaudeLinear;

    ArrayAdapter<CharSequence> ativitiesAdapter, racasAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    Date dataNascimento;
    String atividade, racaText;

    String sexo, sistemaCriacao, atividadeSemanal, itensidadeAtividade, temperamento, problemaSaude;
    boolean suplementacaoMineral, vicio, isProblemaSaude;

    Equino equino;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cavalo);

        inicializar();
        spinners();
        listener();
    }

    private void listener() {
        ativitiesSpinner.setOnItemSelectedListener(this);
        racasSpinner.setOnItemSelectedListener(this);

        cadastrarCavaloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((dataNascimentoE.getText().toString().length() >= 8) && (nome.getText().toString()
                        .length() > 1)) {

                    equino = new Equino(nome.getText().toString(), racaText,
                            dataNascimento, detalhes.getText().toString(),sexo, atividade,
                            sistemaCriacao, atividadeSemanal, itensidadeAtividade, suplementacaoMineral,
                            temperamento, vicio, isProblemaSaude, problemaSaude);

                    addFireStore();

                    nome.setText("");
                    dataNascimentoE.setText("");
                    detalhes.setText("");
                    Toast.makeText(getApplicationContext(), "Cavalo cadastrado", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Preencha os campos obrigatórios",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dataNascimentoI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    public void spinners(){
        // atividades

        ativitiesAdapter = ArrayAdapter.createFromResource(contextActivity,
                R.array.ativities_array, android.R.layout.simple_spinner_item);
        ativitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ativitiesSpinner.setAdapter(ativitiesAdapter);
        // raças

        racasAdapter = ArrayAdapter.createFromResource(contextActivity,
                R.array.racas_array, android.R.layout.simple_spinner_item);
        racasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        racasSpinner.setAdapter(racasAdapter);
    }

    public void onRadioButtonClickedSexo(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.macho:
                if (checked)
                    sexo = "Macho";
                    break;
            case R.id.femea:
                if (checked)
                    sexo = "Fêmea";
                    break;
        }
    }

    public void onRadioButtonClickedCriacao(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        if(view.getId() == R.id.baias || view.getId() == R.id.piqueteSozinho ||
                view.getId() == R.id.piqueteGrupo){
            if (checked)
                sistemaCriacao = ((RadioButton) view).getText().toString();
        }
    }

    public void onRadioButtonClickedVezes(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        if(view.getId() == R.id.vezesX1 || view.getId() == R.id.vezesX2 ||
                view.getId() == R.id.vezesX3 || view.getId() == R.id.vezesX4 ||
                view.getId() == R.id.vezesX5 || view.getId() == R.id.vezesX6 ||
                view.getId() == R.id.vezesX7){
            if (checked)
                atividadeSemanal = ((RadioButton) view).getText().toString();
        }
    }

    public void onRadioButtonClickedItensidade(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        if(view.getId() == R.id.moderada || view.getId() == R.id.leve ||
                view.getId() == R.id.pesada){
            if (checked)
                itensidadeAtividade = ((RadioButton) view).getText().toString();
        }
    }

    public void onRadioButtonClickedMineral(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.isMineral:
                if (checked)
                    suplementacaoMineral = true;
                break;
            case R.id.notMineral:
                if (checked)
                    suplementacaoMineral = false;
                break;
        }
    }

    public void onRadioButtonClickedTemperamento(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        if(view.getId() == R.id.frio || view.getId() == R.id.morno ||
                view.getId() == R.id.quente){
            if (checked)
                temperamento = ((RadioButton) view).getText().toString();
        }
    }

    public void onRadioButtonClickedVicio(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.isVicio:
                if (checked)
                    vicio = true;
                break;
            case R.id.notVicio:
                if (checked)
                    vicio = false;
                break;
        }
    }

    public void onRadioButtonClickedIsProblemaSaude(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.isProblemaSaude:
                if (checked)
                    isProblemaSaude = true;
                    problemaSaudeLinear.setVisibility(View.VISIBLE);
                break;
            case R.id.notProblemaSaude:
                if (checked)
                    isProblemaSaude = false;
                    problemaSaudeLinear.setVisibility(View.INVISIBLE);
                    problemaSaude = "";
                break;
        }
    }

    public void onRadioButtonClickedProblemaSaude(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        if(view.getId() == R.id.articular || view.getId() == R.id.anemia ||
                view.getId() == R.id.digestivo_colica){
            if (checked)
                problemaSaude = ((RadioButton) view).getText().toString();
        }
    }

    public void addFireStore(){
        db.collection("equinos")
                .add(equino)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        equino.setId(documentReference.getId());
                        ListarViewModel.addCavalo(equino);
                        Toast.makeText(contextActivity, "Equino adicionado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore-add", "Error adding document", e);
                    }
                });
    }

    private void showDatePickerDialog(){
        Calendar hoje = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                contextActivity,
                this,
                hoje.get(Calendar.YEAR),
                hoje.get(Calendar.MONTH),
                hoje.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Toast.makeText(contextActivity, dayOfMonth+"/"+(month+1)+"/"+year,Toast.LENGTH_SHORT).show();
        String dataRecebida = dayOfMonth+"/"+(month+1)+"/"+year;

        Date dataFormatada = formateDate(dataRecebida);
        dataNascimento = dataFormatada;
        dataNascimentoE.setText(dataRecebida);
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
        cadastrarCavaloBtn = findViewById(R.id.cadastrarCavaloBtn);
        cancelarBtn = findViewById(R.id.cancelarBtn);
        nome = findViewById(R.id.nomeTextView);
        detalhes = findViewById(R.id.detalhesTextView);
        dataNascimentoI =findViewById(R.id.dataNascimentoI);
        dataNascimentoE = findViewById(R.id.dataNascimentoE);
        ativitiesSpinner = findViewById(R.id.ativitiesSpinner);
        contextActivity = this;
        ativitiesSpinner = findViewById(R.id.ativitiesSpinner);
        racasSpinner = findViewById(R.id.racasSpinner);
        problemaSaudeLinear = findViewById(R.id.problemaSaudeLinear);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();

        switch(parent.getId()){
            case R.id.ativitiesSpinner:
                atividade = text;
                break;
            case  R.id.racasSpinner:
                racaText = text;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
