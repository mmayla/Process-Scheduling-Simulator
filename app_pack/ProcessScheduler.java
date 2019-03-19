package app_pack;

import java.sql.Savepoint;
import java.util.*;

import exception_error_pack.PSSexception;

import mini_pack.*;
import mini_pack.Event.eventtype;
import mini_pack.Process;

public class ProcessScheduler
{
	// private
	// Data members
	private float contextswitching;
	private float timequantum;
	public Vector<Event> eventlist;
	public Vector<Process> processlist;
	private boolean fifo; // to choose between using of normal ready_queue or
							// fifo ready queue

	// queues
	public ReadyQueueComparator RQC;
	public PriorityQueue<Process> background_queue; // set by PSSManager
	private PriorityQueue<Process> ready_queue;
	private Queue<Process> ready_queue_fifo; // to initialize use:
												// ready_queue_fifo = new
												// LinkedList<Process>();

	// methods
	private void _constructor(float c, float tq)
	{
		contextswitching = c;
		timequantum = tq;

		// Initialize the comparator
		RQC = new ReadyQueueComparator(
				ReadyQueueComparator.queueType.background);

		// Initialize back_ground queue
		background_queue = new PriorityQueue<Process>(50,
				new ReadyQueueComparator(
						ReadyQueueComparator.queueType.background));

		// Initialize event list
		eventlist = new Vector<Event>();

		// Initialize process list
		processlist = new Vector<Process>();
	}

	private void clearAll()
	{
		eventlist.clear();
		processlist.clear();
	}

	private void incrementPriority_ReadyQueue(int dpriority)
	{
		Process uproc;
		Vector<Process> saveprocess = new Vector<Process>();
		while (!ready_queue.isEmpty())
		{
			uproc = ready_queue.poll();
			uproc.priority += dpriority;
			saveprocess.add(uproc);
		}

		for (int i = 0; i < saveprocess.size(); i++)
		{
			ready_queue.add(saveprocess.elementAt(i));
		}
	}

	// public
	public ProcessScheduler(float c, float tq)
	{
		_constructor(c, tq);
	}

	public ProcessScheduler(float c)
	{
		_constructor(c, 0);
	}

	public void set_BackGroundQueue(Vector<Process> plist) throws PSSexception
	{
		background_queue.clear();

		Process newproc;

		for (int i = 0; i < plist.size(); i++)
		{
			newproc = new Process(plist.elementAt(i).ID,
					plist.elementAt(i).arrivaltime,
					plist.elementAt(i).expectedruntime,
					plist.elementAt(i).priority);
			background_queue.add(newproc);
		}
	}

	public void setTimeQuantum(float tq)
	{
		timequantum = tq;
	}

	public void setContextSwitching(float c)
	{
		contextswitching = c;
	}

	public void addEvent(int id, float t, eventtype et)
	{
		eventlist.add(new Event(id, t, et));
	}

	public void updateReady(float time)
	{
		Queue<Process> RQ;
		if (fifo)
			RQ = ready_queue_fifo;
		else
			RQ = ready_queue;

		while (true)
		{
			if (background_queue.isEmpty())
				break;

			if (background_queue.element().arrivaltime <= time)
			{
				// add to ready queue
				RQ.add(background_queue.element());

				// add arriving event
				addEvent(background_queue.element().ID,
						background_queue.element().arrivaltime,
						Event.eventtype.arrived);

				// remove the head of background queue
				background_queue.poll();

			} else
				break;
		}
	}

	public void terminateProcess(Process p, float time)
	{
		addEvent(p.ID, time, Event.eventtype.terminated);
		p.FinishTime = time;
		processlist.add(p);
	}

	/* scheduling algorithms */
	public void runAlgorithm(int index)
	{
		switch (index)
		{
		case 0:
			this.FCFS();
			break;
		case 1:
			this.SJF();
			break;
		case 2:
			this.RR();
			break;
		case 3:
			this.HPFSN();
			break;
		case 4:
			this.HPFSP();
			break;
		case 5:
			this.HPFD();
			break;
		case 6:
			this.WRR();
			break;
		}
	}

	public void FCFS()
	{
		// clear all lists
		clearAll();

		// use normal ready_queue
		fifo = false;

		// set the comparator type for the algorithm (FCFS)
		RQC.setType(ReadyQueueComparator.queueType.FCFS);
		// Initialize the ready_queue with the comparator
		ready_queue = new PriorityQueue<Process>(50, RQC);

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		// update ready queue
		updateReady(current_time);

		while (true)
		{
			if (!ready_queue.isEmpty())
			{
				// add scheduling event
				addEvent(ready_queue.element().ID, current_time,
						Event.eventtype.scheduled);

				// update time
				current_time += ready_queue.element().expectedruntime;

				// update the ready queue + add the arrived elements event
				updateReady(current_time);

				// add termination event of current process
				terminateProcess(ready_queue.element(), current_time);
				// remove from read_queue
				ready_queue.poll();

				// add context switching overhead time
				if (!ready_queue.isEmpty())
					current_time += contextswitching;

			} else
			{
				// if there is no processes in the background queue
				if (background_queue.isEmpty())
					break;

				// update the current time to time of the head of background
				// queue
				current_time = background_queue.element().arrivaltime;

				// update ready queue
				updateReady(current_time);
			}
		}
	}

	public void SJF()
	{
		// clear all lists
		clearAll();

		// use normal ready_queue
		fifo = false;

		// set the comparator type for the algorithm (FCFS)
		RQC.setType(ReadyQueueComparator.queueType.SJF);
		// Initialize the ready_queue with the comparator
		ready_queue = new PriorityQueue<Process>(50, RQC);

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		Process running_process;
		// update ready queue
		updateReady(current_time);

		while (true)
		{
			if (!ready_queue.isEmpty())
			{
				// run the take out of ready_queue
				running_process = ready_queue.poll();

				// add scheduling event
				addEvent(running_process.ID, current_time,
						Event.eventtype.scheduled);

				// update time
				current_time += running_process.expectedruntime;

				// update the ready queue + add the arrived elements event
				updateReady(current_time);

				// add termination event of current process
				terminateProcess(running_process, current_time);

				// add context switching overhead time
				if (!ready_queue.isEmpty())
					current_time += contextswitching;

			} else
			{
				// if there is no processes in the background queue
				if (background_queue.isEmpty())
					return;

				// update the current time to time of the head of background
				// queue
				current_time = background_queue.element().arrivaltime;

				// update ready queue
				updateReady(current_time);
			}
		}
	}

	public void RR()
	{
		// clear all lists
		clearAll();

		// use FIFO ready_queue
		fifo = true;

		// Initialize the ready_queue with the comparator
		ready_queue_fifo = new LinkedList<Process>();

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		// update ready queue
		updateReady(current_time);

		Process running_process = ready_queue_fifo.poll();
		addEvent(running_process.ID, current_time, Event.eventtype.scheduled);

		while (true)
		{
			// the process will not finish
			if (running_process.currentruntime + timequantum < running_process.expectedruntime)
			{
				current_time += timequantum;
				running_process.currentruntime += timequantum;

				// update ready queue
				updateReady(current_time);

				if (!ready_queue_fifo.isEmpty())
				{
					addEvent(running_process.ID, current_time,
							Event.eventtype.preempted);
					ready_queue_fifo.add(running_process);
					running_process = null;

					// add context switching overhead time
					if (!ready_queue_fifo.isEmpty())
						current_time += contextswitching;
				}
			} else
			{
				// the time that process need to finish
				float dif = running_process.expectedruntime
						- running_process.currentruntime;
				current_time += dif;
				terminateProcess(running_process, current_time);
				// update ready queue
				updateReady(current_time);

				// add context switching overhead time
				if (!ready_queue_fifo.isEmpty())
					current_time += contextswitching;

				running_process = null;
			}

			// new time slice or finish
			if (ready_queue_fifo.isEmpty() && running_process == null)
			{
				if (background_queue.isEmpty()) // finished
					break;
				else
				{
					// update the current time to time of the head of background
					// queue
					current_time = background_queue.element().arrivaltime;

					// update ready queue
					updateReady(current_time);
				}
			}

			// get next process
			if (running_process == null)
			{
				running_process = ready_queue_fifo.poll();
				addEvent(running_process.ID, current_time,
						Event.eventtype.scheduled);
			}
		}
	}

	public void HPFSN()
	{
		// clear all lists
		clearAll();

		fifo = false;

		// set the comparator type for the algorithm (HPFSN)
		RQC.setType(ReadyQueueComparator.queueType.HPFSN);
		// Initialize the ready_queue with the comparator
		ready_queue = new PriorityQueue<Process>(50, RQC);

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		Process running_process;
		// update ready queue
		updateReady(current_time);

		while (true)
		{
			if (!ready_queue.isEmpty())
			{
				// run the take out of ready_queue
				running_process = ready_queue.poll();

				// add scheduling event
				addEvent(running_process.ID, current_time,
						Event.eventtype.scheduled);

				// update time
				current_time += running_process.expectedruntime;

				// update the ready queue + add the arrived elements event
				updateReady(current_time);

				// add termination event of current process
				terminateProcess(running_process, current_time);

				// add context switching overhead time
				if (!ready_queue.isEmpty())
					current_time += contextswitching;

			} else
			{
				// if there is no processes in the background queue
				if (background_queue.isEmpty())
					break;

				// update the current time to time of the head of background
				// queue
				current_time = background_queue.element().arrivaltime;

				// update ready queue
				updateReady(current_time);
			}
		}
	}

	public void d_HPFSP()
	{
		// clear all lists
		clearAll();

		fifo = false;

		// set the comparator type for the algorithm (HPFSP)
		RQC.setType(ReadyQueueComparator.queueType.HPFSP);
		// Initialize the ready_queue with the comparator
		ready_queue = new PriorityQueue<Process>(50, RQC);

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		Process running_process;
		// update ready queue
		updateReady(current_time);

		float nextarrival = 0;

		while (true)
		{
			if (!ready_queue.isEmpty())
			{
				running_process = ready_queue.poll();
				if (!background_queue.isEmpty())
					nextarrival = background_queue.peek().arrivaltime;
				else
					nextarrival = -1;

				float diff = running_process.expectedruntime
						- running_process.currentruntime;

				// the process will finish
				if (nextarrival != -1
						&& ((running_process.currentruntime + diff) <= nextarrival))
				{
					// terminate process
					terminateProcess(running_process, current_time + diff);

					current_time = nextarrival;

					// update ready
					updateReady(current_time);
				} else
				{
					running_process.currentruntime += nextarrival
							- running_process.currentruntime;

					if (nextarrival != -1)
						current_time = nextarrival;
					else
						current_time = diff + running_process.currentruntime;

					// update ready
					updateReady(current_time);

					if (ready_queue.peek().priority > running_process.priority)
					{
						addEvent(running_process.ID, current_time,
								Event.eventtype.preempted);
					}
				}

			}
		}
	}

	public void HPFSP()
	{
		// clear all lists
		clearAll();

		// use FIFO ready_queue
		fifo = false;

		// set the comparator type for the algorithm (HPFSP)
		RQC.setType(ReadyQueueComparator.queueType.HPFSP);
		// Initialize the ready_queue with the comparator
		ready_queue = new PriorityQueue<Process>(50, RQC);

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		// update ready queue
		updateReady(current_time);

		Process running_process = ready_queue.poll();
		addEvent(running_process.ID, current_time, Event.eventtype.scheduled);
		
		//int newtimequantum = 1;
		
		while (true)
		{
			// the process will not finish
			if (running_process.currentruntime + timequantum < running_process.expectedruntime)
			{
				current_time += timequantum;
				running_process.currentruntime += timequantum;

				// update ready queue
				updateReady(current_time);

				if (!ready_queue.isEmpty())
				{
					addEvent(running_process.ID, current_time,
							Event.eventtype.preempted);
					ready_queue.add(running_process);
					running_process = null;

					// add context switching overhead time
					if (!ready_queue.isEmpty())
						current_time += contextswitching;
				}
			} else
			{
				// the time that process need to finish
				float dif = running_process.expectedruntime
						- running_process.currentruntime;
				current_time += dif;
				terminateProcess(running_process, current_time);
				// update ready queue
				updateReady(current_time);

				// add context switching overhead time
				if (!ready_queue.isEmpty())
					current_time += contextswitching;

				running_process = null;
			}

			// new time slice or finish
			if (ready_queue.isEmpty() && running_process == null)
			{
				if (background_queue.isEmpty()) // finished
					break;
				else
				{
					// update the current time to time of the head of background
					// queue
					current_time = background_queue.element().arrivaltime;

					// update ready queue
					updateReady(current_time);
				}
			}

			// get next process
			if (running_process == null)
			{
				running_process = ready_queue.poll();
				addEvent(running_process.ID, current_time,
						Event.eventtype.scheduled);
			}
		}
	}

	public void _HPFSP()
	{
		// clear all lists
		clearAll();

		fifo = false;

		// set the comparator type for the algorithm (HPFSP)
		RQC.setType(ReadyQueueComparator.queueType.HPFSP);
		// Initialize the ready_queue with the comparator
		ready_queue = new PriorityQueue<Process>(50, RQC);

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		Process running_process;
		// update ready queue
		updateReady(current_time);

		while (true)
		{
			if (!ready_queue.isEmpty())
			{
				// run the take out of ready_queue
				running_process = ready_queue.poll();

				// add scheduling event
				addEvent(running_process.ID, current_time,
						Event.eventtype.scheduled);

				while (!background_queue.isEmpty())
				{
					if ((current_time + (running_process.expectedruntime - running_process.currentruntime)) < (background_queue
							.element().arrivaltime))
					{
						current_time += running_process.expectedruntime
								- running_process.currentruntime;
						updateReady(current_time);

						// add termination event of current process
						terminateProcess(running_process, current_time);
					}

					else
					{
						running_process.currentruntime += (background_queue
								.element().arrivaltime - current_time);
						current_time = background_queue.element().arrivaltime;
						updateReady(current_time);
						if (ready_queue.element().priority > running_process.priority)
						{
							ready_queue.add(running_process);
							addEvent(running_process.ID, current_time,
									Event.eventtype.preempted);

							// add context switching overhead time
							if (!ready_queue.isEmpty())
								current_time += contextswitching;

							running_process = ready_queue.poll();
							addEvent(running_process.ID, current_time,
									Event.eventtype.scheduled);
							// update time
							current_time += running_process.expectedruntime;

							// update the ready queue + add the arrived elements
							// event
							// updateReady(current_time);

							// add termination event of current process
							// addEvent(running_process.ID,
							// current_time,Event.eventtype.terminated);

						}
					}
				}
				// update time
				current_time += running_process.expectedruntime;

				// update the ready queue + add the arrived elements event
				updateReady(current_time);

				// add termination event of current process
				terminateProcess(running_process, current_time);

				// add context switching overhead time
				if (!ready_queue.isEmpty())
					current_time += contextswitching;

			} else
			{
				// if there is no processes in the background queue
				if (background_queue.isEmpty())
					break;

				// update the current time to time of the head of background
				// queue
				current_time = background_queue.element().arrivaltime;

				// update ready queue
				updateReady(current_time);
			}
		}
	}

	public void HPFD()
	{
		// clear all lists
		clearAll();

		// use FIFO ready_queue
		fifo = false;

		// dynamic time quantum
		int dtimequantum = 1;

		// set the comparator type for the algorithm (HPFSP)
		RQC.setType(ReadyQueueComparator.queueType.HPFD);
		// Initialize the ready_queue with the comparator
		ready_queue = new PriorityQueue<Process>(50, RQC);

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		// update ready queue
		updateReady(current_time);

		Process running_process = ready_queue.poll();
		addEvent(running_process.ID, current_time, Event.eventtype.scheduled);

		while (true)
		{
			// the process will not finish
			if (running_process.currentruntime + dtimequantum < running_process.expectedruntime)
			{
				current_time += dtimequantum;
				running_process.currentruntime += dtimequantum;

				// update ready queue
				updateReady(current_time);

				// change priority
				incrementPriority_ReadyQueue(1);
				running_process.priority = running_process.priority == 0 ? running_process.priority
						: running_process.priority - 1;

				if (!ready_queue.isEmpty()
						&& (ready_queue.peek().priority > running_process.priority))
				{
					addEvent(running_process.ID, current_time,
							Event.eventtype.preempted);
					ready_queue.add(running_process);
					running_process = null;

					// add context switching overhead time
					if (!ready_queue.isEmpty())
						current_time += contextswitching;
				}
			} else
			{
				// the time that process need to finish
				float dif = running_process.expectedruntime
						- running_process.currentruntime;
				current_time += dif;
				terminateProcess(running_process, current_time);

				// update ready queue
				updateReady(current_time);

				// change priority
				incrementPriority_ReadyQueue(1);

				// add context switching overhead time
				if (!ready_queue.isEmpty())
					current_time += contextswitching;

				running_process = null;
			}

			// new time slice or finish
			if (ready_queue.isEmpty() && running_process == null)
			{
				if (background_queue.isEmpty()) // finished
					break;
				else
				{
					// update the current time to time of the head of background
					// queue
					current_time = background_queue.element().arrivaltime;

					// update ready queue
					updateReady(current_time);
				}
			}

			// get next process
			if (running_process == null)
			{
				running_process = ready_queue.poll();
				addEvent(running_process.ID, current_time,
						Event.eventtype.scheduled);
			}
		}
	}

	public void WRR()
	{
		// clear all lists
		clearAll();

		// use FIFO ready_queue
		fifo = false;

		// set the comparator type for the algorithm (HPFSP)
		RQC.setType(ReadyQueueComparator.queueType.WRR);
		// Initialize the ready_queue with the comparator
		ready_queue = new PriorityQueue<Process>(50, RQC);

		// get the first arrived processes
		float current_time = background_queue.element().arrivaltime;
		// update ready queue
		updateReady(current_time);

		Process running_process = ready_queue.poll();
		addEvent(running_process.ID, current_time, Event.eventtype.scheduled);

		while (true)
		{
			// the process will not finish
			if (running_process.currentruntime + timequantum < running_process.expectedruntime)
			{
				current_time += timequantum;
				running_process.currentruntime += timequantum;

				// update ready queue
				updateReady(current_time);

				if (!ready_queue.isEmpty())
				{
					addEvent(running_process.ID, current_time,
							Event.eventtype.preempted);
					ready_queue.add(running_process);
					running_process = null;
				}
			} else
			{
				// the time that process need to finish
				float dif = running_process.expectedruntime
						- running_process.currentruntime;
				current_time += dif;
				terminateProcess(running_process, current_time);
				// update ready queue
				updateReady(current_time);

				running_process = null;
			}

			// new time slice or finish
			if (ready_queue.isEmpty() && running_process == null)
			{
				if (background_queue.isEmpty()) // finished
					break;
				else
				{
					// update the current time to time of the head of background
					// queue
					current_time = background_queue.element().arrivaltime;

					// update ready queue
					updateReady(current_time);
				}
			}

			// get next process
			if (running_process == null)
			{
				running_process = ready_queue.poll();
				addEvent(running_process.ID, current_time,
						Event.eventtype.scheduled);
			}
		}
	}

	/* methods for testing only */
	public void printEventList()
	{
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

			System.out.println(eventlist.elementAt(i).pID + " "
					+ eventlist.elementAt(i).time + " " + eventtypeS);
		}
	}

}
