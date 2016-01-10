package codepathapplication.ruchad.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by ruchadeshmukh on 1/4/16.
 */
public class DatePickerFramgent extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog;
        Bundle bundle = this.getArguments();
        if (bundle.getString("type").equalsIgnoreCase("edit"))
            dialog = new DatePickerDialog(getActivity(), (ToDoEditFragment) (getFragmentManager().findFragmentById(R.id.todo_add_container)), year, month, day);
        else
            dialog = new DatePickerDialog(getActivity(), (ToDoAddFragment) (getFragmentManager().findFragmentById(R.id.todo_add_container)), year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return dialog;
    }

}
