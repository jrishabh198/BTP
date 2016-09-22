package flexgridsim.util;

import java.util.ArrayList;

import flexgridsim.DeadlineEvent;
import flexgridsim.Flow;

/**
 * The Class Batch.
 */
public class Batch extends ArrayList<Flow>{

	private static final long serialVersionUID = -5949815745169072537L;
	private DeadlineEvent shortestDeadlineEvent;
	private DeadlineEvent oldShortestDeadlineEvent;
	private int src;
	private int dst;
	static long flowId = 0;
	/**
	 * Instantiates a new batch.
	 *
	 * @param src the src
	 * @param dst the dst
	 */
	public Batch(int src, int dst){
		shortestDeadlineEvent = new DeadlineEvent(Double.MAX_VALUE, this);
		oldShortestDeadlineEvent = new DeadlineEvent(Double.MAX_VALUE, this);
		this.src = src;
		this.dst = dst;
	}
	
	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(Flow flow){
		super.add(flow);
		if (flow.getDeadline() < this.shortestDeadlineEvent.getTime()){
			this.oldShortestDeadlineEvent = shortestDeadlineEvent;
			this.shortestDeadlineEvent = new DeadlineEvent(flow.getDeadline(), this);
		}
		return true;
	}

	/**
	 * Gets the old shortest deadline event.
	 *
	 * @return the old shortest deadline event
	 */
	public DeadlineEvent getOldShortestDeadlineEvent() {
		return oldShortestDeadlineEvent;
	}

	/**
	 * Gets the src.
	 *
	 * @return the src
	 */
	public int getSource() {
		return src;
	}

	/**
	 * Gets the dst.
	 *
	 * @return the dst
	 */
	public int getDestination() {
		return dst;
	}
	
	/**
	 * Gets the shortest deadline.
	 *
	 * @return the shortest deadline
	 */
	public double getShortestDeadline() {
		return shortestDeadlineEvent.getTime();
	}

	/**
	 * Gets the shortest deadline event.
	 *
	 * @return the shortest deadline event
	 */
	public DeadlineEvent getShortestDeadlineEvent() {
		return shortestDeadlineEvent;
	}

	/**
	 * Convert batch to single flow.
	 *
	 * @param batch the batch
	 * @return the flow
	 */
	public Flow convertBatchToSingleFlow(){
		int rateSum = 0;
		int maxCos = 0;
		double maxDuration = 0;
		for (Flow flow : this) {
			rateSum += flow.getRate();
			
			if (flow.getCOS() > maxCos) {
				maxCos = flow.getCOS();
			}
			if (flow.getDuration() > maxDuration) {
				maxDuration = flow.getDuration();
			}
		}
		flowId++;
		Flow flow = new Flow(Long.MAX_VALUE - flowId, this.getSource(), this.getDestination(), shortestDeadlineEvent.getTime(), rateSum, maxDuration, maxCos, 0);
		flow.setBatch(true);
		flow.setNumberOfFlowsGroomed(this.size());
		return flow;
	}
	
}
