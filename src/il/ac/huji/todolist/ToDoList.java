package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ToDoList extends Activity {
	MyAdapter adp;
	ArrayList<String> list;
	ListView lstTodoItems ;
	EditText edtNewItem;
	Menu menuItemAdd;
	ContextMenu menuItemDelete;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_list);
		list = new ArrayList<String>();
		adp = new MyAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,list);

		lstTodoItems = (ListView) findViewById(R.id.lstTodoItems);
		lstTodoItems.setAdapter(adp);

		edtNewItem = (EditText) findViewById(R.id.edtNewItem);
		menuItemAdd = (Menu) findViewById(R.id.add);
		menuItemDelete = (ContextMenu) findViewById(R.id.del);

		registerForContextMenu(lstTodoItems);



		//		lstTodoItems.setOnItemLongClickListener(new OnItemLongClickListener() {
		//			@Override
		//			public boolean onItemLongClick(AdapterView<?> parent, View view,
		//					int position, long id) {
		//				AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
		//				builder.setMessage("do you wont");//.show();
		//				// TODO Auto-generated method stub
		//				return true;
		//			}
		//		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu , View v, ContextMenuInfo info){
		super.onCreateContextMenu(menu, v, info);
		AdapterContextMenuInfo info2 = (AdapterContextMenuInfo) info;
		menu.setHeaderTitle(list.get(info2.position));
		getMenuInflater().inflate(R.menu.ctxmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		list.remove(info.position);
		adp.notifyDataSetChanged();
		return true;

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.add){
			if(edtNewItem.getText().toString().length()==0){
				return false;
			}
			list.add(edtNewItem.getText().toString());
			adp.notifyDataSetChanged();
			edtNewItem.getText().clear();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}


	public class MyAdapter extends ArrayAdapter<String>{
		private List<String> objects;

		public MyAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
			this.objects=objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v=convertView;
			if(v == null){ 
				return super.getView(position, convertView, parent);
			}
			TextView t = (TextView) v.findViewById(android.R.id.text1);
			
			if(position%2==0)
			{
				t.setTextColor(Color.RED);
				t.setText(objects.get(position));
			}
			else
			{
				t.setTextColor(Color.BLUE);
				t.setText(objects.get(position));
			}
			return v;
		}


	}

}
