package com.nflscheduling;

import java.util.ArrayList;

public class NflGMetNoRepeatedMatchup extends NflGameMetric {

	NflGMetNoRepeatedMatchup(String theName, NflGameSchedule theGameSchedule) {
		super(theName, theGameSchedule);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean computeMetric(int weekNum, NflSchedule schedule, ArrayList<NflGameSchedule> candidateGames) {
	   // Ensure there is not a weekNum-1 or weekNum+1 scheduled game that has the same teams
	   // as the gameSchedule linked to this gameMetric
	   // if no repeated matchups in either direction - then set the score to 0
	   // if a repeated matchup exists in either direction - set the score to 1
	

	   // for each team in my gameSchedule 
	   // if weekNum+1 is a valid week (NflDefs.numberOfWeeks)
       // check weekNum+1 schedule for TeamSchedule(s) homeTeam and then awayTeam
	   // if weekNum+1 has a game scheduled, ensure it's gameSchedule teams are not the same as mine
	   // if weekNum-1 is a valid week
       // if weekNum-1 has a game scheduled, ensure it's gameSchedule teams are not the same as mine
       
	   // check my home teams scheduled game for a repeated matchup
	   
       score = 0.0;
       
	   NflTeamSchedule homeTeamSched = schedule.findTeam(gameSchedule.game.homeTeam);
	   NflTeamSchedule awayTeamSched = schedule.findTeam(gameSchedule.game.awayTeam);
       //System.out.println("Info: No repeated match up metric for game, weekNum: " + weekNum + " home team: " + homeTeamSched.team.teamName + " away team: " + awayTeamSched.team.teamName);
	   
	   // Check next weeks game for no repeated matchup
	   // be aware of byes - skip over
	   if (weekNum+1 <= NflDefs.numberOfWeeks) {
		  // check first non-bye game
	      for (int wi=weekNum+1; wi <= NflDefs.numberOfWeeks; wi++) {
             NflGameSchedule nextWeeksGame = homeTeamSched.scheduledGames[wi-1]; // NOTE: weekNum starts at 1, must correct for index
             if (nextWeeksGame == null) {
            	 break;
             }
             
	    	 if (nextWeeksGame.isBye) {
	    		 continue;
	    	 }
	    	 
             if (nextWeeksGame.game.awayTeam.equalsIgnoreCase(gameSchedule.game.homeTeam) &&
	            nextWeeksGame.game.homeTeam.equalsIgnoreCase(gameSchedule.game.awayTeam)) {
                score = 10.0;
             }
             break;
	      }
	   }
	   	   
	   // Check previous weeks game for no repeated matchup
	   // be aware of byes - skip over
	   
	   if (weekNum > 1) {
		  // check first non-bye game
	      for (int wi=weekNum-1; wi >= 1; wi--) {
             NflGameSchedule prevWeeksGame = homeTeamSched.scheduledGames[wi-1]; // NOTE: weekNum starts at 1, must correct for index
             if (prevWeeksGame == null) {
            	 break;
             }
             
	    	 if (prevWeeksGame.isBye) {
	    		 continue;
	    	 }
	    	 
             if (prevWeeksGame.game.awayTeam.equalsIgnoreCase(gameSchedule.game.homeTeam) &&
                 prevWeeksGame.game.homeTeam.equalsIgnoreCase(gameSchedule.game.awayTeam)) {
                score = 10.0;
             }
             break;
	      }
	   }
	   
	   //System.out.println("Info: No Repeated Matchup metric for game, weekNum: " + weekNum + " home team: " + gameSchedule.game.homeTeam + " away team: " + gameSchedule.game.awayTeam
       //           + ", score: " + score);
	   return true;
	}
}
