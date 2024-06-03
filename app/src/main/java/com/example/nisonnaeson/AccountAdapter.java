package com.example.nisonnaeson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private ArrayList<String> accountList;

    public AccountAdapter(ArrayList<String> accountList) {
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        String account = accountList.get(position);
        String[] accountData = account.split(" ");
        if (accountData.length == 5) { // 4 data fields + 1 currency field
            holder.textViewUsageDetails.setText(accountData[0]);
            holder.textViewCategory.setText(accountData[1]);
            holder.textViewDate.setText(accountData[2]);
            holder.textViewAmount.setText(accountData[3] + " " + accountData[4]);
        }
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsageDetails, textViewCategory, textViewDate, textViewAmount;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsageDetails = itemView.findViewById(R.id.textViewUsageDetails);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
        }
    }
}
