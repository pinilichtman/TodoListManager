package il.ac.huji.todolist;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {
	private EditText edtNewItem;
	private Button btnOk;
	private Button btnCancel;
	private DatePicker datePicker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		edtNewItem = (EditText) findViewById(R.id.edtNewItem);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String itm = edtNewItem.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("title", itm);
				intent.putExtra("dueDate", datePicker.getCalendarView().getDate() );
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
		return true;
	}

}
