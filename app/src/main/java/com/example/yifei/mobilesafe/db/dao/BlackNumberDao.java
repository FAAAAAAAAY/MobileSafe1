package com.example.yifei.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.example.yifei.mobilesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yifei on 2015/8/11.
 */
public class BlackNumberDao {
    private final BlackNumberOpenHelper helper;

    public BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }

    public boolean add(String number, String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number",number);
        contentValues.put("mode", mode);
        long rowId = db.insert("blacknumber", null, contentValues);
        if (rowId == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean delete(String number){
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete("blacknumber", "number=?", new String[]{number});
        if (rowNumber == 0){
            return false;
        }else{
            return true;
        }
    }

    public boolean change(String number,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode",mode);
        int rowNumber = db.update("blacknumber", contentValues, "number=?", new String[]{number});
        if (rowNumber == 0){
            return false;
        }else {
            return true;
        }
    }

    /*
    * 返回一个电话号码的拦截模式
    * */
    public String findNumber(String number){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"},
                "number=?", new String[]{number}, null, null, null);
        String mode = "";
        if (cursor.moveToNext()){
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    public List<BlackNumberInfo> findAll(){
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null,null);
        while (cursor.moveToNext()){
            BlackNumberInfo info = new BlackNumberInfo();
            info.setMode(cursor.getString(1));
            info.setNumber(cursor.getString(0));
            list.add(info);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(3000);
        return list;
    }

    public List<BlackNumberInfo> findPar(int pageNumber, int pageSize){
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?",
                new String[]{String.valueOf(pageSize), String.valueOf(pageNumber * pageSize)});
        while (cursor.moveToNext()){
            BlackNumberInfo info = new BlackNumberInfo();
            info.setMode(cursor.getString(1));
            info.setNumber(cursor.getString(0));
            list.add(info);
        }
        cursor.close();
        db.close();

        return list;
    }

    public List<BlackNumberInfo> findPar2(int startIndex, int maxCount){
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?",
                new String[]{String.valueOf(maxCount), String.valueOf(startIndex)});
        while (cursor.moveToNext()){
            BlackNumberInfo info = new BlackNumberInfo();
            info.setMode(cursor.getString(1));
            info.setNumber(cursor.getString(0));
            list.add(info);
        }
        cursor.close();
        db.close();

        return list;
    }


    public int getTotal(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;

    }
}
