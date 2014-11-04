package com.belanger.simon.foodle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.models.FUserInfo;

/**
 * Created by SimonPro on 14-11-03.
 */
public class FContactListViewAdapter extends BaseAdapter implements ListAdapter{

    private final FList<FUserInfo> contactList;
    private final LayoutInflater inflater;
    private OnAddContact listener;

    public FContactListViewAdapter(Context context, FList<FUserInfo> contactList) {
        this.contactList = contactList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final FUserInfo contact = contactList.get(position);
        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.f_view_contact_listitem, parent, false);
            holder = Holder.fromView(convertView);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        updateView(position, contact, holder);

        return convertView;
    }

    private static class Holder {

        TextView contactName;
        Button addContact;

        public static Holder fromView(View view) {
            Holder holder = new Holder();

            holder.contactName = (TextView) view.findViewById(R.id.contactName);
            holder.addContact = (Button) view.findViewById(R.id.addContactButton);

            return holder;
        }
    }

    // TODO: only update visible views
    private void updateView(final int position, final FUserInfo contact, final Holder holder) {

        if(contact == null)
            return;

        holder.contactName.setText(contact.firstName + " " + contact.lastName);
        holder.addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.addContact(contact);
            }
        });
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

    public void setOnAddContactListener(OnAddContact l){
        this.listener = l;
    }

    public interface OnAddContact{
        public void addContact(FUserInfo contact);
    }
}