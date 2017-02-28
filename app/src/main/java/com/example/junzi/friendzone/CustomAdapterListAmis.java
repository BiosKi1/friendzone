package com.example.junzi.friendzone;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomAdapterListAmis extends ArrayAdapter<String> {

    private final ListeAmisActivity context;
    private final ArrayList<String> itemname;
    private final ArrayList<String> imgid;
    private final ArrayList<String> id_ami;
    private final ArrayList<String> par;

    public CustomAdapterListAmis(ListeAmisActivity context, ArrayList<String> itemname, ArrayList<String> imgid, ArrayList<String> id_ami, ArrayList<String> par) {
        super(context, R.layout.list_item, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.id_ami= id_ami;
        this.par = par;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        String name = "";
        String tel = "";
        String part = par.get(position).toString();



        LayoutInflater inflater=context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null,true);

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

		Button partage = (Button) rowView.findViewById(R.id.button2);
        Button delete = (Button) rowView.findViewById(R.id.button3);

        if(part.equals("0")){
            partage.setText("Partager");
            part = "1" ;
        }else{
            partage.setText("plus partager");
            part = "0";
        }

        final String partres = part;

		partage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				partager_position(id_ami.get(position),partres);
                context.startActivity(context.getIntent());
			}
		});

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_amis(id_ami.get(position));
                context.startActivity(context.getIntent());
            }
        });

        txtTitle.setText(name);
            /*imageView.setImageURI(Uri.parse(imgid.get(position)));*/
        extratxt.setText(tel);
        return rowView;

    };

    public void partager_position(final String id_amis,final String part){
        class partage_pos extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID_USER,Config.id_user_co);

                hashMap.put(Config.KEY_EMP_ID_AMI,id_amis);

                RequestHandler rh = new RequestHandler();

                String url = Config.ip+"api.php/?fichier=users&action=partage_position" +
                        "&values[id_user]=" +Config.id_user_co+
                        "&values[id_ami]=" +id_amis+
                        "&values[partage_pos]="+part;
                String s = rh.sendPostRequest(url,hashMap);

                return s;
            }
        }
        partage_pos pp = new partage_pos();
        pp.execute();
        String res = "";

        if(part.equals("0")){
           res = "supprimé";
        }else{
            res = "activé";
        }

        Toast.makeText(context, "Partage de la position "+res, Toast.LENGTH_LONG).show();
    }

    public void delete_amis(final String id_amis){
        class delete_ami extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID_USER,Config.id_user_co);

                hashMap.put(Config.KEY_EMP_ID_AMI,id_amis);

                RequestHandler rh = new RequestHandler();

                String url = Config.ip+"api.php/?fichier=users&action=delete_amis" +
                        "&values[id_user]=" +Config.id_user_co+
                        "&values[id_ami]=" +id_amis;
                String s = rh.sendPostRequest(url,hashMap);

                return s;
            }
        }

        delete_ami pp = new delete_ami();
        pp.execute();

        Toast.makeText(context, "Amis supprimé", Toast.LENGTH_LONG).show();
    }

}