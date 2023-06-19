package br.edu.ifsp.dmo.app15_agenda.mvp;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;

public interface DetalhesMVP {

    interface View{
        Context getContext();

    }

    interface Presenter{
        void detach();

        void updateUI(Intent intent, EditText name, EditText phone, EditText email, ActionBar toolBar);

        boolean isNewContact();

        void saveNewContact(String name, String phone, String email);

        void updateContact(String name, String phone, String email);

        void deleteContact();
    }

}
