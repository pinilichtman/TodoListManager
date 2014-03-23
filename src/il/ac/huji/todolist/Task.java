package il.ac.huji.todolist;
import java.util.Date;



public class Task {
	final static long DAY = 86400000;
	String task;
	Date date;
	
	public Task(String task ,Date date) {
		this.date = new Date(date.getTime()-date.getTime()%DAY);
		this.task = task;
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
}
