package codepathapplication.ruchad.todo;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by ruchadeshmukh on 1/4/16.
 */
public class TimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog dialog;
        Bundle args = this.getArguments();
        if (args.getString("type").equalsIgnoreCase("edit"))
            dialog = new TimePickerDialog(getActivity(), (ToDoEditFragment) getFragmentManager().findFragmentById(R.id.todo_add_container), hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        else
            dialog = new TimePickerDialog(getActivity(), (ToDoAddFragment) getFragmentManager().findFragmentById(R.id.todo_add_container), hour, minute,
                    DateFormat.is24HourFormat(getActivity()));

        return dialog;
    }
}
