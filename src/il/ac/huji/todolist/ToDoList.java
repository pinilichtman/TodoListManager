package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;



public class ToDoList extends Activity {
	private static final int ADD = 0;
	
	private MyAdapter adp;
	private ArrayList<Task> list;
	private ListView lstTodoItems ;
	private SQLiteDatabase todo_db;
	private Cursor cursor;
	private DBHelper dbh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("sycle", "in on create");

		super.onCreate(savedInstanceState);
		dbh = new DBHelper(getApplicationContext());
		todo_db = dbh.getWritableDatabase();
		setContentView(R.layout.activity_to_do_list);
		list = new ArrayList<Task>();
		adp = new MyAdapter(getApplicationContext(), R.layout.row, list);
		lstTodoItems = (ListView) findViewById(R.id.lstTodoItems);
		lstTodoItems.setAdapter(adp);		
		registerForContextMenu(lstTodoItems);
		Thread thread =new Thread(new Runnable() {			
			@Override
			public void run() {
				Parse.initialize(getApplicationContext(),
						"vl49h5ZqCb91wKCg9Ktzh3FFQc4AWQoKGmPkCKTh", 
						"ZdXX7yZa2CQpcc0LYQ27hIZ4cxmPcC2nUQYKQBoh");
				ParseUser.enableAutomaticUser();
				
				cursor = todo_db.query("todo", new String[]{"_id","title","due"},null, null,null, null, null);
				if(cursor.moveToFirst()){
					do{
						list.add(new Task( cursor.getString(1) ,new Date( cursor.getLong(2)),cursor.getLong(0)));
					}while(cursor.moveToNext());
				}
	
			}
			
		});
		thread.start();		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK){
			String s;
			switch (requestCode){
			case ADD:
				s = data.getStringExtra("title");	
				long d = data.getLongExtra("dueDate",-1);
				addNewItem(new Task(s, new Date(d)));				
				break;
			default: 
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
		else
			super.onActivityResult(requestCode, resultCode, data);


	}

	private void addNewItem(Task task){
	
		ContentValues todoRow = new ContentValues();
		todoRow.put(DBHelper.TITLE_COLUMN_NAME, task.getTask());
		todoRow.put(DBHelper.DUE_COLUMN_NAME, task.getDate().getTime());
		long id =todo_db.insert(DBHelper.TABLE_NAME, null, todoRow);
		if(id ==-1){
			Log.e("db","error inserting to bd");
			System.exit(3);
		}
		
		task.setDbId(id);
		list.add(task);
		adp.notifyDataSetChanged();
		
		ParseObject parseObj = new ParseObject("todo");
		parseObj.put("db_id", task.getDbId());
		parseObj.put("title",task.getTask() );
		parseObj.put("due", task.getDate());
		parseObj.saveInBackground();
		
		
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu , View v, ContextMenuInfo info){
		//super.onCreateContextMenu(menu, v, info);
		AdapterContextMenuInfo info2 = (AdapterContextMenuInfo) info;
		menu.setHeaderTitle(list.get(info2.position).getTask());
		String str = list.get(info2.position).getTask();
		menu.add(0, R.id.menuItemDelete, 0,R.string.delete_item );
		if(isPhoneNum(str)){
			menu.add(0, R.id.menuItemCall, 1, str);
		}
		getMenuInflater().inflate(R.menu.ctxmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item){		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch(item.getItemId()){
		case R.id.menuItemDelete:
			removeItem(	list.get(info.position));
			break;
		case R.id.menuItemCall:
			call(list.get(info.position).getTask());
			break;
		}
		return true;
	}

	private void removeItem(Task task ) {
		todo_db.delete(DBHelper.TABLE_NAME, DBHelper.ID_COLUMN_NAME+"  == "+task.getDbId(),null);
		list.remove(task);
		adp.notifyDataSetChanged();
		
		ParseQuery<ParseObject> prsq = new ParseQuery<ParseObject>("todo");
		prsq.whereEqualTo("db_id", task.getDbId());
		prsq.getFirstInBackground(new GetCallback<ParseObject>() {			
			@Override
			public void done(ParseObject object, ParseException e) {
				object.deleteInBackground();
			}
		});

	}

	private void call(String tel) {
		tel = tel.replace("call", "");
		tel=tel.trim();
		tel="tel:"+tel;
		Intent dial = new Intent(Intent.ACTION_DIAL	, Uri.parse(tel)); 
		startActivity(dial);

	}


	private boolean isPhoneNum(String str) {
		String str1 = str.toLowerCase(Locale.ENGLISH).trim();
		if(str1.startsWith("call ")){
			return true;
		}
		return false;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()== R.id.add){
			Intent intent = new Intent(getApplicationContext(), AddNewTodoItemActivity.class);
			startActivityForResult(intent, ADD);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public class MyAdapter extends ArrayAdapter<Task>{

		private List<Task> objects;
		private Context context;
		private int resource;

		public MyAdapter(Context context, int resource, List<Task> objects) {
			super(context, resource, objects);
			this.objects=objects;
			this.context =context;
			this.resource = resource;
		}				

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			Task entry = objects.get(position);
			TextView txtTodoTitle,txtTodoDueDate;
			if (convertView == null) {
				if(context!=null){
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(resource, viewGroup, false);
				}
				txtTodoTitle = (TextView)convertView.findViewById(R.id.txtTodoTitle);
				txtTodoDueDate = (TextView) convertView.findViewById(R.id.txtTodoDueDate);
				convertView.setTag(R.id.txtTodoTitle,txtTodoTitle);
				convertView.setTag(R.id.txtTodoDueDate,txtTodoDueDate);

			}
			else{
				txtTodoTitle= (TextView) convertView.getTag(R.id.txtTodoTitle);
				txtTodoDueDate= (TextView) convertView.getTag(R.id.txtTodoDueDate);
			}
			if(txtTodoTitle!=null && txtTodoDueDate!= null && entry != null){
				txtTodoTitle.setText(entry.getTask());
				java.text.DateFormat sdf= DateFormat.getDateFormat(getApplicationContext());
				Date d = entry.getDate();
				txtTodoDueDate.setText( sdf.format(d)); 

				//set color
				Date tmp = new Date();
				Date today = new Date(tmp.getTime()-tmp.getTime()%Task.DAY) ;
				if(d.compareTo(today)<0){
					txtTodoTitle.setTextColor(Color.RED);
					txtTodoDueDate.setTextColor(Color.RED);

				}else{
					txtTodoTitle.setTextColor(Color.BLUE);
					txtTodoDueDate.setTextColor(Color.BLUE);
				}

			}
			return convertView;
		}

	}
}
