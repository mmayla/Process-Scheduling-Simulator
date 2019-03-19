package exception_error_pack;



public class PSSexception extends Exception 
{
	private static final long serialVersionUID = 1L;

	public static enum errortype
	{
		inputfile_error,
		processing_error,
		output_error;
	}
	
	private errortype etype;
	private String discription;
	
	public PSSexception(errortype et, String dist)
	{
		etype = et;
		discription = dist;
	}
	
	public errortype getErrorType()
	{
		return etype;
	}
	
	public String getDiscription()
	{
		return discription;
	}
}
