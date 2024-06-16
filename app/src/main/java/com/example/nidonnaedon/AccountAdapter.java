package com.example.nidonnaedon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.nidonnaedon.R;
import java.util.ArrayList;

public class AccountAdapter extends ArrayAdapter<ExpenditureDetailsDTO> {

    public AccountAdapter(Context context, ArrayList<ExpenditureDetailsDTO> expenditures) {
        super(context, 0, expenditures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpenditureDetailsDTO expenditure = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_account, parent, false);
        }

        TextView expenditureName = convertView.findViewById(R.id.usageDetails);
        TextView expenditureCategory = convertView.findViewById(R.id.category);
        TextView expenditureDate = convertView.findViewById(R.id.date);
        TextView expenditureAmount = convertView.findViewById(R.id.amount);

        expenditureName.setText(expenditure.getExpenditureName());
        expenditureCategory.setText(expenditure.getExpenditureCategory());
        expenditureDate.setText(expenditure.getExpenditureDate());

        // 통화의 첫 단어만 표시
        String currency = expenditure.getExpenditureCurrency();
        if (currency != null && currency.contains("-")) {
            currency = currency.split("-")[0].trim();
        } else if (currency == null) {
            currency = "";
        }

        // 금액과 통화를 합쳐서 표시
        String fullAmount = expenditure.getExpenditureAmount() + " " + currency;
        expenditureAmount.setText(fullAmount);

        return convertView;
    }
}
