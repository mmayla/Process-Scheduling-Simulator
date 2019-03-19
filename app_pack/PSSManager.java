package app_pack;

import java.util.Vector;

import exception_error_pack.PSSexception;

import mini_pack.Event;
import mini_pack.Process;
import app_pack.*;

public class PSSManager
{
	// operation classes
	private FileHandlerInput FHI;
	private FileHandlerOutput FHO;
	private ProcessScheduler PS;
	private GraphGenerator GG;
	private ResultGenerator RG;

	// data members
	private Vector<Vector<Event>> eventlist;
	private Vector<Process> processlist;

	// Additional inputs
	private final int NUMBEROFALGORITHMS;
	private float contextswitching;
	private float timequantum;
	private String inputfile;
	private String outputdir;
	public boolean[] run_algorrithms;

	public PSSManager()
	{
		FHI = new FileHandlerInput();
		FHO = new FileHandlerOutput();
		PS = new ProcessScheduler(0);
		GG = new GraphGenerator();
		RG = new ResultGenerator();

		NUMBEROFALGORITHMS = 7;
		// 0-FCFS, 1-SJF, 2-RR, 3-HPFSN, 4-HPFSP, 5-HPFD, 6-WRR
		run_algorrithms = new boolean[NUMBEROFALGORITHMS];
	}

	public void setAdditionalInputs(float c, float tq)
	{
		contextswitching = c;
		timequantum = tq;
	}

	public void setAll_algorithms(boolean state)
	{
		for (int i = 0; i < NUMBEROFALGORITHMS; i++)
		{
			run_algorrithms[i] = state;
		}
	}

	public void clearAll()
	{
		for (int i = 0; i < NUMBEROFALGORITHMS; i++)
		{
			eventlist.elementAt(i).clear();
		}
		processlist.clear();

		setAll_algorithms(false);
	}
	
	public static String getAlgoName(int index)
	{
		switch (index)
		{
		case 0:
			return "FCFS";
		case 1:
			return "SJF";
		case 2:
			return "RR";
		case 3:
			return "HPFSN";
		case 4:
			return "HPFSP";
		case 5:
			return "HPFD";
		case 6:
			return "WRR";
		}
		
		return "default";
	}
	
	private void insertEventList(int index,Vector<Event> evlist)
	{
		Vector<Event> newevlist = new Vector<Event>();
		
		for(int i=0;i<evlist.size();i++)
		{
			newevlist.add(evlist.elementAt(i));
		}
		
		eventlist.insertElementAt(newevlist, index);
	}
	
	public void simulate() throws PSSexception
	{
		try
		{
			FHI.Read(inputfile);
			processlist = FHI.input_list;
			PS.setTimeQuantum(timequantum);
			PS.setContextSwitching(contextswitching);
			
			eventlist = new Vector<Vector<Event>>(NUMBEROFALGORITHMS);
			for (int i = 0; i < NUMBEROFALGORITHMS; i++)
			{	
				eventlist.add(null);
				if (run_algorrithms[i])
				{
					//run the algorithm
					PS.set_BackGroundQueue(processlist);
					PS.runAlgorithm(i);
					//this.eventlist.insertElementAt( PS.eventlist, i);
					insertEventList(i,PS.eventlist);
					//this.eventlist.
					//calculate the results
					RG.setLists(PS.processlist, eventlist.elementAt(i));
					RG.Result();
					
					//output results
					FHO.generateDir(outputdir, getAlgoName(i));
					FHO.generate_LogFile(eventlist.elementAt(i));
					FHO.generate_ConfigFile(getAlgoName(i), contextswitching, timequantum, inputfile);
					FHO.generate_ResultFile(RG.resultString());
						FHO.write_files();
				}
			}
			
			GG.setEventlist(eventlist);
			GG.setProcessList(processlist);

		} catch (PSSexception e)
		{
			System.out.println(e.getErrorType() + ":\n" + e.getDiscription()
					+ "\n");
			throw e;
		}
	}
	
	public void drawGraph(int index)
	{
		GG.drawGraph(index);
	}
	
	public void set_inputfile(String ifile)
	{
		inputfile = ifile;
	}
	
	public void set_OutputDir(String odir)
	{
		outputdir = odir;
	}
	
	public String get_Output()
	{
		return outputdir;
	}
	
	public void enableAlgorithm(int index)
	{
		run_algorrithms[index] = true;
	}
}
