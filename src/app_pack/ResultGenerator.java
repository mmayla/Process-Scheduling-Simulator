package app_pack;

import java.util.*;

import exception_error_pack.PSSexception;
import mini_pack.*;
import mini_pack.Event.eventtype;
import mini_pack.Process;

public class ResultGenerator
{
	private Vector<Process> processtlist;
	private Vector<Event> eventlist;

	float Sum_Time_around = 0;
	float Sum_Weighted_around = 0;
	float Avg_Time_around = 0;
	float Avg_Weighted_around = 0;
	double Standared = 0;
	
	//TODO utilization

	public void Result() throws PSSexception
	{
		int d = processtlist.size();
		for (int i = 0; i < d; i++)
		{
			processtlist.elementAt(i).Time_around = processtlist.elementAt(i).FinishTime
					- processtlist.elementAt(i).arrivaltime;
			processtlist.elementAt(i).Weighted_around = processtlist
					.elementAt(i).Time_around
					/ processtlist.elementAt(i).expectedruntime;
			Sum_Time_around += processtlist.elementAt(i).Time_around;
			Sum_Weighted_around += processtlist.elementAt(i).Weighted_around;
		}

		Avg_Time_around = Sum_Time_around / d;
		Avg_Weighted_around = Sum_Weighted_around / d;

		double a = 0;
		float Sum = 0;
		for (int i = 0; i < d; i++)
		{
			a = processtlist.elementAt(i).Weighted_around - Avg_Weighted_around;
			a = Math.pow(a, 2);
			Sum += a;
		}

		Standared = Math.sqrt(Sum / d);

	}
	
	public void setLists(Vector<Process> pl,Vector<Event> vl)
	{
		processtlist = pl;
		eventlist = vl;
	}
	
	public String resultString()
	{
		String results = "# process_id, arrival_time, finish_time, run_time, priority, " +
				"turnaround_time, weighted_turnaround_time\n";
		//for each process
		for(int i=0;i<processtlist.size();i++)
		{
			results += processtlist.elementAt(i).toString()+"\n";
		}
		
		results += "\n# performance evaluation metrics\n";
		
		results += "Average turnaround time = "+Avg_Time_around+"\n";
		results += "Average waited turnaround time = "+Avg_Weighted_around+"\n";
		results += "Standard diviation = "+Standared+"\n";
		return results;
	}
}
