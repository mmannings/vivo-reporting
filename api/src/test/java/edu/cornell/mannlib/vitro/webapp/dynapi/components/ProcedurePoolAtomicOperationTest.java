package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cornell.mannlib.vitro.webapp.dynapi.LoggingControl;
import edu.cornell.mannlib.vitro.webapp.dynapi.ProcedurePool;
import edu.cornell.mannlib.vitro.webapp.dynapi.ServletContextTest;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.operations.PoolOperation;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.operations.ProcedurePoolAtomicOperation;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.Data;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.DataStore;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.TestView;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.conversion.InitializationException;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.implementation.JsonContainerObjectParam;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.implementation.StringParam;

public class ProcedurePoolAtomicOperationTest extends ServletContextTest{

    private static final String JSON_OBJECT_PARAM = "jsonContainerParam";
    private static final String STRING_PARAM_NAME = "stringParam";
    private final static String TEST_ACTION_URI = "https://vivoweb.org/ontology/vitro-dynamic-api/procedure/testProcedure1";
    private ProcedurePool procedurePool;
    
    @Before
    public void preparePool() {
        LoggingControl.offLogs();
        procedurePool = initWithDefaultModel();
    }
    
    @After
    public void reset() {
        setup();
        procedurePool = initWithDefaultModel();
        LoggingControl.restoreLogs();
    }
    
    @Test
    public void componentLoadUnloadTest() throws InitializationException {
        ProcedurePoolAtomicOperation apao = new ProcedurePoolAtomicOperation();
        DataStore dataStore = new DataStore();
        long counter = procedurePool.count();
        apao.setOperationType(PoolOperation.OperationType.UNLOAD.toString());
        addStringParam(dataStore, apao);
        OperationResult result = apao.run(dataStore);
        assertEquals(OperationResult.ok().toString(),result.toString());
        assertEquals(counter - 1, procedurePool.count());
        
        apao.setOperationType(PoolOperation.OperationType.LOAD.toString());
        addStringParam(dataStore, apao);
        result = apao.run(dataStore);
        assertEquals(OperationResult.ok().toString(),result.toString());
        assertEquals(counter, procedurePool.count());
    }
    
    @Test
    public void componentReloadTest() throws InitializationException {
        ProcedurePoolAtomicOperation apao = new ProcedurePoolAtomicOperation();
        DataStore dataStore = new DataStore();
        Procedure action1 = null;
        Procedure action2 = null;
        try {
            action1 = procedurePool.getByUri(TEST_ACTION_URI);
            apao.setOperationType(PoolOperation.OperationType.RELOAD.toString());
            addStringParam(dataStore, apao);
            OperationResult result = apao.run(dataStore);
            assertEquals(OperationResult.ok().toString(),result.toString());
            action2 = procedurePool.getByUri(TEST_ACTION_URI);
            assertNotEquals(action1, action2);
        } finally {
            if (action1 != null) {
                action1.removeClient();    
            }
            if (action2 != null) {
                action2.removeClient();    
            }
        }
    }
    
    @Test
    public void componentStatusTest() throws InitializationException {
        ProcedurePoolAtomicOperation apao = new ProcedurePoolAtomicOperation();
        DataStore dataStore = new DataStore();
        addJsonArrayParam(dataStore, apao);
        apao.setOperationType(PoolOperation.OperationType.STATUS.toString());
        addStringParam(dataStore, apao);
        OperationResult result = apao.run(dataStore);
        assertEquals(OperationResult.ok().toString(),result.toString());
        Data data = dataStore.getData(JSON_OBJECT_PARAM);
        assertNotNull(data);
        String expectedValue = "{\"" + TEST_ACTION_URI + "\":true}";
        assertEquals(expectedValue, data.getSerializedValue());
    }

    private void addStringParam(DataStore dataStore, ProcedurePoolAtomicOperation apao) {
        Parameter plainStringParam = new StringParam(STRING_PARAM_NAME);
        apao.addInputParameter(plainStringParam);
        Data plainStringData = new Data(plainStringParam);
        TestView.setObject(plainStringData, TEST_ACTION_URI);
        dataStore.addData(STRING_PARAM_NAME, plainStringData);
    }
    
    private void addJsonArrayParam(DataStore dataStore, ProcedurePoolAtomicOperation apao) {
        Parameter jsonObjectParam = new JsonContainerObjectParam(JSON_OBJECT_PARAM);
        apao.addOutputParameter(jsonObjectParam);
    }
    
    private ProcedurePool initWithDefaultModel() {
        try {
            loadDefaultModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProcedurePool actionPool = ProcedurePool.getInstance();
        actionPool.init(servletContext);
        return actionPool;
    }
}
