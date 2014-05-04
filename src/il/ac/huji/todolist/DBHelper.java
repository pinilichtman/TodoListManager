package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{ 

	static final String TITLE_COLUMN_NAME = "title";
	static final String TABLE_NAME = "todo";
	static final String DUE_COLUMN_NAME = "due";
	public static final String ID_COLUMN_NAME = "_id";

	
	public DBHelper(Context context) { 
			super(context, "todo_db", null, 1); 
	} 
	
	public void onCreate(SQLiteDatabase db) { 
		db.execSQL("create table "+TABLE_NAME+" ( "+
				" _id integer primary key autoincrement , " +
				TITLE_COLUMN_NAME+" text, " + 
				DUE_COLUMN_NAME+" long );"); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.d("onupgrade", "up ");
		
	} 

	
} 