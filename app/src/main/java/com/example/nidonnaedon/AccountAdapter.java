package com.example.nidonnaedon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nidonnaedon.R;

import java.util.ArrayList;

public class AccountAdapter extends ArrayAdapter<Account> {

    public AccountAdapter(Context context, ArrayList<Account> accounts) {
        super(context, 0, accounts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_account, parent, false);
        }

        TextView usageDetails = convertView.findViewById(R.id.usageDetails);
        TextView category = convertView.findViewById(R.id.category);
        TextView date = convertView.findViewById(R.id.date);
        TextView amount = convertView.findViewById(R.id.amount);

        usageDetails.setText(account.getUsageDetails());
        category.setText(account.getCategory());
        date.setText(account.getDate());

        // amount에서 필요한 부분만 추출하여 설정
        String fullAmount = account.getAmount();
        String shortAmount = fullAmount.split("-")[0];
        amount.setText(shortAmount);

        return convertView;
    }
}
