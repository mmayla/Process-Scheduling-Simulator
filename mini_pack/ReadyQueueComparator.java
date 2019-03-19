package mini_pack;

import app_pack.*;
import java.util.Comparator;

public class ReadyQueueComparator implements Comparator<mini_pack.Process>
{
	// privates
	private queueType type;

	// algorithm comparing methods
	private int comparing_FCFS(mini_pack.Process p1, mini_pack.Process p2)
	{
		if (p1.arrivaltime < p2.arrivaltime)
			return -1;
		if (p1.arrivaltime > p2.arrivaltime)
			return 1;
		return 0;
	}

	private int comparing_SJF(mini_pack.Process p1, mini_pack.Process p2)
	{
		if (p1.expectedruntime < p2.expectedruntime)
			return -1;
		if (p1.expectedruntime > p2.expectedruntime)
			return 1;
		return 0;
	}
	
	private int comparing_HPFSN(mini_pack.Process p1, mini_pack.Process p2)
	{
		if(p1.priority > p2.priority)
			return -1;
		if(p1.priority < p2.priority)
			return 1;
		return 0;
	}
	
	private int comparing_HPFSP(mini_pack.Process p1, mini_pack.Process p2)
	{
		if(p1.priority > p2.priority)
			return -1;
		if(p1.priority < p2.priority)
			return 1;
		return 0;
	}
	
	private int comparing_HPFD(mini_pack.Process p1, mini_pack.Process p2)
	{
		if(p1.priority > p2.priority)
			return -1;
		if(p1.priority < p2.priority)
			return 1;
		return 0;
	}
	
	private int comparing_WRR(mini_pack.Process p1, mini_pack.Process p2)
	{
		float p1weight = p1.arrivaltime + p1.expectedruntime + p1.priority;
		float p2weight = p2.arrivaltime + p2.expectedruntime + p2.priority;
		
		if(p1weight > p2weight)
			return -1;
		if(p1weight < p2weight)
			return 1;
		return 0;
	}
	
	private int comparing_background(mini_pack.Process p1, mini_pack.Process p2)
	{
		if (p1.arrivaltime < p2.arrivaltime)
			return -1;
		if (p1.arrivaltime > p2.arrivaltime)
			return 1;
		return 0;
	}
	
	public static enum queueType
	{
		background, 
		FCFS,
		SJF,
		RR,
		HPFSN,
		HPFSP,
		HPFD,
		WRR;
	}

	public ReadyQueueComparator(queueType qt)
	{
		super();
		type = qt;
	}

	public void setType(queueType qt)
	{
		type = qt;
	}

	@Override
	public int compare(mini_pack.Process p1, mini_pack.Process p2)
	{
		switch (type)
		{
		case FCFS:
			return comparing_FCFS(p1, p2);

		case SJF:
			return comparing_SJF(p1, p2);
			
		case HPFSN:
			return comparing_HPFSN(p1, p2);
			
		case HPFSP:
			return comparing_HPFSP(p1, p2);
			
		case WRR:
			return comparing_WRR(p1, p2);
		
		case HPFD:
			return comparing_HPFD(p1, p2);
		
		case background:
			return comparing_background(p1, p2);
		}

		return 0;
	}
}
