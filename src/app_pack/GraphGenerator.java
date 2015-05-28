package app_pack;

import gui.LineChart;

import java.util.Comparator;
import java.util.Vector;
import java.util.Collections;

import org.jfree.ui.RefineryUtilities;

import mini_pack.Event;

import mini_pack.Process;
import mini_pack.Event.eventtype;

public class GraphGenerator
{
	//Data member
	private Vector<Vector<Event>> eventlist;
	private Vector<Process> processlist;
	
	//Methods
	public GraphGenerator()
	{
		
	}
	
	public void setEventlist(Vector<Vector<Event>> eventlist)
	{
		this.eventlist = new Vector<Vector<Event>>(eventlist.size());
		
//		Comparator<Event> listcomparator = new Comparator<Event>()
//		{
//			@Override
//			public int compare(Event e1, Event e2)
//			{
//				if(e1.pID < e2.pID)
//					return -1;
//				if(e1.pID > e2.pID)
//					return 1;
//				return 0;
//			}
//		};
		
		for(int i=0;i<eventlist.size();i++)
		{
			this.eventlist.add(i, eventlist.elementAt(i));
//			if(this.eventlist.elementAt(i) != null)
//			Collections.sort(this.eventlist.elementAt(i),listcomparator);
		}
	}
	
	
	public void setProcessList(Vector<Process> processlist)
	{
		this.processlist = processlist;
	}
	
	private void setPoints(LineChart demo,int algoindex)
	{
		int pid=0;
		float start=0,finish=0;
		boolean flag=false;
		for(int i=0;i<eventlist.elementAt(algoindex).size();i++)
		{
			if(eventlist.elementAt(algoindex).elementAt(i).type == eventtype.scheduled)
			{
				pid = eventlist.elementAt(algoindex).elementAt(i).pID;
				start = eventlist.elementAt(algoindex).elementAt(i).time;
			}
			
			if((eventlist.elementAt(algoindex).elementAt(i).type == eventtype.preempted)||
				(eventlist.elementAt(algoindex).elementAt(i).type == eventtype.terminated))
			{
				finish = eventlist.elementAt(algoindex).elementAt(i).time;
				demo.addLineStep(pid, start, finish);
			}
		}
	}
	
	public void drawGraph(int index)
	{
		LineChart demo = new LineChart("PSS Simulator");
		demo.algoname = PSSManager.getAlgoName(index);
		demo.setLabels(processlist);
		setPoints(demo,index);
		
		demo.drawnShow();
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		
	}

}
