package com.aim.hyperheuristics;



import com.aim.HyFlexUtilities;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * @author Warren G Jackson
 * @since 20/03/2025
 */
public class RLILS_AM_HH extends HyperHeuristic {
	
	private final int m_iDefaultScore, m_iLowerBound, m_iUpperBound;
	
	/**
	 * 
	 * @param seed The experimental seed.
	 * @param defaultScore The default score to give each heuristic in RWS.
	 * @param lowerBound The lower bound for each heuristic's score in RWS.
	 * @param upperBound The upper bound for each heursitic's score in RWS.
	 */
	public RLILS_AM_HH(long seed, int defaultScore, int lowerBound, int upperBound) {
		
		super(seed);
		
		this.m_iDefaultScore = defaultScore;
		this.m_iLowerBound = lowerBound;
		this.m_iUpperBound = upperBound;
	}

	/**
	 * TODO - Implement a selection hyper-heuristic using a reinforcement learning based
	 * roulette wheel selection (RWS) heuristic selection method accepting all moves (AM).
	 * 
	 * PSEUDOCODE:
	 * 
	 * INPUT: problem_instance, default_score, lower_bound, upper_bound
	 * mtns <- { MUTATION } 
	 * lss <- { LOCAL_SEARCH } 
	 * hs <- { (a, b) | a <- mtns, b <- lss }
	 * s <- initialiseSolution();
	 * rws <- initialiseNewRouletteWheelSelectionMethod();
	 * 
	 * WHILE termination criterion is not met DO
	 * 
	 *     h <- rws.performRouletteWheelSelection();
	 *     s' <- h(s);
	 *     
	 *     updateHeuristicScore(h_i, f(s), f(s'));
	 *     
	 *     accept(); // all moves
	 * END_WHILE
	 * 
	 * return s_{best};
	 */
	
	// remember to update the roulette wheel selection based on feedback
	public void solve(ProblemDomain oProblem) {
		
		// TODO ...

	}
	
	public String toString() {

		return "RL-ILS_AM_HH";
	}

}
