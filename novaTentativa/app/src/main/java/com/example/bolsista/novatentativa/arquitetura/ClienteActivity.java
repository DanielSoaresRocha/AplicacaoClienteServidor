package com.example.bolsista.novatentativa.arquitetura;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.sockets.Cliente;
import com.example.bolsista.novatentativa.Jogar;
import com.example.bolsista.novatentativa.R;

public class ClienteActivity extends AppCompatActivity {
    public LinearLayout identificacaoCliente;
    public TextView numIdentificacao;
    public EditText ipClientEdit;
    public Button criarClientBtn;
    public Button comecarClientBtn;

    static Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        inicializar();
        comecar();
        listener();

    }

    private void listener() {
        criarClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarCliente();
            }
        });

        comecarClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jogar = new Intent(ClienteActivity.this,Jogar.class);

                startActivity(jogar);
            }
        });
    }

    public static void enviar() {
        cliente.escrever();
    }

    public void criarCliente(){
        if(ipClientEdit.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),R.string.ip_invalido, Toast.LENGTH_LONG).show();
        }else {
            final ClienteActivity client = this;
            cliente = new Cliente(ipClientEdit.getText().toString(),client,false);
            cliente.connect();
        }
    }

    private void comecar() {
        comecarClientBtn.setVisibility(View.GONE);
    }

    private void inicializar() {
        ipClientEdit = findViewById(R.id.ipClientEdit);

        criarClientBtn = findViewById(R.id.criarClientBtn);
        comecarClientBtn = findViewById(R.id.comecarClientBtn);
        identificacaoCliente = findViewById(R.id.identificacaoCliente);
        numIdentificacao = findViewById(R.id.numIdentificacao);
    }
}
