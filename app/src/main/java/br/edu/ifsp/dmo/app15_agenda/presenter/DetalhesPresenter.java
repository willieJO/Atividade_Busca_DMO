package br.edu.ifsp.dmo.app15_agenda.presenter;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.ktx.Firebase;

import java.util.concurrent.Executor;

import br.edu.ifsp.dmo.app15_agenda.constant.Constants;
import br.edu.ifsp.dmo.app15_agenda.model.Contato;
import br.edu.ifsp.dmo.app15_agenda.mvp.DetalhesMVP;

public class DetalhesPresenter implements DetalhesMVP.Presenter {

    private DetalhesMVP.View view;
    private FirebaseFirestore database;
    private String firestoreId = "";
    private Contato contato = null;

    public DetalhesPresenter(DetalhesMVP.View view) {
        this.view = view;
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void updateUI(Intent intent, EditText name, EditText phone, EditText email, ActionBar toolBar) {
        if(intent.hasExtra(Constants.FIRESOTRE_DOCUMENT_KEY)){
            firestoreId = intent.getStringExtra(Constants.FIRESOTRE_DOCUMENT_KEY);
            database.collection(Constants.CONTACTS_COLLECTION).document(firestoreId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value != null){
                        contato = value.toObject(Contato.class);
                        if(contato != null) {
                            name.setText(contato.getNome());
                            phone.setText(contato.getTelefone());
                            email.setText(contato.getEmail());

                            String firstName = contato.getNome().split(" ")[0];
                            toolBar.setTitle(firstName.toUpperCase());
                        }
                    }
                }
            });
        }else{
            toolBar.setTitle("Novo Contato");
        }
    }

    @Override
    public boolean isNewContact() {
        return firestoreId.isEmpty();
    }

    @Override
    public void saveNewContact(String name, String phone, String email) {
        Contato contato;
        contato = new Contato(name, phone, email);

        CollectionReference contatosRef = database.collection(Constants.CONTACTS_COLLECTION);

        contatosRef.add(contato)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(view.getContext(), "Contato cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "Erro ao salvar contato.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void updateContact(String name, String phone, String email) {
        Contato contato;
        contato = new Contato(name, phone, email);

        CollectionReference contatosRef = database.collection(Constants.CONTACTS_COLLECTION);

        contatosRef.document(firestoreId)
                .set(contato)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(view.getContext(), "Contato atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "Erro ao atualizar contato.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void deleteContact() {
        if(!firestoreId.isEmpty()) {
            CollectionReference contatosRef = database.collection(Constants.CONTACTS_COLLECTION);
            contatosRef.document(firestoreId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Contato removido com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "Erro ao deletar contato.", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
}
