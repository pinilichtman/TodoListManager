package il.ac.huji.todolist;
import java.util.Date;



public class Task {
	final static long DAY = 86400000;
	private String task;
	private Date date;
	private long dbId;
	
	public Task(String task ,Date date) {
		this.date = new Date(date.getTime()-date.getTime()%DAY);
		this.task = task;
		this.dbId = -1;
	}
	
	public Task(String task ,Date date , long dbId) {
		this.date = new Date(date.getTime()-date.getTime()%DAY);
		this.task = task;
		this.dbId = dbId;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setDbId(long dbId){
		this.dbId = dbId;
	}
	
	public long getDbId() {
		return dbId;
	}
}
