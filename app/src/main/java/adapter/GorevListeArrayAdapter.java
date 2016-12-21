package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.isakatirci.yapilacaklarlistesi.R;

import java.util.List;

import model.GorevListe;
import model.Tekrar;

/**
 * Created by isa on 16.12.2016.
 */

public class GorevListeArrayAdapter extends ArrayAdapter<GorevListe> {
    private Context context;
    private List<GorevListe> gorevListeler;
//http://stackoverflow.com/questions/34023452/flag-activity-new-task-and-flag-activity-clear-task-acts-weird
    public GorevListeArrayAdapter(Context context, int resource, List<GorevListe> gorevListeler) {
        super(context, resource, gorevListeler);
        this.context = context;
        this.gorevListeler = gorevListeler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GorevListe gorevListe = gorevListeler.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        CheckedTextView text1 = (CheckedTextView) view.findViewById(android.R.id.text1);
        text1.setTextSize(20f);
        text1.setTextColor(Color.parseColor("#404040"));
        text1.setText(gorevListe.getGorevListeAdi());
        return view;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        GorevListe gorevListe = gorevListeler.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(android.R.layout.simple_spinner_item, null);
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        text1.setTextSize(20f);
        text1.setTextColor(Color.parseColor("#404040"));
        text1.setText(gorevListe.getGorevListeAdi());
        return view;
    }






}

