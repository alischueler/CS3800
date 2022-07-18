import java.util.*;

public class Regex {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String use = scan.next();
        NFAKleene kleene = new NFAKleene();
        NFAConct concat = new NFAConct();
        NFAUnion union = new NFAUnion();
        NFA toRet;
        switch(use) {
            case "1":
                //kleene zero
                NFA zeroKleene = kleene.kleene(recognizes0());
                //System.out.println(zeroKleene.getDelta() + " start "+zeroKleene.getStart()+ " accept "+zeroKleene.getAccept());
                //concat kleene zero with just 1
                NFA oneTwoConcat = concat.concat(zeroKleene, recognizes1());
                //System.out.println(oneTwoConcat.getDelta()+" start "+oneTwoConcat.getStart()+ " accept "+oneTwoConcat.getAccept());
                //concat above with kleene zero
                NFA oneTwoOneConcat = concat.concat(oneTwoConcat, kleene.kleene(recognizes0()));
                //System.out.println(oneTwoOneConcat.getDelta()+" start "+oneTwoOneConcat.getStart()+ " accept "+oneTwoOneConcat.getAccept());
                toRet = oneTwoOneConcat;
                break;
            case "2":
                NFA oneUnionTwoOne = union.union(recognizes0(), recognizes1());
                NFA kleeneAlphaOne = kleene.kleene(oneUnionTwoOne);

                NFA oneUnionTwoTwo = union.union(recognizes0(), recognizes1());
                NFA kleeneAlphaTwo = kleene.kleene(oneUnionTwoTwo);
                //union zero, one
                //kleene union of zero, one
                //concat of 0 and 0
                //concat or 00 and 1
                //concat of kleene (union of zero, one) and 001
                NFA concatFirstTwo = concat.concat(kleeneAlphaOne, recognizes001());
                //concat of above and kleene (union of zero, one)
                toRet = concat.concat(concatFirstTwo, kleeneAlphaTwo);
                break;
            case "3":
                //kleene of one
                NFA oneKleene = kleene.kleene(recognizes1());
                //kleene of zero and at least one one
                NFA zeroAtLeastOneKleene = kleene.kleene(recognizes01Plus());
                //concat of one and two
                toRet = concat.concat(oneKleene, zeroAtLeastOneKleene);
                break;
            case "4":
                //union of zero and one
                //concat of zero and one and zero and one
                //concat of above and zero and one
                //kleene of above
                NFA oneUnionTwoA = union.union(recognizes0(), recognizes1());

                NFA oneUnionTwoTwoB = union.union(recognizes0(), recognizes1());

                NFA oneUnionTwoTwoC = union.union(recognizes0(), recognizes1());

                //concat NFA accepting any string any amount of times X3 kleene
                NFA concatOne = concat.concat(oneUnionTwoA, oneUnionTwoTwoB);
                NFA concattwo = concat.concat(concatOne, oneUnionTwoTwoC);
                toRet = kleene.kleene(concattwo);
                break;
            case "5":
                //kleene of zero union one

                NFA oneUnionTwoZ = union.union(recognizes0(), recognizes1());
                NFA kleeneAlphaZ = kleene.kleene(oneUnionTwoZ);
                //concat of zero and above
                NFA concatZeroAlpha = concat.concat(recognizes0(), kleeneAlphaZ);
                //concat of above and zero
                NFA concatAboveZero = concat.concat(concatZeroAlpha, recognizes0());

                //kleene of zero union one
                NFA oneUnionTwoX = union.union(recognizes0(), recognizes1());
                NFA kleeneAlphaX = kleene.kleene(oneUnionTwoX);
                //concat of one and above
                NFA concatOneAlpha = concat.concat(recognizes1(), kleeneAlphaX);
                //concat of above and one
                NFA concatAboveOne = concat.concat(concatOneAlpha, recognizes1());

                //top union bottom
                NFA unionOneTwo = union.union(concatAboveZero, concatAboveOne);
                //above union zero
                NFA unionAboveZero = union.union(unionOneTwo, recognizes0());
                //above union one
                toRet = union.union(unionAboveZero, recognizes1());
                break;
            case "6":
                //kleene 1
                NFA kleeneOne = kleene.kleene(recognizes1());
                //union zero and empty
                NFA unionFirst = union.union(recognizes0(), recognizesEmpty());
                //concat of above and kleene one
                toRet = concat.concat(unionFirst, kleeneOne);

                break;
            case "7":
                //zero union empty
                NFA zeroUnionEmpty = union.union(recognizes0(), recognizesEmpty());
                //one union empty
                NFA oneUnionEmpty = union.union(recognizes1(), recognizesEmpty());
                //concat one and two
                toRet = concat.concat(zeroUnionEmpty, oneUnionEmpty);

                break;
            case "8":
                //kleene one
                NFA onekleene = kleene.kleene(recognizes1());
                //concat kleene one and none
                toRet = concat.concat(onekleene, recognizesNone());
                break;
            default: toRet = new NFAMaker();
            break;
        }
        NFAXML xml = new NFAXML();
        List<String> toPrint = xml.createXML(toRet, true);
        for (String s : toPrint) {
            System.out.println(s);
        }
    }

    public static Map<String, String> makeIdName(String states) {
        Map<String, String> idName = new LinkedHashMap<String, String>();
        String[] statenames = states.split(" ");
        for (String s : statenames) {
            idName.put(s, s);
        }
        return idName;
    }

    public static String[] makeTrans(String transitions) {
        String[] toRet;
        if (transitions.contains(",")) {
            String[] transitionsarr = transitions.split(", ");
            toRet = new String[transitionsarr.length];
            for(int i = 0; i < transitionsarr.length; i++) {
                toRet[i] = transitionsarr[i];
            }
        }
        else {
            toRet = new String[1];
            toRet[0] = transitions;
        }
        return toRet;
    }

    public static List<String> addToList(String[] trans) {
        List<String> toRet = new ArrayList<>();
        for (int i= 0; i<trans.length;i++) {
            toRet.add(trans[i]);
        }
        return toRet;
    }

    public static NFA recognizes0() {
        NFA toRet;
        String oneStates = "q0 q1";
        String oneAccept = "q1";
        String oneStart = "q0";
        List<String> oneAllStates = new ArrayList<>();
        oneAllStates.add(oneStates);
        oneAllStates.add(oneStart);
        oneAllStates.add(oneAccept);
        String[] stringTransOne = makeTrans("q0 q1 0, 0 1");
        List<String> transitionsOne = addToList(stringTransOne);
        Map<String, String> idNameOne = makeIdName(oneStates);
        toRet = new NFAMaker(oneAllStates, transitionsOne, idNameOne);
        return toRet;
    }

    public static NFA recognizes1() {
        String twoStates = "q0 q1";
        String twoAccept = "q1";
        String twoStart = "q0";
        List<String> twoAllStates = new ArrayList<>();
        twoAllStates.add(twoStates);
        twoAllStates.add(twoStart);
        twoAllStates.add(twoAccept);
        String[] stringTransTwo = makeTrans("q0 q1 1, 0 1");
        List<String> transitionsTwo = addToList(stringTransTwo);
        Map<String, String> idNameTwo = makeIdName(twoStates);
        NFA two = new NFAMaker(twoAllStates, transitionsTwo, idNameTwo);
        return two;
    }

    public static NFA recognizes001() {
        String twoStates = "q0 q1 q2 q3";
        String twoAccept = "q3";
        String twoStart = "q0";
        List<String> twoAllStates = new ArrayList<>();
        twoAllStates.add(twoStates);
        twoAllStates.add(twoStart);
        twoAllStates.add(twoAccept);
        String[] stringTransTwo = makeTrans("q0 q1 0, q1 q2 0, q2 q3 1, 0 1");
        List<String> transitionsTwo = addToList(stringTransTwo);
        Map<String, String> idNameTwo = makeIdName(twoStates);
        NFA two = new NFAMaker(twoAllStates, transitionsTwo, idNameTwo);
        return two;
    }

    public static NFA recognizesNone() {
        String twoStates = "q0";
        String twoAccept = "";
        String twoStart = "q0";
        List<String> twoAllStates = new ArrayList<>();
        twoAllStates.add(twoStates);
        twoAllStates.add(twoStart);
        twoAllStates.add(twoAccept);
        String[] stringTransTwo = makeTrans("0 1");
        List<String> transitionsTwo = addToList(stringTransTwo);
        Map<String, String> idNameTwo = makeIdName(twoStates);
        NFA two = new NFAMaker(twoAllStates, transitionsTwo, idNameTwo);
        return two;
    }

    public static NFA recognizesEmpty() {
        String twoStates = "q0 q1";
        String twoAccept = "q1";
        String twoStart = "q0";
        List<String> twoAllStates = new ArrayList<>();
        twoAllStates.add(twoStates);
        twoAllStates.add(twoStart);
        twoAllStates.add(twoAccept);
        String[] stringTransTwo = makeTrans("q0 q1 empty, 0 1");
        List<String> transitionsTwo = addToList(stringTransTwo);
        Map<String, String> idNameTwo = makeIdName(twoStates);
        NFA two = new NFAMaker(twoAllStates, transitionsTwo, idNameTwo);
        return two;

    }

    public static NFA recognizes01Plus() {
        String twoStates = "q0 q1 q2";
        String twoAccept = "q2";
        String twoStart = "q0";
        List<String> twoAllStates = new ArrayList<>();
        twoAllStates.add(twoStates);
        twoAllStates.add(twoStart);
        twoAllStates.add(twoAccept);
        String[] stringTransTwo = makeTrans("q0 q1 0, q1 q2 1, q2 q2 1, 0 1");
        List<String> transitionsTwo = addToList(stringTransTwo);
        Map<String, String> idNameTwo = makeIdName(twoStates);
        NFA two = new NFAMaker(twoAllStates, transitionsTwo, idNameTwo);
        return two;

    }

}
