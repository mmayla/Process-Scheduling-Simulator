import java.io.File;
import java.util.PriorityQueue;

import exception_error_pack.PSSexception;
import gui.InputUI;
import mini_pack.Process;
import app_pack.FileHandlerInput;
import app_pack.FileHandlerOutput;
import app_pack.ProcessScheduler;

public class source
{
	public static void test_ProcessScheduler() throws PSSexception
	{
		ProcessScheduler testPS = new ProcessScheduler(0,10);

		testPS.background_queue.clear();
		
		// test case 1
		 testPS.background_queue.add(new Process(1, 0f, 50, 1));
		 testPS.background_queue.add(new Process(2, 20.5f, 20, 2));
		 testPS.background_queue.add(new Process(3, 35.5f, 100, 3));
		 testPS.background_queue.add(new Process(4, 35.2f, 40, 4));

		// test case 2
//		testPS.background_queue.add(new Process(1, 0f, 30, 1));
//		testPS.background_queue.add(new Process(2, 10.0f, 10, 2));
//		testPS.background_queue.add(new Process(3, 12.0f, 8, 3));
//		testPS.background_queue.add(new Process(4, 15.0f, 5, 4));

		testPS.WRR();

		
		testPS.printEventList();
		System.out.println("finish");
	}
	
	public static void test_FileHandlerInput()
	{
		FileHandlerInput FIO = new FileHandlerInput();
		
		try
		{
		FIO.Read("/home/mohamedmayla/workspace/InfinityDream_PSS/input.txt");
		
		
		FIO.test_print();
		
		System.out.println("finish");
		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void test_FileHandlerOut() throws PSSexception
	{
		FileHandlerOutput FHO = new FileHandlerOutput();
		
		FHO.test_print();
	}
	
	public static void main(String[] args) throws PSSexception
	{
		//test_ProcessScheduler();
		//test_FileHandlerInput();
		//test_FileHandlerOut();
		
		//RUN program
		InputUI frame = new InputUI();
		frame.setVisible(true);
	}

}
