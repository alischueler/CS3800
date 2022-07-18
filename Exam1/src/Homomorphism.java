import java.util.*;

public class Homomorphism {
    static NFA NFAone;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String all = scan.nextLine();
        String[] spl = all.split(" ");
        String fileName = spl[0];
        LinkedHashMap<String, String> oldToNew = new LinkedHashMap<>();
        String[] maps = Arrays.copyOfRange(spl, 1, spl.length);
        for (int i = 0; i < maps.length;i+=2) {
            oldToNew.put(maps[i], maps[i+1]);
        }

        xml parserOne = new xml();
        List<String> statesOne = parserOne.callParse(fileName, "states");
        List<String> transitionsOne = parserOne.callParse(fileName, "transitions");
        Map<String, String> idNameOne = parserOne.idName;
        NFAone = new NFAMaker(statesOne, transitionsOne, idNameOne);
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> newDelta = NFAone.updateSymbols(oldToNew);
        List<String> newDL = convertToString(newDelta);
        NFA nfaTwo = new NFAMaker(statesOne, newDL, idNameOne);
        NFAXML NFAXML = new NFAXML();
        List<String> output = NFAXML.createXML(nfaTwo, true);
        for (String s : output) {
            System.out.println(s);
        }
    }

    private static List<String> convertToString(LinkedHashMap<String, LinkedHashMap<String,
            LinkedHashSet<String>>> newDelta) {
        List<String> toRet = new ArrayList<>();
        LinkedHashSet<String> symbols = new LinkedHashSet<>();
        Iterator<Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>>> i = newDelta.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>> mapElement = i.next();
            String currState = mapElement.getKey();
            LinkedHashMap<String, LinkedHashSet<String>> inner = mapElement.getValue();
            Iterator<Map.Entry<String, LinkedHashSet<String>>> innerI = inner.entrySet().iterator();
            while (innerI.hasNext()) {
                Map.Entry<String, LinkedHashSet<String>> innerVals = innerI.next();
                String sym = innerVals.getKey();
                LinkedHashSet<String> toStates = innerVals.getValue();
                for (String s : toStates) {
                    StringBuilder indiv = new StringBuilder();
                    indiv.append(currState);
                    indiv.append(" ");
                    indiv.append(s);
                    indiv.append(" ");
                    indiv.append(sym);
                    symbols.add(sym);
                    toRet.add(indiv.toString());
                }
            }
        }
        StringBuilder syms = new StringBuilder();
        for (String s : symbols) {
            syms.append(s);
            syms.append(" ");
        }
        toRet.add(syms.toString());
        return toRet;
    }

}
