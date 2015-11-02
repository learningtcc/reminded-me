package com.homefellas.queue;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author Name
 *
 *	concrete implementation of the interface queue. holds the thread pool of workers
 *  this basically is basically a thread pool, where each thread consumes EmailTask from a queue populated
 *  from the db as emails are added
 * 
 */
public class ConcreteQueue implements ITaskQueue {
	private Vector tasks;
	private boolean waiting;
	private boolean shutdown;
	private HashMap myTasks;
	private int maxNumberOfTasksInMemory;
	
	
	public void setShutdown(boolean isShutdown) {
		this.shutdown = isShutdown;
		synchronized (this) {
			notifyAll();
		}
	}
	
	public boolean isQueueFull() {
		System.out.println("number of tasks in memory: " + tasks.size());
		return (tasks.size() >= this.maxNumberOfTasksInMemory);
	}
	
	/**
	 * @see java.lang.Object#Object()
	 */
	public ConcreteQueue(int numberOfWorker, int aMaxNumberOfTasksInMemory) {
		this.maxNumberOfTasksInMemory = aMaxNumberOfTasksInMemory;
		tasks = new Vector(maxNumberOfTasksInMemory);
		waiting = false;
		
		Worker aWorker = null;
		for (int i = 0; i< numberOfWorker; i++) {
			aWorker = new Worker();
			aWorker.setWorkerNumber(i);
			// keep a list of the thread
			//myTasks.put(String.valueOf(i), aWorker);
			new Thread(aWorker).start();
		}

	}
	
	/**
	 * @put(IRunnableTask)
	 * add a task to the queue
	 */
	public void put(IRunnableTask r) {
		tasks.add(r);
		if (waiting) {
			synchronized (this) {
				notifyAll();
			}
		}
	}
	
	/**
	 * @take()
	 * takes a task from the queue if it is not empty otherwize block
	 */
	public IRunnableTask take() {
		if (tasks.isEmpty()) {
			synchronized (this) {
				waiting = true;
				try {
					wait();
				} catch (InterruptedException e) {
					waiting = false;
				}
			}
		}
		synchronized (tasks) {
			if (!tasks.isEmpty() && !shutdown) {
				return (IRunnableTask) tasks.remove(0);
			}
			return null;
		}
	}
	
	/**
	 * @author prc7037
	 *	This is a worker running in its own thread eating "tasks" from the task queue, when no tasks
	 *  are available in the queue just block and wait for food
	 */
	private class Worker implements Runnable {
		private int workerNumber = -1;
		
		public void setWorkerNumber(int aWorkerNumber) {
			this.workerNumber = aWorkerNumber;
		}
		
		public void run() {
			
			while (!shutdown) {
				IRunnableTask r = take();
				
				if (r != null) {
					
					r.execute();
					
				}
				
			}
			
		}
	}
}
