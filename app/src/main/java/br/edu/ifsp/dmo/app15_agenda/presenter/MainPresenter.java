package br.edu.ifsp.dmo.app15_agenda.presenter;

import android.app.SearchManager;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import br.edu.ifsp.dmo.app15_agenda.constant.Constants;
import br.edu.ifsp.dmo.app15_agenda.model.Contato;
import br.edu.ifsp.dmo.app15_agenda.mvp.MainMVP;
import br.edu.ifsp.dmo.app15_agenda.view.DetalhesActivity;
import br.edu.ifsp.dmo.app15_agenda.view.ItemCliclListener;
import br.edu.ifsp.dmo.app15_agenda.view.adapter.ContatoAdapter;

public class MainPresenter implements MainMVP.Presenter {
    private MainMVP.View view;
    private FirebaseFirestore database;
    private ContatoAdapter adapter;

    public MainPresenter(MainMVP.View view) {
        this.view = view;
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public void detach() {
        this.view = null;
    }


    @Override
    public void populate(RecyclerView recyclerView) {
        Query query = database.collection(Constants.CONTACTS_COLLECTION)
                .orderBy(Constants.ATTR_NAME, Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Contato> options = new FirestoreRecyclerOptions
                .Builder<Contato>()
                .setQuery(query, Contato.class).build();

        adapter = new ContatoAdapter(options);
        adapter.setClickListener(new ItemCliclListener() {
            @Override
            public void onClick(String referenceId) {
                abrirDetalhes(referenceId);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void populate(RecyclerView recyclerView, String filter) {
        Query query = database.collection(Constants.CONTACTS_COLLECTION)
                .orderBy(Constants.ATTR_NAME, Query.Direction.ASCENDING);

        if (filter.length() > 0) {
            String proximaLetra = filter.substring(0, filter.length() - 1) + (char) (filter.charAt(filter.length() - 1) + 1);
            query = query.whereGreaterThanOrEqualTo(Constants.ATTR_NAME, filter)
                    .whereLessThan(Constants.ATTR_NAME, proximaLetra);
        }

        FirestoreRecyclerOptions<Contato> options = new FirestoreRecyclerOptions
                .Builder<Contato>()
                .setQuery(query, Contato.class).build();



        adapter = new ContatoAdapter(options);
        adapter.setClickListener(new ItemCliclListener() {
            @Override
            public void onClick(String referenceId) {
                abrirDetalhes(referenceId);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        startListener();
    }

    @Override
    public void startListener() {
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void stopListener() {
        if (adapter != null)
            adapter.stopListening();
    }

    private void abrirDetalhes(String documento) {
        Intent intent = new Intent(view.getContext(), DetalhesActivity.class);
        intent.putExtra(Constants.FIRESOTRE_DOCUMENT_KEY, documento);
        view.getContext().startActivity(intent);
    }
}
