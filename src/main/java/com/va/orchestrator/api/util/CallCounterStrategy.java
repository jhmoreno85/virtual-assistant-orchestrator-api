package com.va.orchestrator.api.util;

/**
 * @author jlhuerta at mx1.ibm.com
 */
public class CallCounterStrategy {

    private int maxNumberOfCalls;

    public CallCounterStrategy(int maxNumberOfCalls) {
        this.maxNumberOfCalls = maxNumberOfCalls;
    }

    public boolean shouldCall() {
        return 0 < --this.maxNumberOfCalls;
    }

    public void increaseCounter() {
        this.maxNumberOfCalls--;
    }
}
