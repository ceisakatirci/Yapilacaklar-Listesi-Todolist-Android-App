package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.isakatirci.yapilacaklarlistesi.R;

import java.util.List;

import model.Tekrar;

/**
 * Created by isa on 16.12.2016.
 */

public class TekrarArrayAdapter extends ArrayAdapter<Tekrar> {
    private Context context;
    private List<Tekrar> tekrarListesi;

    public TekrarArrayAdapter(Context context, int resource, List<Tekrar> tekrarListesi) {
        super(context, resource, tekrarListesi);
        this.context = context;
        this.tekrarListesi = tekrarListesi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tekrar tekrar = tekrarListesi.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        CheckedTextView text1 = (CheckedTextView) view.findViewById(android.R.id.text1);
        text1.setText(tekrar.getTekrarAdi());
        text1.setTextSize(20f);
        text1.setTextColor(Color.parseColor("#404040"));

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Tekrar tekrar = tekrarListesi.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(android.R.layout.simple_spinner_item, null);
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        text1.setText(tekrar.getTekrarAdi());
        text1.setTextColor(Color.parseColor("#404040"));
        text1.setTextSize(20f);
        return view;
    }
}
