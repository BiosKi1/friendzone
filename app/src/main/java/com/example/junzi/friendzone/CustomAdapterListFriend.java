package com.example.junzi.friendzone;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomAdapterListFriend extends ArrayAdapter<String> {

    private final ListFriendActivity context;
    private final ArrayList<String> itemname;
    private final ArrayList<String> imgid;

    public CustomAdapterListFriend(ListFriendActivity context, ArrayList<String> itemname, ArrayList<String> imgid) {
        super(context, R.layout.listfrienditem, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        String name = "";
        String tel = "";
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.listfrienditem, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        String mydata = itemname.get(position);
        Pattern pattern = Pattern.compile("(.*?)\\(");
        Matcher matcher = pattern.matcher(mydata);
        if (matcher.find())
        {
             name = matcher.group(1);
        }

        String mydatatel = itemname.get(position);
        Pattern patterntel = Pattern.compile("(\\d)+");
        Matcher matchertel = patterntel.matcher(mydatatel);
        if (matchertel.find())
        {
            tel = matchertel.group();
        }

        tel.replace(" ", "");

        txtTitle.setText(name);
        /*imageView.setImageURI(Uri.parse(imgid.get(position)));*/
        extratxt.setText(tel);
        return rowView;

    };
}