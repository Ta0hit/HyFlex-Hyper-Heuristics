package com.aim.project.ssp.runners;

import com.aim.project.ssp.hyperheuristics.LS_HH;
import com.aim.project.ssp.hyperheuristics.LS2_HH;
import AbstractClasses.HyperHeuristic;

public class LS_VisualRunner extends HH_Runner_Visual {

    @Override
    protected HyperHeuristic getHyperHeuristic(long seed) {
        // instantiate your learning‐based HH here
        return new LS2_HH(seed);
    }

    public static void main(String[] args) {
        // create an instance of the visual runner
        HH_Runner_Visual runner = new LS_VisualRunner();
        // run the hyper-heuristic
        runner.run();
    }
}
