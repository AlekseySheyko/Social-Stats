package aleksey.sheyko.staticdemo.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.models.Account;

public class AccountAdapter extends ArrayAdapter<Account> {

    // Declaring our ArrayList of items
    private ArrayList<Account> mObjects;

    public AccountAdapter(Context context, int textViewResourceId, ArrayList<Account> objects) {
        super(context, textViewResourceId, objects);
        mObjects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Assign the view we are converting to a local variable
        View view = convertView;

        // Check to see if the view is null
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.account_list_item, parent, false);
        }
        // Recall that the variable position is sent in as an argument to this method.
        Account account = mObjects.get(position);

        if (account != null) {
            TextView nameTextView = (TextView) view.findViewById(R.id.username);
            TextView labelTextView = (TextView) view.findViewById(R.id.label);
            TextView valueTextView = (TextView) view.findViewById(R.id.value);

            nameTextView.setText(account.getUsername());
            labelTextView.setText(account.getStatsList().get(0).getLabel());
            valueTextView.setText(account.getStatsList().get(0).getValue() + "");
        }

        return view;
    }
}
