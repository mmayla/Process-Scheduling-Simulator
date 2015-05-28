package mini_pack;

public class Event
{
	public static enum eventtype
	{
		arrived,
		scheduled,
		terminated,
		preempted,
		context;
		
	}
	
	public int pID;
	public float time;
	public eventtype type;
	
	public Event(int id,float t,eventtype et)
	{
		pID = id;
		time = t;
		type = et;
	}
	
}
