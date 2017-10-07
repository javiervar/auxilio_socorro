package com.example.kira.auxiliosocorro;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by jose_ on 07/10/2017.
 */

public class FragmentContactos extends AppCompatActivity{
    public SimpleCursorAdapter adapter;

@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
        setupCursorAdapter();
        ListView lvContacts =(ListView)findViewById(R.id.dialoglist);
        lvContacts.setAdapter(adapter);
    }
private void setupCursorAdapter(){
    String[] uiBindFrom = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};
    int[] uiBindTo={R.id.list_item, };
    adapter = new SimpleCursorAdapter(
            this, R.layout.dialog_contactos,
            null, uiBindFrom, uiBindTo,
            0);
}
}
