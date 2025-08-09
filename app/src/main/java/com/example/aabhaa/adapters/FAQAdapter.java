package com.example.aabhaa.adapters;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aabhaa.R;
import com.example.aabhaa.models.FAQ;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private List<FAQ> faqList;

    public FAQAdapter(List<FAQ> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQ faq = faqList.get(position);
        holder.txtQuestion.setText(faq.getQuestion());
        holder.txtAnswer.setText(faq.getAnswer());

        boolean expanded = faq.isExpanded();
        holder.txtAnswer.setVisibility(expanded ? View.VISIBLE : View.GONE);


        holder.imgArrow.setRotation(expanded ? 90f : 0f);

        holder.itemView.setOnClickListener(v -> {
            faq.setExpanded(!faq.isExpanded());
            notifyItemChanged(position);

            ObjectAnimator animator = ObjectAnimator.ofFloat(
                    holder.imgArrow,
                    "rotation",
                    faq.isExpanded() ? 0f : 90f,
                    faq.isExpanded() ? 90f : 0f
            );
            animator.setDuration(200);
            animator.start();
        });
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion, txtAnswer;
        ImageView imgArrow;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.tvQuestion);
            txtAnswer = itemView.findViewById(R.id.tvAnswer);
            imgArrow = itemView.findViewById(R.id.imgArrow);
        }
    }
}

