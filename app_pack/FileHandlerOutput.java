package app_pack;

import java.beans.ConstructorProperties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;

import exception_error_pack.PSSexception;

import mini_pack.Event;
import mini_pack.Event.eventtype;

public class FileHandlerOutput
{
	// file strings
	private String configfile;
	private String logfile;
	private String resultfile;

	// folder directory
	String out_dir;

	// Constructor
	public FileHandlerOutput()
	{

	}

	public void generateDir(String odir, String odirname) throws PSSexception
	{
		out_dir = odir + "/" + odirname;

		boolean success = new File(out_dir).mkdirs();
//		if (!success)
//			throw new PSSexception(PSSexception.errortype.output_error,
//					"can't create output directory");
	}

	public void generate_LogFile(Vector<Event> eventlist)
	{
		logfile = "# Time, Process_ID, Action\n";
		for (int i = 0; i < eventlist.size(); i++)
		{
			String eventtypeS = "";
			if (eventlist.elementAt(i).type == eventtype.arrived)
				eventtypeS = "Arrived";
			if (eventlist.elementAt(i).type == eventtype.scheduled)
				eventtypeS = "Scheduled";
			if (eventlist.elementAt(i).type == eventtype.terminated)
				eventtypeS = "Terminated";
			if (eventlist.elementAt(i).type == eventtype.preempted)
				eventtypeS = "Pre-empted";
			if (eventlist.elementAt(i).type == eventtype.context)
				eventtypeS = "Context_Switching";

			logfile += eventlist.elementAt(i).pID + " "
					+ eventlist.elementAt(i).time + " " + eventtypeS + "\n";
		}
	}

	public void generate_ConfigFile(String algoname, float c, float tq,
			String indir)
	{
		configfile = "# The name of schedulting algorithm:\n" + algoname + "\n";
		configfile += "# Context switching:\n" + c + "\n";
		configfile += "# Time quantum:\n" + tq+"\n";
		configfile += "# Input file path:\n" + indir+"\n";
		configfile += "# Output directory:\n" + out_dir+"\n";
	}

	public void generate_ResultFile(String rfile)
	{
		resultfile = rfile;
	}
	
	public void generate_file(String fname,String content) throws PSSexception
	{
		Writer writer = null;

		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(out_dir+"/"+fname+".txt"), "utf-8"));
			writer.write(content);
		} catch (IOException ex)
		{
			throw new PSSexception(PSSexception.errortype.output_error, "failed in creation the "+fname+" file");
		} finally
		{
			try
			{
				writer.close();
			} catch (Exception ex)
			{
			}
		}
	}
	
	public void write_files() throws PSSexception
	{
		generate_file("log",logfile);
		generate_file("Configuration",configfile);
		generate_file("Result",resultfile);
	}


	/* TEST METHODS */
	public void test_print() throws PSSexception
	{
		configfile = "test config file\nnew line";
		logfile = "test log file";
		resultfile = "test result file";
		
		generateDir("/home/mohamedmayla/Desktop","testoutfolder");
		write_files();	
	}
}
