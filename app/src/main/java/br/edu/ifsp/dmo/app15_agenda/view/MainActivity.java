package br.edu.ifsp.dmo.app15_agenda.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.edu.ifsp.dmo.app15_agenda.R;
import br.edu.ifsp.dmo.app15_agenda.mvp.MainMVP;
import br.edu.ifsp.dmo.app15_agenda.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity
        implements MainMVP.View, View.OnClickListener {

    private MainMVP.Presenter presenter;
    private FloatingActionButton mActionButton;
    private RecyclerView mRecyclerView;
    private TextView filtro;
    private String inputAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_main);
        inputAnterior = "";
        findById();
        setListener();
        presenter = new MainPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.populate(mRecyclerView);
        presenter.startListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopListener();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v == mActionButton){
            Intent intent = new Intent(this, DetalhesActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void findById(){
        mActionButton = findViewById(R.id.fab_new_contact);
        mRecyclerView = findViewById(R.id.recyler_view);
        filtro = findViewById(R.id.filtroText);
    }

    private void setListener(){
        mActionButton.setOnClickListener(this);
        filtro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar este método neste caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Aqui você pode realizar o evento a cada letra digitada ou removida
                String input = s.toString();
                if (!input.equals(inputAnterior)) {
                    presenter.populate(mRecyclerView, input);
                }
                inputAnterior = input;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não é necessário implementar este método neste caso
            }
        });

    }
}