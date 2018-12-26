package com.nflscheduling;

import java.util.ArrayList;

public class NflGMetRoadTripLimit extends NflGameMetric {

	public NflGMetRoadTripLimit(String theName, NflGameSchedule theGame) {
		super(theName, theGame);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean computeMetric(int weekNum, NflSchedule schedule, ArrayList<NflGameSchedule> candidateGames) {
       // Determine the penalty incurred if my gameSchedule causes too long of a road trip for the away team
       // no penalty for a 1 or 2 game road trip
       // a modest penalty for a 3 game road trip
       // a severe penalty for a road trip of 4 games or longer
       // look left and right from weekNum
		   
	   score = 0.0;
	   int roadTripLength = 1;
	   int firstRoadWeek;
	   int lastRoadWeek;
	   
       NflTeamSchedule homeTeamSched = schedule.findTeam(gameSchedule.game.homeTeam);
       NflTeamSchedule awayTeamSched = schedule.findTeam(gameSchedule.game.awayTeam);
       
       // left to earlier weeks
       firstRoadWeek = weekNum;
       for (int wi=weekNum-1; wi >= 1; wi--) {
          if (awayTeamSched.scheduledGames[wi-1] == null) {
             break;
          }
          
          if (awayTeamSched.scheduledGames[wi-1].isBye) {
              continue;
           }
          
          String prevWeekAwayTeam = awayTeamSched.scheduledGames[wi-1].game.awayTeam;
          if (!prevWeekAwayTeam.equalsIgnoreCase(awayTeamSched.team.teamName)) {
             break;
          }
          
          roadTripLength++;
          firstRoadWeek--;
       }

       // look right to later scheduled weeks
       lastRoadWeek = weekNum;
       for (int wi=weekNum+1; wi <= NflDefs.numberOfWeeks; wi++) {
          if (awayTeamSched.scheduledGames[wi-1] == null) {
             break;
          }
          
          if (awayTeamSched.scheduledGames[wi-1].isBye) {
              continue;
           }
           
          String nextWeekAwayTeam = awayTeamSched.scheduledGames[wi-1].game.awayTeam;
           
          if (!nextWeekAwayTeam.equalsIgnoreCase(awayTeamSched.team.teamName)) {
             break;
          }
          
          roadTripLength++;
          lastRoadWeek++;
        }
       
        if (roadTripLength == 3) {
  		   //System.out.println("roadTripLength == 3; lastRoadWeek: " + lastRoadWeek + ", firstRoadWeek: " + firstRoadWeek);
           score = .5;
           if (firstRoadWeek == 1) {
         	   //System.out.println("... found firstRoadWeek == 1");
               //score = 1.0;
               score = 4.0;
           }
           else if (lastRoadWeek == NflDefs.numberOfWeeks) {
              //System.out.println("... found lastRoadWeek == " + NflDefs.numberOfWeeks);
               //score = 1.0;
               score = 4.0;
           }
        }
        else if (roadTripLength > 3) {
           // score = 1.0;
           score = 4.0;
        }
        
	    //System.out.println("Info: RoadTripLength metric for game, weekNum: " + weekNum + " home team: " + homeTeamSched.team.teamName + " away team: " + awayTeamSched.team.teamName
	    //		               + ", roadTripLength: " + roadTripLength + ", score: " + score);
	   
	   return true;
	}
}
