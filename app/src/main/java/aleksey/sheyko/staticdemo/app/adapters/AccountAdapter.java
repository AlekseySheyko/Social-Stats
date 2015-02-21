package aleksey.sheyko.staticdemo.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.models.Account;

public class AccountAdapter extends ArrayAdapter<Account> {

    final int INVALID_ID = -1;

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    private Context mContext;
    // Declaring our ArrayList of items
    private ArrayList<Account> mObjects;

    public AccountAdapter(Context context, int textViewResourceId, ArrayList<Account> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mObjects = objects;

        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i).toString(), i);
        }
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

            ImageView iconImageView = (ImageView) view.findViewById(R.id.icon);
            switch (account.getService()) {
                case "Twitter":
                    iconImageView.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.ic_twitter));
                    break;
                case "Instagram":
                    iconImageView.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.ic_instagram));
                    break;
                case "Facebook":
                    iconImageView.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.ic_facebook));
                    break;
                case "Google":
                    iconImageView.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.ic_google));
                    break;
            }
        }

        return view;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        String item = getItem(position).toString();
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
