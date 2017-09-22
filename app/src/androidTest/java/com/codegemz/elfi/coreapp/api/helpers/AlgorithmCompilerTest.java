package com.codegemz.elfi.coreapp.api.helpers;


import android.test.ActivityInstrumentationTestCase2;

import com.codegemz.elfi.apicontracts.AlgorithmStepConstants;
import com.codegemz.elfi.apimanagers.AlgorithmDTO;
import com.codegemz.elfi.apimanagers.AlgorithmManager;
import com.codegemz.elfi.apimanagers.AlgorithmStepDTO;
import com.codegemz.elfi.apimanagers.AlgorithmStepManager;
import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.AlgorithmCompiler;
import com.codegemz.elfi.coreapp.helper.algorithm.AlgorithmExecutor;

/**
 * Created by adrobnych on 5/20/15.
 */
public class AlgorithmCompilerTest extends ActivityInstrumentationTestCase2<BrainActivity> {

    public AlgorithmCompilerTest() {
        super(BrainActivity.class);
    }

    private BrainActivity brainActivity;
    private AlgorithmManager aManager;
    private AlgorithmStepManager asManager;
    private int[] ids;
    private int algId;

    private AlgorithmCompiler ac;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ids = new int[5];
        brainActivity = getActivity();
        aManager = new AlgorithmManager(brainActivity);
        aManager.deleteAll();
        asManager = new AlgorithmStepManager(brainActivity);
        asManager.deleteAll();

        addOneAlgorithmAndTwoDummyAlgorithmSteps();
        addFightStandAlgorithmAndSteps();
        addFightStrikeAlgorithmAndSteps();

        ac = new AlgorithmCompiler(brainActivity);
    }

    private void addFightStandAlgorithmAndSteps() {
        AlgorithmDTO strikeAlgDTO = new AlgorithmDTO(
                "TEST_STAND",
                "parallel",
                "navigation",
                null,
                null,
                "my_alg_bundle");
        aManager.addAlgorithm(strikeAlgDTO);
        AlgorithmStepDTO asDTO = new AlgorithmStepDTO(
                10,
                AlgorithmStepConstants.StepIntent.StandHands.getShortIntentName(),
                null,
                null,
                "TEST_STAND");
        asManager.addAlgorithmStep(asDTO);
        asDTO = new AlgorithmStepDTO(
                40,
                AlgorithmStepConstants.StepIntent.End.getShortIntentName(),
                null,
                null,
                "TEST_STAND");
        asManager.addAlgorithmStep(asDTO);

    }

    private void addFightStrikeAlgorithmAndSteps() {
        AlgorithmDTO strikeAlgDTO = new AlgorithmDTO(
                "TEST_STRIKE",
                "parallel",
                "navigation",
                null,
                null,
                "my_alg_bundle");
        aManager.addAlgorithm(strikeAlgDTO);
        AlgorithmStepDTO asDTO = new AlgorithmStepDTO(
                10,
                AlgorithmStepConstants.StepIntent.LHook.getShortIntentName(),
                null,
                null,
                "TEST_STRIKE");
//        asManager.addAlgorithmStep(asDTO);
//        asDTO = new AlgorithmStepDTO(
//                20,
//                AlgorithmStepConstants.StepIntent.Wait.getShortIntentName(),
//                "2",
//                null,
//                "TEST_STRIKE");
//        asManager.addAlgorithmStep(asDTO);
        asDTO = new AlgorithmStepDTO(
                30,
                AlgorithmStepConstants.StepIntent.LHookOut.getShortIntentName(),
                null,
                null,
                "TEST_STRIKE");
        asManager.addAlgorithmStep(asDTO);

        asDTO = new AlgorithmStepDTO(
                1000,
                AlgorithmStepConstants.StepIntent.End.getShortIntentName(),
                null,
                null,
                "TEST_STRIKE");
        asManager.addAlgorithmStep(asDTO);
    }

    private void addOneAlgorithmAndTwoDummyAlgorithmSteps() {
        AlgorithmDTO aDTO1 = new AlgorithmDTO(
                "TEST_ALGORITHM",
                "parallel",
                "navigation",
                "kitchen",
                "lab",
                "my_alg_bundle");
        algId = aManager.addAlgorithm(aDTO1);
        AlgorithmStepDTO asDTO = new AlgorithmStepDTO(
                10,
                AlgorithmStepConstants.StepIntent.Turn.getShortIntentName(),
                "45",
                null,
                "TEST_ALGORITHM");
        ids[0] = asManager.addAlgorithmStep(asDTO);
        asDTO = new AlgorithmStepDTO(
                20,
                AlgorithmStepConstants.StepIntent.Go.getShortIntentName(),
                "10",
                null,
                "TEST_ALGORITHM");
        ids[1] = asManager.addAlgorithmStep(asDTO);
        asDTO = new AlgorithmStepDTO(
                30,
                AlgorithmStepConstants.StepIntent.AdjustCornerAtWall.getShortIntentName(),
                null,
                null,
                "TEST_ALGORITHM");
        ids[2] = asManager.addAlgorithmStep(asDTO);
        asDTO = new AlgorithmStepDTO(
                50,
                AlgorithmStepConstants.StepIntent.End.getShortIntentName(),
                null,
                null,
                "TEST_ALGORITHM");
        ids[3] = asManager.addAlgorithmStep(asDTO);
        asDTO = new AlgorithmStepDTO(
                40,
                AlgorithmStepConstants.StepIntent.GoToWall.getShortIntentName(),
                "10",
                null,
                "TEST_ALGORITHM");
        ids[4] = asManager.addAlgorithmStep(asDTO);
    }

    @Override
    public void tearDown(){
        //TODO: notify changes from inside CP -> http://stackoverflow.com/questions/15642975/android-refresh-listfragement-using-contentprovider-and-loader
        //abManager.clearAll();
    }

    public void testPreconditions() {

        assertNotNull("mainActivity is null", brainActivity);
        assertEquals("TEST_ALGORITHM", aManager.getAlgorithmById(algId).getName());
        assertEquals(ids[0], asManager.getAlgorithmStepById(ids[0]).get_id());
        assertEquals(".movement.Turn", asManager.getAlgorithmStepById(ids[0]).getIntent());
        assertEquals(ids[0], asManager.getAlgorithmStepById(ids[0]).get_id());
        assertEquals(".movement.Go", asManager.getAlgorithmStepById(ids[1]).getIntent());
        assertEquals(ids[1], asManager.getAlgorithmStepById(ids[1]).get_id());
    }

    public void testAlgorithmCompilation(){
        try {
            assertEquals("77\r45\r90\r10\r49\r0\r91\r10\r22\r0\r", ac.compile(algId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testRunSTANDFight(){
        new AlgorithmExecutor(brainActivity).findAndPerform("TEST_STAND");
    }

    public void testRunStrikeFight(){
        new AlgorithmExecutor(brainActivity).findAndPerform("TEST_STRIKE");
    }

}
