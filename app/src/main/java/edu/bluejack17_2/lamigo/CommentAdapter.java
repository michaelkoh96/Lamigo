package edu.bluejack17_2.lamigo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    public static ArrayList<Comment> comments;

    public ThreadActivity.ThreadFragment threadFragment;

    public Bundle args;
    public static String replyDesc="";
    public static String replyTo="";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public CommentAdapter(ArrayList<Comment> comments,ThreadActivity.ThreadFragment tf,Bundle extras) {
        setData(comments);
        threadFragment = tf;
        args = extras;
    }

    public void setData(ArrayList<Comment> comments){
        this.comments = comments;
    }

    @Override
    public int getItemCount() {
        return (comments != null) ? comments.size() : 0;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType){
            case 0:
                view = inflater.inflate(R.layout.thread_detail,parent,false);
                return new CommentAdapter.ViewHolder(view,viewType);
            default:
                view  = inflater.inflate(R.layout.list_post,parent,false);
                return new CommentAdapter.ViewHolder(view,viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ViewHolder holder, final int position) {

        if(position == 0){
            //set info thread detail


            //holder.txtDetailPoint.setText(args.getString("score"));
            holder.txtDetailTitle.setText(args.getString("title"));
            holder.txtDetailDate.setText(args.getString("date"));
            holder.txtDetailAuthor.setText(args.getString("author"));
            holder.txtDetailDesc.setText(args.getString("desc"));

//            holder.upvoteThread.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//
//            holder.downvoteThread.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });

            holder.shareThread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = "Thread: "+ args.getString("title") + "\n Question: "+args.getString("desc");
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, message);

                    view.getContext().startActivity(Intent.createChooser(share, "Share thread"));
                }
            });

        }else if (position > 0){
            holder.txtAuthorComment.setText(comments.get(position-1).username);

            if(comments.get(position-1).desc.contains("\n\n")){
                String quote = comments.get(position-1).desc.substring(0,comments.get(position-1).desc.indexOf("\n\n"));
                String reply = comments.get(position-1).desc.substring(comments.get(position-1).desc.indexOf("\n\n"),comments.get(position-1).desc.length());

                SpannableString str = new SpannableString(quote + reply);
                str.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, quote.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.txtComment.setText(str);

            }else{
                holder.txtComment.setText(comments.get(position-1).desc);
            }


            holder.txtDateComment.setText(comments.get(position-1).date);
            holder.txtPointComment.setText(comments.get(position-1).points);



            //tambain event di setiap button pada comment card
            holder.upvoteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final String currCommentId = comments.get(position-1).id;
                    mDatabase.child("Comment").child(currCommentId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Comment us = dataSnapshot.getValue(Comment.class);

                           int points = Integer.parseInt(us.points) + 1;
                            mDatabase.child("Comment").child(currCommentId).child("points").setValue(points+"");
                            //refresh
                            ThreadActivity.ThreadFragment.refresh();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            holder.downvoteComment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    final String currCommentId = comments.get(position-1).id;
                    mDatabase.child("Comment").child(currCommentId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Comment us = dataSnapshot.getValue(Comment.class);

                            int points = Integer.parseInt(us.points) - 1;
                            mDatabase.child("Comment").child(currCommentId).child("points").setValue(points+"");
                            //refresh
                            ThreadActivity.ThreadFragment.refresh();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            holder.deleteComment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mDatabase.child("Comment").child( comments.get(position-1).id).removeValue();
                    //refresh
                    ThreadActivity.ThreadFragment.refresh();
                }
            });

            holder.replyComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replyDesc = comments.get(position-1).desc;
                    replyTo = comments.get(position-1).username;
                    ThreadActivity.mViewPager.setCurrentItem(1);

                }
            });

            //hide tombol delete kalo bukan user yang buat comment ini
            if(!comments.get(position-1).userid.equals(mAuth.getCurrentUser().getUid().toString())) {
                holder.deleteComment.setVisibility(View.GONE);
            }else{
                holder.replyComment.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 1;
        if (position == 0){
            viewType = 0;
        }
        return viewType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //general
        private SwipeRefreshLayout srl2;

        //element dari comment
        private TextView txtPointComment,txtComment,txtDateComment,txtAuthorComment;
        private Button upvoteComment,downvoteComment,deleteComment,replyComment;


        //element dari thread detail
        private TextView txtDetailTitle,txtDetailDate,txtDetailAuthor,txtDetailDesc;
        private Button shareThread;
        //private Button upvoteThread,downvoteThread;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            srl2 = threadFragment.srl2;

            if(viewType == 0){
                //txtDetailPoint = itemView.findViewById(R.id.detailPoints);
                txtDetailTitle = itemView.findViewById(R.id.detailTitle);
                txtDetailDate = itemView.findViewById(R.id.detailDate);
                txtDetailAuthor = itemView.findViewById(R.id.detailAuthor);
                txtDetailDesc = itemView.findViewById(R.id.detailDesc);
//                upvoteThread = itemView.findViewById(R.id.dupvoteBtn);
//                downvoteThread = itemView.findViewById(R.id.ddownvoteBtn);
                shareThread = itemView.findViewById(R.id.shareBtn);
            }else {
                txtPointComment = itemView.findViewById(R.id.commentPoints);
                txtComment = itemView.findViewById(R.id.commentDesc);
                txtDateComment = itemView.findViewById(R.id.commentDate);
                upvoteComment = itemView.findViewById(R.id.upvoteBtn);
                downvoteComment = itemView.findViewById(R.id.downvoteBtn);
                deleteComment = itemView.findViewById(R.id.deleteComment);
                txtAuthorComment = itemView.findViewById(R.id.commentAuthor);
                replyComment = itemView.findViewById(R.id.replyComment);

            }

        }
    }
}
