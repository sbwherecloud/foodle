package com.belanger.simon.foodle.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.belanger.simon.foodle.FAppState;
import com.belanger.simon.foodle.GcmIntentService;
import com.belanger.simon.foodle.R;
import com.belanger.simon.foodle.adapters.FSelectedContactListViewAdapter;
import com.belanger.simon.foodle.annotations.Layout;
import com.belanger.simon.foodle.annotations.ViewOutlet;
import com.belanger.simon.foodle.datastructures.FList;
import com.belanger.simon.foodle.models.FUserInfo;
import com.belanger.simon.foodle.models.transactions.FContactResponse;
import com.belanger.simon.foodle.network.FCallback;
import com.belanger.simon.foodle.network.FWebService;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SimonPro on 14-10-31.
 */
@Layout(R.layout.f_activity_contacts)
public class FContactsActivity extends FActivity implements GcmIntentService.OnFriendRequestReceivedListener {

    @ViewOutlet(R.id.sendContactRequestButton)
    Button sendRequest;
    @ViewOutlet(R.id.addContactEmail)
    EditText contactEmail;
    @ViewOutlet(R.id.contactsContactList)
    ListView contactListView;

    private FSelectedContactListViewAdapter contactListAdapter;
    private FList<FUserInfo> contactList = new FList<FUserInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.contacts));

        GcmIntentService.setOnFriendRequestReceivedListener(this);

        FWebService.getInstance().getContactList(FAppState.getInstance().getUserInfo().email, new FCallback<FContactResponse>(){
            @Override
            public void success(FContactResponse object, Response response) {
                super.success(object, response);
                contactList.clear();
                contactList.addAll(object.contactList);
                contactListAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FWebService.getInstance().setContactRequest(contactEmail.getText().toString(),
                        FAppState.getInstance().getUserInfo(), new FCallback<FUserInfo>(){
                            @Override
                            public void success(FUserInfo object, Response response) {
                                super.success(object, response);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                super.failure(error);
                            }
                        });
            }
        });

        contactListAdapter = new FSelectedContactListViewAdapter(this, contactList);
        contactListView.setAdapter(contactListAdapter);
    }

    @Override
    public void friendRequestReceived(String askerEmail) {
        String email = FAppState.getInstance().getUserInfo().email;
        FWebService.getInstance().updateContactList(askerEmail, email, true, new FCallback<FContactResponse>(){
            @Override
            public void success(FContactResponse object, Response response) {
                super.success(object, response);
                FAppState.getInstance().getFriendsList().clear();
                FAppState.getInstance().getFriendsList().addAll(object.contactList);
                contactList.clear();
                contactList.addAll(object.contactList);
                contactListAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
            }
        });
    }
}
