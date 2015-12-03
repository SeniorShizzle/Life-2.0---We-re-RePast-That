import com.sun.xml.internal.ws.util.StringUtils;



	/** Enumerated type containing various states of the life of a fish. Used in the state-machine apparatus. */
	public enum FishLifeState {
		EGG_INCUBATION(),
		FRY_COLONIZATION(),
		ZERO_AGE_RESIDENT_REARING(),
		ZERO_AGE_MIGRANT(),
		ZERO_INACTIVE(),
		ONE_AGE_RESIDENT_REARING(),
		ONE_AGE_MIGRANT(),
		ONE_AGE_TRANSIENT_REARING(),
		TWO_PLUS_AGE_TRANSIENT_REARING(),
		MIGRANT_PRESPAWNER(),
		HOLDING_PREPAWNER(),
		SPAWNING();

	    public static FishLifeState[] vals = values();
	    
	    /**
	     * Get the next state in order
	     * @param state
	     * @return
	     */
	    public static FishLifeState nextState(FishLifeState state) {
	    	// first, find the ordinal position of current state
	    	int i;
	    	for (i = 0; i < vals.length; i++) { if (vals[i] == state) break; }
	    	// then return the next one
	    	return FishLifeState.vals[(i + 1) % vals.length];
	    }

//		public FishLifeState nextState() {
//			// TODO Auto-generated method stub
//			return vals[(this.ordinal()+1) % vals.length];
//		}

	    /**
	     * String[] of fish life state names
	     * @return
	     */
		public static String[] strings() {
		    FishLifeState[] states = values();
		    String[] names = new String[states.length];

		    for (int i = 0; i < states.length; i++) {
		    	names[i] = StringUtils.capitalize(states[i].name().toLowerCase());
		    }

		    return names;
		}
	}

