package mini_pack;

import exception_error_pack.*;

public class Process
{
	//Data member
	public int ID;
	public float arrivaltime;
	public float expectedruntime;
	public float currentruntime;
	public int priority;
	
	//calculation result
	public float Time_around;
	public float Weighted_around;
	public float FinishTime;
	
	//methods
	public Process(int id,float at,float ert,int pr) throws PSSexception
	{
		ID = id;
		arrivaltime = at;
		expectedruntime = ert;
		priority = pr;
		currentruntime = 0;
		
		
		//exception handling
		if(ID<0)
			throw new PSSexception(PSSexception.errortype.inputfile_error, 
					"ID is a negative value");
		
		if(arrivaltime<0)
			throw new PSSexception(PSSexception.errortype.inputfile_error, 
					"Arrival Time is a negative value");
	
		if(expectedruntime<0)
			throw new PSSexception(PSSexception.errortype.inputfile_error, 
					"Expected run time is a negative value");
		
		if(priority<0)
			throw new PSSexception(PSSexception.errortype.inputfile_error, 
					"Priority is a negative value");
		
		
	}
	
	public String toString()
	{
		String out=ID+" "+arrivaltime+" "+FinishTime+" "+expectedruntime+" "
					+priority+" "+Time_around+" "+Weighted_around;
		return out;
	
	}
	
}
