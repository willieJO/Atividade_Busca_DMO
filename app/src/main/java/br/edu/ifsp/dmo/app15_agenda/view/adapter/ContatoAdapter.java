package br.edu.ifsp.dmo.app15_agenda.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

import br.edu.ifsp.dmo.app15_agenda.R;
import br.edu.ifsp.dmo.app15_agenda.model.Contato;
import br.edu.ifsp.dmo.app15_agenda.view.ItemCliclListener;

public class ContatoAdapter extends FirestoreRecyclerAdapter<Contato, ContatoAdapter.ContatoViewHolder> {

    private ItemCliclListener clickListener;

    public ContatoAdapter(@NonNull FirestoreRecyclerOptions<Contato> options) {
        super(options);
    }

    public void setClickListener(ItemCliclListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contato_celula, parent, false);
        ContatoViewHolder holder = new ContatoViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ContatoViewHolder holder, int position, @NonNull Contato model) {
        holder.nomeTextView.setText(model.getNome());
    }

    public class ContatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nomeTextView;

        public ContatoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.textview_nome);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick( getSnapshots().getSnapshot(getBindingAdapterPosition()).getId() );
        }
    }
}
