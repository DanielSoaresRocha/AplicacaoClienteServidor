package com.example.bolsista.novatentativa.cadastros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.FixedTabsPageAdapter;
import com.example.bolsista.novatentativa.fragments.EquinosGerenciar;
import com.example.bolsista.novatentativa.fragments.ExperimentosGerenciar;
import com.example.bolsista.novatentativa.fragments.TestesGerenciar;
import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Gerenciar extends AppCompatActivity {
    ViewPager viewPagerEdit;
    TabLayout tabLayoutEdit;

    FixedTabsPageAdapter pageAdapter;
    public static int identificaAba = 0; //Identifica aba em que está no momento

    ArrayList<Equino> equinos;
    ArrayList<Experimento> experimentos;
    ArrayList<Teste> testes;

    //FireBase FireStore
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_excluir);

        inicializar();
        pageAdapter();
    }

    private void pageAdapter(){
        pageAdapter = new FixedTabsPageAdapter(getSupportFragmentManager());
        pageAdapter.adicionar(new EquinosGerenciar(), "Equinos");
        pageAdapter.adicionar(new TestesGerenciar(), "Testes");
        pageAdapter.adicionar(new ExperimentosGerenciar(), "Experimentos");

        viewPagerEdit.setAdapter(pageAdapter);
        tabLayoutEdit.setupWithViewPager(viewPagerEdit);

        viewPagerEdit.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        identificaAba = 0;
                        break;
                    case 1:
                        identificaAba = 1;
                        break;
                    case 2:
                        identificaAba = 2;
                        break;
                    case 3:
                        identificaAba = 3;
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void inicializar() {
        viewPagerEdit = findViewById(R.id.viewPagerEdit);
        tabLayoutEdit = findViewById(R.id.tabLayoutEdit);

        equinos = new ArrayList<>();
        experimentos = new ArrayList<>();
        testes = new ArrayList<>();

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());

    }

    public ArrayList<Teste> getTestes() {
        return testes;
    }

    public void setTestes(ArrayList<Teste> testes) {
        this.testes = testes;
    }

    public ArrayList<Equino> getEquinos() {
        return equinos;
    }

    public void setEquinos(ArrayList<Equino> equinos) {
        this.equinos = equinos;
    }

    public ArrayList<Experimento> getExperimentos() {
        return experimentos;
    }

    public void setExperimentos(ArrayList<Experimento> experimentos) {
        this.experimentos = experimentos;
    }
}