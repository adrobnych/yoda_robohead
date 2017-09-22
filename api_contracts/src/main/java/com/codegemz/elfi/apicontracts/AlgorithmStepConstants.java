package com.codegemz.elfi.apicontracts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adrobnych on 8/19/15.
 */
public class AlgorithmStepConstants {
    public enum StepIntent{
        GoToWall, Go, Turn, AdjustCornerAtWall, WheelCalibration,
        GoSlow, SetTotalAngle, WheelTurn, End, Call, Loop, Wait,
        RHandGoSlow1, RHandGoSlow2, RHandGoSlow3,
        LHandGoSlow1, LHandGoSlow2, LHandGoSlow3,
        FullHands, RelaxHands, StandHands, RightBlockIn, LeftBlockIn, RightBlockOut, LeftBlockOut,
        LeftStrikeIn, LeftStrikeOut, RightStrikeIn, RightStrikeOut,
        HighStand, RightHighStrikePrepare, RightHighStrike,
        LeftHighStrikePrepare, LeftHighStrike, BackToStand, FullStandOut, LowToHighStand,
        RHook, RHookOut, LHook, LHookOut, FullHandsOut, Dance, Security,
        TransformerUp, TransformerDown;

        private static final Map<String, StepIntent> lookupByShortIntent = new HashMap<>();
        private static final Map<StepIntent, Integer> lookupForEV3Code = new HashMap<>();

        static {
            for (StepIntent si : StepIntent.values()) {
                lookupByShortIntent.put(si.getShortIntentName(), si);
            }
            //TODO: refactor to content provider and db
            lookupForEV3Code.put(Go, 90);
            lookupForEV3Code.put(GoSlow, 95);
            lookupForEV3Code.put(Turn, 77);
            lookupForEV3Code.put(GoToWall, 91);
            lookupForEV3Code.put(AdjustCornerAtWall, 49);
            lookupForEV3Code.put(WheelCalibration, 65);
            lookupForEV3Code.put(SetTotalAngle, 25);
            lookupForEV3Code.put(WheelTurn, 37);
            lookupForEV3Code.put(End, 22);
            lookupForEV3Code.put(Call, null);
            lookupForEV3Code.put(Loop, null);
            lookupForEV3Code.put(Wait, 11);

            //hands
            lookupForEV3Code.put(RHandGoSlow1, 819);
            lookupForEV3Code.put(RHandGoSlow2, 829);
            lookupForEV3Code.put(RHandGoSlow3, 839);
            lookupForEV3Code.put(LHandGoSlow1, 981);
            lookupForEV3Code.put(LHandGoSlow2, 982);
            lookupForEV3Code.put(LHandGoSlow3, 983);
            //Complex hands
            lookupForEV3Code.put(FullHands, 777);
            lookupForEV3Code.put(RelaxHands, 888);
            lookupForEV3Code.put(StandHands, 999);
            lookupForEV3Code.put(RightBlockIn, 889);
            lookupForEV3Code.put(RightBlockOut, 809);
            lookupForEV3Code.put(LeftBlockIn, 988);
            lookupForEV3Code.put(LeftBlockOut, 980);

            lookupForEV3Code.put(RightStrikeIn, 779);
            lookupForEV3Code.put(RightStrikeOut, 709);
            lookupForEV3Code.put(LeftStrikeIn, 977);
            lookupForEV3Code.put(LeftStrikeOut, 970);

            lookupForEV3Code.put(HighStand, 555);
            lookupForEV3Code.put(RightHighStrikePrepare, 559);
            lookupForEV3Code.put(RightHighStrike, 599);

            lookupForEV3Code.put(LeftHighStrikePrepare, 955);
            lookupForEV3Code.put(LeftHighStrike, 995);

            lookupForEV3Code.put(BackToStand, 444);
            lookupForEV3Code.put(FullStandOut, 404);

            lookupForEV3Code.put(LowToHighStand, 222);

            lookupForEV3Code.put(RHook, 911);
            lookupForEV3Code.put(RHookOut, 910);
            lookupForEV3Code.put(LHook, 119);
            lookupForEV3Code.put(LHookOut, 109);

            lookupForEV3Code.put(FullHandsOut, 111);
            lookupForEV3Code.put(Dance, 123);

            lookupForEV3Code.put(Security, 9999);

            lookupForEV3Code.put(TransformerUp, 10101);
            lookupForEV3Code.put(TransformerDown, 10100);

        }

        public int ev3Code(){
            return lookupForEV3Code.get(this);
        }

        public static StepIntent findByShortIntent(String shortI){
            return lookupByShortIntent.get(shortI);
        }

        public String getFullIntentName() {
            return "com.codegemz.elfi.coreapp.api.movement." + name();
        }
        public String getShortIntentName() {
            return ".movement." + name();
        }

    };
    public enum StepValue1{
        Up, Down, Left, Right
    };
}
