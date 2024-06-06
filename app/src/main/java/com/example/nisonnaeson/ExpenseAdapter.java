package com.example.nisonnaeson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<String> expenseList;
    private OnItemClickListener listener;
    private Context context;

    public ExpenseAdapter(List<String> expenseList, OnItemClickListener listener, Context context) {
        this.expenseList = expenseList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        String expense = expenseList.get(holder.getAdapterPosition());
        holder.expenseTextView.setText(expense);

        // 배경색 설정
        if (holder.getAdapterPosition() % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green)); // 짝수 행 배경색
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray)); // 홀수 행 배경색
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public TextView expenseTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTextView = itemView.findViewById(R.id.expense_text);
        }
    }
}
