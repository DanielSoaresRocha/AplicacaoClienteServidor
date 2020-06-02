package com.example.bolsista.novatentativa.banco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Cavalo;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
* sexo
* atividade
*/

public class CadastrarCavalo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Button cadastrarCavaloBtn,cancelarBtn;
    EditText nome, raca, detalhes;
    Context contextActivity;
    ImageView dataNascimentoI;
    TextView dataNascimentoE;
    AutoCompleteTextView autoComplete;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    Date dataNascimento;
    String sexo = "";
    private static final String[] tasks = new String[] {
            "Equitação (escola)", "Passeio", "Enduro", "Vaquejada", "Tambores", "Lida pecuária",
            "Militar", "Equoterapia", "Adestramento", "Volteio", "Corrida/Turfe"
    };

    Cavalo cavalo;
    FirebaseAuth usuario;
    DocumentReference usuarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cavalo);

        inicializar();
        listener();

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
    }

    private void listener() {
        cadastrarCavaloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((dataNascimentoE.getText().toString().length() >= 8) && (nome.getText().toString()
                        .length() > 1)) {
                    cavalo = new Cavalo(nome.getText().toString(), raca.getText().toString(),
                            dataNascimento, detalhes.getText().toString(),sexo, autoComplete.getText().toString(), usuarioRef);

                    addFireStore();

                    nome.setText("");
                    raca.setText("");
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

    public void onRadioButtonClicked(View view) {
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

    public void addFireStore(){
        db.collection("equinos")
                .add(cavalo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        cavalo.setId(documentReference.getId());
                        ListarViewModel.addCavalo(cavalo);
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
        raca = findViewById(R.id.racaTextView);
        detalhes = findViewById(R.id.detalhesTextView);
        dataNascimentoI =findViewById(R.id.dataNascimentoI);
        dataNascimentoE = findViewById(R.id.dataNascimentoE);
        autoComplete = findViewById(R.id.autoComplete);
        contextActivity = this;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contextActivity,
                android.R.layout.simple_dropdown_item_1line, tasks);
        autoComplete.setAdapter(adapter);
        //autoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }
}
