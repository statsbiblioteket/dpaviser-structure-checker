package dk.statsbiblioteket.dpaviser;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ConfigConstants;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.newspaper.eventhandlers.Util;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
TODO fix this to work with the infomedia stuff
 */
@SuppressWarnings("deprecation") // ResultCollector
public class BatchStructureCheckerComponentIT {
    private final static String TEST_BATCH_ID = "400022028241";
    private final Properties properties = new Properties();
    private String pathToTestBatch;

    /**
     * Tests that the BatchStructureChecker can parse a production like batch which contain failures.
     */
    @Test(groups = "testDataTest", enabled = false)
    public void testGoodBatchStructureCheck() throws Exception {
        properties.setProperty(ConfigConstants.ITERATOR_FILESYSTEM_BATCHES_FOLDER, pathToTestBatch + "/" + "small-test-batch");
        properties.setProperty(
                ConfigConstants.AUTONOMOUS_BATCH_STRUCTURE_STORAGE_DIR,
                pathToTestBatch + "/" + "small-test-batch");
        properties.setProperty("batchStructure.storageDir", createTempDir().getAbsolutePath());

        BatchStructureCheckerComponent batchStructureCheckerComponent = new BatchStructureCheckerComponent(
                properties
        );
        ResultCollector resultCollector = new ResultCollector("Batch Structure Checker", "v0.1");
        Batch batch = new Batch();
        batch.setBatchID(TEST_BATCH_ID);
        batch.setRoundTripNumber(1);
        batchStructureCheckerComponent.doWorkOnItem(batch, resultCollector);
        if (!resultCollector.isSuccess()) {
            System.out.println(resultCollector.toReport());
        }
        assertTrue(resultCollector.isSuccess(), "Found failure with run on good batch");
    }

    /**
     * Tests that the BatchStructureChecker can parse a production like batch which contain failures.
     */
    @Test(groups = "externalTest", enabled = false)
    public void testGoodBatchStructureCheckFedora() throws Exception {

        properties.setProperty(ConfigConstants.ITERATOR_USE_FILESYSTEM, "false");
        BatchStructureCheckerComponent batchStructureCheckerComponent =
                new BatchStructureCheckerComponent(properties);

        ResultCollector resultCollector = new ResultCollector("Batch Structure Checker", "v0.1");
        Batch batch = new Batch();
        batch.setBatchID(TEST_BATCH_ID);
        batch.setRoundTripNumber(1);

        batchStructureCheckerComponent.doWorkOnItem(batch, resultCollector);
        if (!resultCollector.isSuccess()) {
            System.out.println(resultCollector.toReport());
        }
        assertTrue(resultCollector.isSuccess(), "Found failure with run on good batch");
    }


    /**
     * Tests that the BatchStructureChecker can parse a production like batch which should contain failures
     * for all .
     */
    @Test(groups = "testDataTest",enabled = false)
    public void testBadBatchStructureCheck() throws Exception {
        properties.setProperty(ConfigConstants.ITERATOR_FILESYSTEM_BATCHES_FOLDER, pathToTestBatch + "/" + "bad-bad-batch");
        properties.setProperty(ConfigConstants.AUTONOMOUS_BATCH_STRUCTURE_STORAGE_DIR, pathToTestBatch + "/" + "bad-bad-batch");
        properties.setProperty("batchStructure.storageDir", createTempDir().getAbsolutePath());

        BatchStructureCheckerComponent batchStructureCheckerComponent = new BatchStructureCheckerComponent(
                properties
        );
        ResultCollector resultCollector = new ResultCollector("Batch Structure Checker", "v0.1");
        Batch batch = new Batch();
        batch.setBatchID(TEST_BATCH_ID);
        batch.setRoundTripNumber(1);
        batchStructureCheckerComponent.doWorkOnItem(batch, resultCollector);
        assertFalse(resultCollector.isSuccess());
        System.out.println("Found " + Util.countFailures(resultCollector) + " failures.");
    }

    private File createTempDir() throws IOException {
        File temp = File.createTempFile("folder-name", "");
        temp.delete();
        temp.mkdir();
        temp.deleteOnExit();
        return temp;
    }

    @BeforeMethod(alwaysRun = true)
    private void loadConfiguration() throws Exception {
        pathToTestBatch = System.getProperty("integration.test.newspaper.testdata");
        properties.load(new FileInputStream(System.getProperty("integration.test.newspaper.properties")));
    }
}
