package com.example.remindme.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.remindme.db.dao.TaskDao;
import com.example.remindme.db.entity.Task;

import java.util.Calendar;

@Database(entities = {Task.class}, version = 13, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {

    private static TaskRoomDatabase INSTANCE;
    public abstract TaskDao dao();

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TaskDao dao;

        PopulateDbAsync(TaskRoomDatabase db) {
            dao = db.dao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            dao.deleteAll();
            Calendar alarmDateTime = Calendar.getInstance();
            alarmDateTime.setTimeInMillis(System.currentTimeMillis());
            alarmDateTime.set(Calendar.HOUR_OF_DAY, 20);
            alarmDateTime.set(Calendar.MINUTE, 50);
            alarmDateTime.set(Calendar.DATE, 30);
            alarmDateTime.set(Calendar.MONTH, 10);
            alarmDateTime.set(Calendar.YEAR, 2019);
            long timeStampFuture = alarmDateTime.getTimeInMillis();

            alarmDateTime = Calendar.getInstance();
            alarmDateTime.setTimeInMillis(System.currentTimeMillis());
            alarmDateTime.set(Calendar.HOUR_OF_DAY, 20);
            alarmDateTime.set(Calendar.MINUTE, 50);
            alarmDateTime.set(Calendar.DATE, 30);
            alarmDateTime.set(Calendar.MONTH, 8);
            alarmDateTime.set(Calendar.YEAR, 2019);
            long timeStampPast = alarmDateTime.getTimeInMillis();


            Task task = new Task("Read a book",
                    "How about Atomic Habits: An Easy & Proven Way to Build Good Habits & Break Bad Ones by James Clear?"+
                    " or The Count of Monte Cristo by Alexandre Dumas?",
                    Task.BG_COLOR_RED,
                    Task.PERIODICITY_WEEKLY,
                    timeStampFuture);
            dao.insert(task);

            task = new Task("Buy plane tickets",
                    "You should really visit your grandparents",
                    Task.BG_COLOR_GREEN,
                    Task.PERIODICITY_ONE_TIME,
                    timeStampFuture);
            dao.insert(task);

            task = new Task("Go to gym",
                    "Move your ass!",
                    Task.BG_COLOR_ORANGE,
                    Task.PERIODICITY_ONE_TIME,
                    timeStampPast);
            dao.insert(task);

            task = new Task("Clean the house",
                    "Clean it, you filthy animal",
                    Task.BG_COLOR_ORANGE,
                    Task.PERIODICITY_DAILY,
                    timeStampPast);
            dao.insert(task);
            return null;
        }
    }

    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, "task_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
