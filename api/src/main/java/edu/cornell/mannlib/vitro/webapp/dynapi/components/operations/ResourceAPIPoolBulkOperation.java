package edu.cornell.mannlib.vitro.webapp.dynapi.components.operations;

import edu.cornell.mannlib.vitro.webapp.dynapi.ResourceAPIPool;

public class ResourceAPIPoolBulkOperation extends PoolBulkOperation{

    public ResourceAPIPoolBulkOperation(){
		this.pool = ResourceAPIPool.getInstance();
	}
}
