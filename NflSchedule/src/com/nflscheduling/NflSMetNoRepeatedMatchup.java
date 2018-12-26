package com.nflscheduling;

import java.util.ArrayList;

public class NflSMetNoRepeatedMatchup extends NflScheduleMetric {

	public NflSMetNoRepeatedMatchup() {
		// TODO Auto-generated constructor stub
	}

	public NflSMetNoRepeatedMatchup(String theName) {
		super(theName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean computeMetric(NflSchedule schedule) {
       computeMetric(schedule,"NoRepeatedMatchup");
	   return true;
	}
}
