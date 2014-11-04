package com.belanger.simon.foodle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.models.FUserInfo;

/**
 * Created by SimonPro on 14-10-31.
 */
public class FSelectedContactListViewAdapter extends BaseAdapter implements ListAdapter {

    private final FList<FUserInfo> contactList;
    private final LayoutInflater inflater;

    public FSelectedContactListViewAdapter(Context context, FList<FUserInfo> contactList) {
        this.contactList = contactList;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FUserInfo userInfo = contactList.get(position);
        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.f_view_selected_contact_listitem, parent, false);
            holder = Holder.fromView(convertView);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        updateView(position, userInfo, holder);

        return convertView;
    }

    private static class Holder {

        TextView contactName;

        public static Holder fromView(View view) {
            Holder holder = new Holder();

            holder.contactName = (TextView) view.findViewById(R.id.contactName);

            return holder;
        }
    }

    // TODO: only update visible views
    private void updateView(final int position, final FUserInfo userInfo, final Holder holder) {

        if(userInfo == null)
            return;

        holder.contactName.setText(userInfo.firstName + " " +
                userInfo.lastName);
    }
}
