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
    private final List<Account> accounts;
    private final LayoutInflater inflater;
    private final boolean smallItems;

    public AccountAdapter(Context context, List<Account> accounts, boolean smallItems) {
        this.accounts = accounts;
        this.smallItems = smallItems;
        inflater = LayoutInflater.from(context);
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
        int itemView = smallItems ? R.layout.view_item_cloud_account_small : R.layout.view_item_cloud_account;
        convertView = inflater.inflate(itemView, parent, false);
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
