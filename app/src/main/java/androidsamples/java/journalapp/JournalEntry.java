package androidsamples.java.journalapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "journal_table")
public class JournalEntry {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private UUID mUid;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "created_date")
    private String mCreatedDate;

    @ColumnInfo(name = "start_time")
    private String mStartTime;

    @ColumnInfo(name = "end_time")
    private String mEndTime;

    public JournalEntry(@NonNull String title, String createdDate, String startTime, String endTime) {
        mUid = UUID.randomUUID();
        mTitle = title;
        mCreatedDate = createdDate;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    @NonNull
    public UUID getUid() {
        return mUid;
    }

    public void setUid(@NonNull UUID id) {
        mUid = id;
    }

    @NonNull
    public String title() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @NonNull
    public String getCreatedDate() { return mCreatedDate; }

    public void setCreatedDate(String createdDate) { mCreatedDate = createdDate; }

    @NonNull
    public String getStartTime() { return mStartTime; }

    public void setStartTime(String startTime) { mStartTime = startTime; }

    @NonNull
    public String getEndTime() { return mEndTime; }

    public void setEndTime(String endTime) { mEndTime = endTime; }
}
