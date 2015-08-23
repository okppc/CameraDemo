package com.eebbk.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseManager {
	private static PicDatabase mDatabase;//�õ�һ�����ݿ��ʵ��
	private static SQLiteDatabase mDbWriter;
	private static Cursor mCursor;

	//����һ����д���ݿ�
	public static void getInstance(Context context){
		if(mDatabase ==null){
			mDatabase = new PicDatabase(context);
			mDbWriter = mDatabase.getWritableDatabase();
		}

	}

	//���ݿ�Ĳ������
	public static void insertDB(String table,String nullColumnHack,ContentValues cv){
		mDbWriter.insert(table, nullColumnHack, cv);

	}

	//���ݿ�Ĳ�ѯ����
	public static Cursor queryDB(String table,String[] columns,String selection,String[] selectionArgs,
			String groupBy,String having,String orderBy){
		mCursor = mDbWriter.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return mCursor;

	}

	//���ݿ�ĸ��²���
	public static void updateDB(String table,ContentValues values,String whereClause,String[] whereArgs){
		mDbWriter.update(table, values, whereClause, whereArgs);
	}

	//���ݿ��ɾ������
	public static void deleteDB(String table,String whereClause,String[] whereArgs){
		mDbWriter.delete(table, whereClause, whereArgs);
		
	}
	
	//�ر����ݿ�
	public static void closeDB(){
		mDbWriter.close();
	}

}
