package com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper;

import android.content.Context;

import com.codegemz.elfi.apicontracts.AlgorithmStepConstants;
import com.codegemz.elfi.apimanagers.AlgorithmDTO;
import com.codegemz.elfi.apimanagers.AlgorithmManager;
import com.codegemz.elfi.apimanagers.AlgorithmStepDTO;
import com.codegemz.elfi.coreapp.BrainApp;

import java.util.List;

/**
 * Created by adrobnych on 9/9/15.
 */
public class AlgorithmCompiler {

    private Context context;

    public AlgorithmCompiler(Context context) {
        this.context = context;
    }

    public String compile(int algorithmId) throws Exception {
        AlgorithmManager am = new AlgorithmManager(context);
        List<AlgorithmStepDTO> steps =
                am.getStepsByAlgorithmId(algorithmId);
        StringBuilder result = new StringBuilder("");
        for(AlgorithmStepDTO s : steps){
            AlgorithmStepConstants.StepIntent si =  AlgorithmStepConstants.StepIntent.findByShortIntent(s.getIntent());
            if(si != null) {
                switch (si){
                    case Call:
                        AlgorithmDTO algorithmToInject = am.getAlgorithmByName(s.getValue1());
                        result.append(compile(algorithmToInject.get_id()));
                        break;
                    case Loop:
                        AlgorithmDTO algorithmToLoop = am.getAlgorithmByName(s.getValue1());
                        for(int i=0; i<(new Integer(s.getValue2())); i++)
                            result.append(compile(algorithmToLoop.get_id()));
                        break;
                    default:
                        result.append(si.ev3Code());
                        result.append(((BrainApp)context.getApplicationContext())
                                .getConnector().fieldDelimiter());
                        result.append(s.getValue1() == null ? "0" : s.getValue1());
                        result.append(((BrainApp)context.getApplicationContext())
                                .getConnector().commandDelimiter());
                }
            }
            else
                throw new Exception("This is not navigational step");
        }
        return result.toString();
    }
}
