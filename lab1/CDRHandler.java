import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CDRHandler {

    public static void main(String[] args) {
        String csvData = args[0];
        String targetNumber = args[1];
        int outgoingCallFactor = Integer.parseInt(args[2]);
        int incomingCallFactor = Integer.parseInt(args[3]);
        int smsFactor = Integer.parseInt(args[4]);
        double outgoingCallsDurationMin = 0;
        double incomingCallsDurationMin = 0;
        int smsSent = 0;
        double freeOutgoingCallsDurationMin = 0;
        int userFee = 0;

        BufferedReader br = null;
        String line = "";
        String csvSeparator = ",";

        List<String> stringList = new ArrayList<String>();
        boolean isFirstLine = true;

        try {
            br = new BufferedReader(new FileReader(csvData));

            while ((line = br.readLine()) != null) {
                String[] lineMod = line.split(csvSeparator);
                String matchingRecord = null;

                if (isFirstLine) {
                    stringList.add(lineMod[0] + " " + lineMod[1] + " " + lineMod[2] + " " + lineMod[3] + " " + lineMod[4]);
                    isFirstLine = false;
                }

                if (lineMod[1].equals(targetNumber)) {
                    outgoingCallsDurationMin += Double.parseDouble(lineMod[3]);
                    smsSent += Integer.parseInt(lineMod[4]);
                    matchingRecord = lineMod[0] + " [" + lineMod[1] + "] " + lineMod[2] + " " + lineMod[3] + " " + lineMod[4];
                }

                if (lineMod[2].equals(targetNumber)) {
                    incomingCallsDurationMin += Double.parseDouble(lineMod[3]);
                    matchingRecord = lineMod[0] + " " + lineMod[1] + " [" + lineMod[2] + "] " + lineMod[3] + " " + lineMod[4];
                }

                if (matchingRecord != null)
                    stringList.add(matchingRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        freeOutgoingCallsDurationMin = 20;
        if (outgoingCallsDurationMin - freeOutgoingCallsDurationMin < 0)
            outgoingCallsDurationMin = 0;

        userFee = (int) (( Math.ceil(outgoingCallsDurationMin) * outgoingCallFactor )
                        + ( Math.ceil(incomingCallsDurationMin) * incomingCallFactor )
                        + ( smsSent * smsFactor ));

        System.out.println("User's [" + targetNumber + "] fee is " + userFee + " RUB in " + csvData);
        System.out.println("Found records:");
        for (String s : stringList) {
            System.out.println(s);
        }
    }
}
