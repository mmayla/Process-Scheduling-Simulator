package app_pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import mini_pack.Process;
import exception_error_pack.PSSexception;

public class FileHandlerInput
{
	protected Vector<Process> input_list;
	
	//priavte
	private String removeComments(String dir) throws PSSexception
	{
		try
		{
			Scanner in = new Scanner(new File(dir));
			String infile = "",line;
			while (in.hasNextLine())
			{
				line = in.nextLine();
				if (line.charAt(0) != '#')
				{
					infile = infile.concat(line+" ");
				}
			}
			
			in.close();
			return infile;
		} catch (FileNotFoundException e)
		{
			throw new PSSexception(PSSexception.errortype.inputfile_error,
					"Input file not found");
		}

	}
	
	// public
	public FileHandlerInput()
	{
		input_list = new Vector<Process>();
	}

	public void Read(String dir) throws PSSexception
	{
		//clear list
		input_list.clear();
		
		//remove comments from the input file
		String infile = removeComments(dir);		
		Scanner in = new Scanner(infile);
		
		int id,pr,n;
		float at,ert;
		
		n = in.nextInt();
		
		for(int i=0;i<n;i++)
		{
			id = in.nextInt();
			at = in.nextFloat();
			ert = in.nextFloat();
			pr = in.nextInt();
			
			Process newprocess = new Process(id, at, ert, pr);
			
			input_list.add(newprocess);
		}
		
		in.close();
	}

	
	
	/* TESTING METHODS */
	public void test_print()
	{
		for(int i=0;i<input_list.size();i++)
		{
			System.out.println(input_list.elementAt(i).ID+" "+input_list.elementAt(i).arrivaltime+" "+
		input_list.elementAt(i).expectedruntime+" "+input_list.elementAt(i).priority);
		}
	}
}
