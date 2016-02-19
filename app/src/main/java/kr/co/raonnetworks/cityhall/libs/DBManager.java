package kr.co.raonnetworks.cityhall.libs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.raonnetworks.cityhall.LoginActivity;
import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJongRak on 2015. 11. 27..
 */
public class DBManager {

    public static final int CONNECTION_TIME_OUT = 3000;

    public static void addEdu(Context context, EducationModel educationModel) {
        if (!educationModel.checkSum()) {
            throw new CityHallDBException();
        }

        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = makeQuery("INSERT INTO EDUCATION (EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE) VALUES(?,?,?,?,?,?,?,?)", educationModel.toObjectArray());
        db.execSQL(query);

        Log.d("test", Arrays.toString(getEdu(context)));

    }

    public static EducationModel[] getEdu(Context context) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = "SELECT EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE FROM EDU";

        Cursor cursor = db.rawQuery(query, null);

        List<EducationModel> modelList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                EducationModel mEducationModelTmp = new EducationModel();
                mEducationModelTmp.setEduId(cursor.getString(cursor.getColumnIndex("EDU_ID")));
                mEducationModelTmp.setEduName(cursor.getString(cursor.getColumnIndex("EDU_NAME")));
                mEducationModelTmp.setEduLocation(cursor.getString(cursor.getColumnIndex("EDU_LOCATION")));
                mEducationModelTmp.setEduPart(cursor.getString(cursor.getColumnIndex("EDU_PART")));
                mEducationModelTmp.setEduStart(cursor.getLong(cursor.getColumnIndex("EDU_START")));
                mEducationModelTmp.setEduEnd(cursor.getLong(cursor.getColumnIndex("EDU_END")));
                mEducationModelTmp.setEduTarget(cursor.getInt(cursor.getColumnIndex("EDU_TARGET")));
                mEducationModelTmp.setEduType(cursor.getString(cursor.getColumnIndex("EDU_TYPE")));
                modelList.add(mEducationModelTmp);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return (EducationModel[]) modelList.toArray();
    }


    private static String makeQuery(String query, Object[] datas) throws CityHallDBException {
        for (Object data : datas) {
            if (data instanceof String) {
                query = query.replaceFirst("\\?", "'" + data + "'");
            } else if (data instanceof Number) {
                query = query.replaceFirst("\\?", data.toString());
            } else if (data instanceof Date) {
                query = query.replaceFirst("\\?", Long.toString(((Date) data).getTime()));
            } else {
                throw new CityHallDBException();
            }
        }
        return query;
    }

    public static class CityHallDBException extends SQLiteException {
        public CityHallDBException() {
            super();
            System.err.println("쿼리를 확인하세요.");
        }
    }
}
