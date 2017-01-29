package com.emn.trustydrive;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emn.trustydrive.providers.CloudAccount;
import com.emn.trustydrive.providers.Provider;

import java.util.List;

public class CloudAdapter extends BaseAdapter {

    private final List<CloudAccount> cloudAccounts;
    private final LayoutInflater inflater;

    public CloudAdapter(Context context, List<CloudAccount> cloudAccounts) {
        this.cloudAccounts = cloudAccounts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cloudAccounts.size();
    }

    @Override
    public CloudAccount getItem(int position) {
        return cloudAccounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cloudAccounts.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CloudAccount cloudAccount = getItem(position);
        convertView = inflater.inflate(R.layout.view_item_cloud_account, parent, false);
        TextView emailTextView = (TextView) convertView.findViewById(R.id.emailTextView);
        emailTextView.setText(cloudAccount.getEmail());
        ImageView cloudLogoImageView = (ImageView) convertView.findViewById(R.id.cloudLogo);
        switch (cloudAccount.getProvider()) {
            case DROPBOX:
                cloudLogoImageView.setImageResource(R.drawable.dropbox);
                break;
            case GOOGLE_DRIVE:
                cloudLogoImageView.setImageResource(R.drawable.google_drive);
                break;
            case ONEDRIVE:
                cloudLogoImageView.setImageResource(R.drawable.onedrive);
                break;
            default:
                break;
        }
        if (cloudAccount.getProvider() == Provider.DROPBOX) {
            cloudLogoImageView.setImageResource(R.drawable.dropbox);
        }
        return convertView;
    }

}