package androidsamples.java.journalapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.NavArgs;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryDetailsFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryDetailsFragment extends Fragment {
  private EditText mEditTitle;
  private Button mBtnDate, mBtnStart, mBtnEnd;
  private EntryDetailsViewModel mEntryDetailsViewModel;
  private JournalEntry mEntry;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    mEntryDetailsViewModel = new ViewModelProvider(getActivity()).get(EntryDetailsViewModel.class);

    UUID mEntryId = UUID.fromString(EntryDetailsFragmentArgs.fromBundle(getArguments()).getEntryId());
    mEntryDetailsViewModel.loadEntry(mEntryId);
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_entry_details, container, false);
    mEditTitle = view.findViewById(R.id.edit_title);
    mBtnDate = view.findViewById(R.id.btn_entry_date);
    mBtnStart = view.findViewById(R.id.btn_start_time);
    mBtnEnd = view.findViewById(R.id.btn_end_time);
    view.findViewById(R.id.btn_save).setOnClickListener(this::saveEntry);
    view.findViewById(R.id.btn_entry_date).setOnClickListener(this::launchDateDialog);
    view.findViewById(R.id.btn_start_time).setOnClickListener(this::launchStartTimerDialog);
    view.findViewById(R.id.btn_end_time).setOnClickListener(this::launchEndTimerDialog);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mEntryDetailsViewModel.getEntryLiveData().observe(getActivity(),
            entry -> {
              this.mEntry = entry;
              if (entry != null) updateUI();
            });
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_entry_details, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_delete_entry) {
      new AlertDialog.Builder(getContext())
              .setTitle("Delete Entry")
              .setMessage("Are you sure?")
              .setPositiveButton(R.string.yes, (dialog, id) -> {
                mEntryDetailsViewModel.deleteEntry(mEntry);
                getActivity().onBackPressed();
              })
              .setNegativeButton(R.string.cancel, null).show();
    } else if (item.getItemId() == R.id.menu_share_entry) {
      Intent sendIntent = new Intent();
      sendIntent.setAction(Intent.ACTION_SEND);
      String message = "Look what I have been up to: "+ mEntry.title() + " on " +
              mEntry.getCreatedDate() + ", " + mEntry.getStartTime() + " to " + mEntry.getEndTime();
      sendIntent.putExtra(Intent.EXTRA_TEXT, message);
      sendIntent.setType("text/plain");

      Intent shareIntent = Intent.createChooser(sendIntent, null);
      startActivity(shareIntent);
    }
    return super.onOptionsItemSelected(item);
  }

  private void updateUI() {
    mEditTitle.setText(mEntry.title().isEmpty()?null:mEntry.title());
    mBtnDate.setText(mEntry.getCreatedDate().isEmpty()?"Date":mEntry.getCreatedDate());
    mBtnStart.setText(mEntry.getStartTime().isEmpty()?"Start Time":mEntry.getStartTime());
    mBtnEnd.setText(mEntry.getEndTime().isEmpty()?"End Time":mEntry.getEndTime());
  }

  private void saveEntry(View v) {
    mEntry.setTitle(mEditTitle.getText().toString());
    mEntry.setCreatedDate(mBtnDate.getText().toString());
    mEntry.setStartTime(mBtnStart.getText().toString());
    mEntry.setEndTime(mBtnEnd.getText().toString());
    mEntryDetailsViewModel.saveEntry(mEntry);

    getActivity().onBackPressed();
  }

  private void launchDateDialog(View view) {
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog dialog =
            new DatePickerDialog(getContext(),
                    R.style.ThemeOverlay_AppCompat_Dialog_Alert,
                    this::mDateSetListener, year, month, day);
    dialog.show();
  }

  private void launchStartTimerDialog(View view) {
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    TimePickerDialog dialog =
            new TimePickerDialog(getContext(),
                    this::mStartTimeListener,
                    hour, minute, false);
    dialog.show();
  }

  private String formatTime(int hour, int minute) {
    String time = "";
    if(hour<10)time = time.concat("0");
    time = time.concat(Integer.toString(hour));
    time = time.concat(":");
    if(minute<10)time = time.concat("0");
    time = time.concat(Integer.toString(minute));
    return time;
  }

  private void mStartTimeListener(TimePicker timePicker, int hour, int minute) {
    mBtnStart.setText(formatTime(hour, minute));
  }

  private void launchEndTimerDialog(View view) {
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    TimePickerDialog dialog =
            new TimePickerDialog(getContext(),
                    this::mEndTimeListener,
                    hour, minute, false);
    dialog.show();
  }

  private void mEndTimeListener(TimePicker timePicker, int hour, int minute) {
    mBtnEnd.setText(formatTime(hour, minute));
  }

  public void mDateSetListener(DatePicker view, int year, int month, int dayOfMonth) {
    Calendar mCalendar = Calendar.getInstance();
    mCalendar.set(Calendar.YEAR, year);
    mCalendar.set(Calendar.MONTH, month);
    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    Date d = new Date();
    d.setTime(mCalendar.getTimeInMillis());
    SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd, yyyy");
    String dateToDisplay = formatter.format(d);
    mBtnDate.setText(dateToDisplay);
  }
}