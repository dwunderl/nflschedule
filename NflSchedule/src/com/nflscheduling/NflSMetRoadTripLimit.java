package com.nflscheduling;

import java.util.ArrayList;

public class NflSMetRoadTripLimit extends NflScheduleMetric {

	public NflSMetRoadTripLimit() {
		// TODO Auto-generated constructor stub
	}

	public NflSMetRoadTripLimit(String theName) {
		super(theName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean computeMetric(NflSchedule schedule) {
       computeMetric(schedule,"RoadTripLimit");

	   return true;
	}
}
