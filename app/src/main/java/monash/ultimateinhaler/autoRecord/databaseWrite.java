package monash.ultimateinhaler.autoRecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by magician-harry on 2016/9/19.
 */
public class databaseWrite extends SQLiteOpenHelper {

    private static final String TableName = "RAWINFO";
    private static final String Column1_PK = "date_pk";
    private static final String Column2_weather = "WESTHER";
    private static final String Column3_pollen = "POLLEN";
    SQLiteDatabase db = null;

    public databaseWrite(Context context) {
        super(context, "rawInfo.db", null, 1);
        db = getWritableDatabase();
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RAWINFO_TABLE = "CREATE TABLE " + TableName +
                "(" +
                Column1_PK + " TEXT PRIMARY KEY," +
                Column2_weather + " BLOB," +
                Column3_pollen + " BLOB" +
                ")";

        try {
            db.execSQL(CREATE_RAWINFO_TABLE);
        } catch (Exception e) {
            Log.i("db issue", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            this.onCreate(db);
        } catch (Exception e) {
            Log.i("db issue", e.getMessage());
        }
    }

    public Boolean insertData(String Weather, String Pollen) {
        ContentValues singleEntry = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        singleEntry.put("date_pk", dateFormat.format(new Date()));
        singleEntry.put("WESTHER", Weather);
        singleEntry.put("POLLEN", Pollen);
        long resultNum = db.insert("RAWINFO", null, singleEntry);
        return resultNum != -1;
    }

    public Cursor getData(Date requestDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Cursor res = db.rawQuery("select * from " + TableName + " where " + Column1_PK + " = " + dateFormat.format(requestDate), null);
        return res;
    }


    public String getDateBasePath() {
        return db.getPath();
    }
}
