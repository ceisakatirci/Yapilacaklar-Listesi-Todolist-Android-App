package fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import com.isakatirci.yapilacaklarlistesi.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        //("YYYY-MM-DD HH:MM:SS.SSS").
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0,hourOfDay,minute);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String saat = format.format(calendar.getTime());

        editText = (EditText) getActivity().findViewById(R.id.editTextSonaErmeSaati);
        editText.setText(saat);
    }
}
