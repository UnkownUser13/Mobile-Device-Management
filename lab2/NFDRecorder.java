import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NFDRecorder {

    public static void main(String[] args) {
        String csvData = args[0];
        String targetIP = args[1];
        int trafficFactorRUBPerMbyte = Integer.parseInt(args[2]);
        int outgoingTrafficVolumeBytes = 0;
        int userFee = 0;

        BufferedReader br = null;
        String line = null;
        String csvSeparator = ",";

        List<String> reportList = new ArrayList<String>();
        boolean isFirstLine = true;

        try {
            br = new BufferedReader(new FileReader(csvData));
            FileWriter fileWriter = new FileWriter("graph.csv");

            while ((line = br.readLine()) != null) {
                String[] lineMod = line.split(csvSeparator);
                String matchingRecord = null;

                if (isFirstLine) {
                    reportList.add(lineMod[0] + " " + lineMod[1] + " " + lineMod[2] + " " + lineMod[3]
                            + " " + lineMod[4] + " " + lineMod[5] + " " + lineMod[6] + " " + lineMod[7]);
                    isFirstLine = false;
                }

                if (lineMod[3].contains(targetIP)) {
                    outgoingTrafficVolumeBytes += Integer.parseInt(lineMod[6]);
                    matchingRecord = lineMod[0] + " " + lineMod[1] + " " + lineMod[2] + " " + lineMod[3]
                            + " " + lineMod[4] + " " + lineMod[5] + " " + lineMod[6] + " " + lineMod[7];

                    fileWriter.write(lineMod[0]);
                    fileWriter.append(",");
                    fileWriter.write(lineMod[6]);
                    fileWriter.append("\n");
                }

                if (matchingRecord != null)
                    reportList.add(matchingRecord);
            }

            fileWriter.flush();
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

        userFee = (int) Math.ceil((double) outgoingTrafficVolumeBytes / 1024) * trafficFactorRUBPerMbyte;

        System.out.println("Found records:");
        for (String s : reportList) {
            System.out.println(s);
        }

        System.out.println("User's [" + targetIP + "] fee is " + userFee + " RUB in " + csvData);
    }
}
