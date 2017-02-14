package com.emn.trustydrive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emn.trustydrive.R;
import com.emn.trustydrive.metadata.Account;

import java.util.List;

public class AccountAdapter extends BaseAdapter {
    private List<Account> accounts;
    private final LayoutInflater inflater;

    public AccountAdapter(Context context, List<Account> accounts) {
        this.accounts = accounts;
        inflater = LayoutInflater.from(context);
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Account getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return accounts.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);
        convertView = inflater.inflate(R.layout.view_item_account, parent, false);
        ((TextView) convertView.findViewById(R.id.emailTextView)).setText(account.getEmail());
        ImageView providerLogoImageView = (ImageView) convertView.findViewById(R.id.cloudLogo);
        switch (account.getProvider()) {
            case DROPBOX:
                providerLogoImageView.setImageResource(R.drawable.dropbox);
                break;
            case GOOGLE_DRIVE:
                providerLogoImageView.setImageResource(R.drawable.google_drive);
                break;
            case ONEDRIVE:
                providerLogoImageView.setImageResource(R.drawable.onedrive);
                break;
            default:
                break;
        }
        return convertView;
    }
}
