package com.nflscheduling;

public class NflSMetHomeStandLimit extends NflScheduleMetric {

	public NflSMetHomeStandLimit() {
		// TODO Auto-generated constructor stub
	}

	public NflSMetHomeStandLimit(String theName) {
		super(theName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean computeMetric(NflSchedule schedule) {
       computeMetric(schedule,"HomeStandLimit");
       
	   return true;
	}
}
