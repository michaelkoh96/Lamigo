package edu.bluejack17_2.lamigo;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable{

    public ArrayList<Thread> threads;
    public ArrayList<Thread> filteredThreads = new ArrayList<>();
//    RecyclerViewAdapter rvAdapter = ThreadShared.getInstance();
    public ContentFragment contentFragment;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public RecyclerViewAdapter(ArrayList<Thread> threads,ContentFragment cf){
        setData(threads);
        contentFragment = cf;
    }

    public void setData(ArrayList<Thread> threads){
        this.threads = threads;
    }

    @Override
    public int getItemCount() {
        return (threads != null) ? threads.size() : 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtTitle.setText(threads.get(position).getTitle());
        //holder.txtScore.setText(threads.get(position).getScore());
        holder.txtDate.setText(threads.get(position).getDate());
        holder.txtAuthor.setText(threads.get(position).author);

        if(threads.get(position).language.equals("English")){
            holder.iv.setImageResource(R.drawable.uk);
        }else if(threads.get(position).language.equals("France")){
            holder.iv.setImageResource(R.drawable.france);
        }else if(threads.get(position).language.equals("Indonesian")){
            holder.iv.setImageResource(R.drawable.indonesia);
        }else if(threads.get(position).language.equals("German")){
            holder.iv.setImageResource(R.drawable.germany);
        }else if(threads.get(position).language.equals("Spanish")){
            holder.iv.setImageResource(R.drawable.spain);
        }



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mDatabase.child("Thread").child( threads.get(position).id).removeValue();
               holder.srl.setRefreshing(true);
               contentFragment.refresh();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.srl.setRefreshing(false);
                    }
                },1000);
                notifyDataSetChanged();
            }
        });



        //hide delete button kalo bukan user yg login yg buat thread
        if(!threads.get(position).userid.equals(mAuth.getCurrentUser().getUid().toString())) {
            holder.delete.setVisibility(View.GONE);
        }

        holder.card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(),threads.get(position).getTitle(),Toast.LENGTH_SHORT).show();

                //code action listener untuk pindah ke thread (baru pindah doang blm lempar informasi thread nya)
                Intent toThread =  new Intent(view.getContext(),ThreadActivity.class);
				toThread.putExtra("title", threads.get(position).title);
				toThread.putExtra("score",threads.get(position).score);
                toThread.putExtra("date",threads.get(position).date);
                toThread.putExtra("desc",threads.get(position).desc);
                toThread.putExtra("id",threads.get(position).id);
                toThread.putExtra("author",threads.get(position).author);
				
                view.getContext().startActivity(toThread);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searched = charSequence.toString();
                filteredThreads.clear();
                if (searched.isEmpty()) {
                    filteredThreads = threads;
                } else {
                    for (Thread thread : threads) {

                        if (thread.title.toLowerCase().contains(searched.toLowerCase())) {
                            filteredThreads.add(thread);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredThreads;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //filteredThreads = (ArrayList<Thread>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle,txtDate,txtAuthor;
//        private TextView txtScore;
        private CardView card;
        private Button delete;
        private SwipeRefreshLayout srl;
        private ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.contentCard);
            txtTitle = itemView.findViewById(R.id.threadListTitle);
            //txtScore = itemView.findViewById(R.id.threadListScore);
            txtDate = itemView.findViewById(R.id.threadListDate);
            delete = itemView.findViewById(R.id.deleteThread);
            txtAuthor = itemView.findViewById(R.id.threadAuthor);
            srl = contentFragment.srl;
            iv = itemView.findViewById(R.id.flag_icon);
        }
    }
}
