package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yifei.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends Activity {

    private ListView lvList;
    private ArrayList<HashMap<String, String>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        list = new ArrayList<HashMap<String, String>>();
        lvList = (ListView) findViewById(R.id.lv_list);
        readContact();


        /*for (int i=0;i<list.size();i++){
            System.out.println(list.get(i).get("name") +": "+ list.get(i).get("phone"));
        }*/

        lvList.setAdapter(new ContacrAdapter());
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = list.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    private void readContact() {

        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        Cursor rawContactsCuror = getContentResolver().query(rawContactsUri, new String[]{"contact_id"}, null, null, null);

        if (rawContactsCuror != null) {
            while (rawContactsCuror.moveToNext()) {
                String contactId = rawContactsCuror.getString(0);
                Cursor dataCursor = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?",
                        new String[]{contactId}, null);

                if (dataCursor != null) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        //System.out.println(contactId + ", "+data1 + ", "+mimetype);
                        if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                            map.put("phone", data1);
                        } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                            map.put("name", data1);
                        }
                    }

                    list.add(map);

                }

                dataCursor.close();

            }
        }
        rawContactsCuror.close();

    }

    class ContacrAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ContactActivity.this,R.layout.contact_item,null);
            TextView tvName = (TextView) view.findViewById(R.id.name);
            TextView tvPhone = (TextView) view.findViewById(R.id.phone);

            String name = list.get(position).get("name");
            String phone = list.get(position).get("phone");

            tvPhone.setText(phone);
            tvName.setText(name);

            return view;
        }
    }

}
