package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.app.melhorevento.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import model.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.MyViewHolder>{

    private List<Evento> produtos;
    private Context context;
    private StorageReference imagem = FirebaseStorage.getInstance().getReference("eventos");

    public EventoAdapter(List<Evento> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.evento_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        Evento evento = produtos.get(i);
        StorageReference img = imagem.child(evento.getId()+".jpeg");
        img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri){
                Glide.with(context)
                        .load(uri)
                        .into(holder.imagemEvento);
            }
        });
        holder.nomeEvento.setText(evento.getTitulo());
        holder.descricaoEvento.setText(evento.getDescricao());
        holder.nomeEmpresa.setText(evento.getPessoaJuridica().getNome());
        String dia, mes;
        if(evento.getDia()<10){
            dia = "0"+evento.getDia().toString();
        }
        else{
            dia = evento.getDia().toString();
        }
        if(evento.getMes()<10){
            mes = "0"+evento.getMes().toString();
        }
        else{
            mes = evento.getMes().toString();
        }
        holder.dataEvento.setText(dia+"/"+mes+"/"+evento.getAno());
        holder.horaEvento.setText(evento.getHora()+":"+evento.getMinuto());
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemEvento;
        TextView nomeEvento;
        TextView descricaoEvento;
        TextView nomeEmpresa;
        TextView dataEvento;
        TextView horaEvento;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagemEvento = itemView.findViewById(R.id.imageViewImagemEvento);
            nomeEvento = itemView.findViewById(R.id.textViewNomeEvento);
            descricaoEvento = itemView.findViewById(R.id.textViewDescricaoEvento);
            nomeEmpresa = itemView.findViewById(R.id.textViewNomeEmpresa);
            dataEvento = itemView.findViewById(R.id.textViewDataEvento);
            horaEvento = itemView.findViewById(R.id.textViewHoraEvento);
        }
    }
}
