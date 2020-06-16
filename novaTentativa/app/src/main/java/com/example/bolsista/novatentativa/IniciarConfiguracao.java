package com.example.bolsista.novatentativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.bolsista.novatentativa.adapters.FixedTabsPageAdapter;
import com.example.bolsista.novatentativa.fragments.CadastrarExperimento;
import com.example.bolsista.novatentativa.fragments.ListarCavalos;
import com.example.bolsista.novatentativa.fragments.ListarTestes;
import com.example.bolsista.novatentativa.modelo.Cavalo;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.material.tabs.TabLayout;

public class IniciarConfiguracao extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    public static ListarViewModel mViewModel;

    FixedTabsPageAdapter pageAdapter;
    public static int identificaAba = 0; //Identifica aba em que está no momento

    public static Cavalo cavaloSelecionado;
    public static Configuracao configuracaoSelecionada;
    public static Experimento experimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_configuracao);

        inicializar();
        pageAdapter();
    }

    private void pageAdapter() {
        pageAdapter = new FixedTabsPageAdapter(getSupportFragmentManager());
        pageAdapter.adicionar(new ListarCavalos(), "Equino");
        pageAdapter.adicionar(new ListarTestes(), "Testes");
        pageAdapter.adicionar(new CadastrarExperimento(), "Experimento");

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        mViewModel = ViewModelProviders.of(this).get(ListarViewModel.class);
    }
}
