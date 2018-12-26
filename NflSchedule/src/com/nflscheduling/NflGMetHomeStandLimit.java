package com.nflscheduling;

import java.util.ArrayList;

public class NflGMetHomeStandLimit extends NflGameMetric {

	NflGMetHomeStandLimit(String theName, NflGameSchedule theGameSchedule) {
		super(theName, theGameSchedule);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean computeMetric(int weekNum, NflSchedule schedule, ArrayList<NflGameSchedule> candidateGames) {
	       // Determine the penalty incurred if my gameSchedule causes too long of a home stand for the home team
	       // no penalty for a 1 or 2 game home
	       // a modest penalty for a 3 game home stand
	       // a severe penalty for a home stand of 4 games or longer
	       // look left and right from weekNum
			   
		   score = 0.0;
		   int homeStandLength = 1;
		   int firstHomeWeek;
		   int lastHomeWeek;
		   
	       NflTeamSchedule homeTeamSched = schedule.findTeam(gameSchedule.game.homeTeam);
	       NflTeamSchedule awayTeamSched = schedule.findTeam(gameSchedule.game.awayTeam);
	       
	       // look left to earlier weeks
	       firstHomeWeek = weekNum;
	       for (int wi=weekNum-1; wi >= 1; wi--) {
	          if (homeTeamSched.scheduledGames[wi-1] == null) {
	             break;
	          }
	          
	          if (homeTeamSched.scheduledGames[wi-1].isBye) {
	             continue;
	          }
	          
	          String prevWeekHomeTeam = homeTeamSched.scheduledGames[wi-1].game.homeTeam;
	          if (!prevWeekHomeTeam.equalsIgnoreCase(homeTeamSched.team.teamName)) {
	             break;
	          }
	          
	          homeStandLength++;
	          firstHomeWeek--;
	       }

	       // look right to later scheduled weeks
	       lastHomeWeek = weekNum;
	       for (int wi=weekNum+1; wi <= NflDefs.numberOfWeeks; wi++) {
	           if (homeTeamSched.scheduledGames[wi-1] == null) {
	         	  break;
	           }
	           
		       if (homeTeamSched.scheduledGames[wi-1].isBye) {
			      continue;
			   }
		       
	           String nextWeekHomeTeam = homeTeamSched.scheduledGames[wi-1].game.homeTeam;
	           
	           if (!nextWeekHomeTeam.equalsIgnoreCase(homeTeamSched.team.teamName)) {
	         	  break;
	           }
	           
	           homeStandLength++;
	           lastHomeWeek++;
	        }
	       
	        if (homeStandLength == 3) {
	  		   //System.out.println("homeStandLength == 3; lastHomeWeek: " + lastHomeWeek + ", firstHomeWeek: " + firstHomeWeek);
	           score = .5;
	           if (firstHomeWeek == 1) {
	              //System.out.println("... found firstHomeWeek == 1");
		          // score = 1.0;
		          score = 2.0;
	           }
	           else if (lastHomeWeek == NflDefs.numberOfWeeks) {
	              // System.out.println("... found lastHomeWeek == " + NflDefs.numberOfWeeks);
		          // score = 1.0;
		          score = 2.0;
	           }
	        }
	        else if (homeStandLength > 3) {
		       // score = 1.0;
		       score = 2.0;
	        }
	        
		    //System.out.println("Info: homeStandLength metric for game, weekNum: " + weekNum + " home team: " + homeTeamSched.team.teamName + " away team: " + awayTeamSched.team.teamName
		    //		               + ", homeStandLength: " + homeStandLength + ", score: " + score);
		   
		   return true;
		}
}
