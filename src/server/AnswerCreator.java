package server;

import org.xbill.DNS.*;

public class AnswerCreator {

    public static final String question = "0#00#2#google.com.vn#A#IN#tinhte.vn#A#IN!";
    public static void main(String[] args) throws TextParseException {

        StringBuilder answer = new StringBuilder("1#00#");

        String[] fields = question.split("#");
        fields[fields.length - 1] = fields[fields.length - 1].replace("!", "");
        for (String w: fields){
            System.out.println(w);
        }

        Integer questionCount = Integer.valueOf(fields[2]);
        System.out.println(questionCount);

        int k = 0, i = questionCount;
        while ( i > 0){
            --i;
            int type = Type.A;
            switch (fields[4 + k*3]){
                case "A":
                    type = Type.A;
                    System.out.println("type A");
                    break;
                case "AAAA":
                    type = Type.AAAA;
                    break;
                case "CNAME":
                    type = Type.CNAME;
                    break;
                case "MX":
                    type = Type.MX;
                    break;
                default:
                    answer.replace(2,3, "11");
                    answer.delete(5,answer.length()-1);
                    break;
            }

            int dclass;
            switch (fields[5 + k*3]){
                case "IN":
                    dclass = DClass.IN;
                    break;
                case "CH":
                    dclass = DClass.CH;
                    break;
                default:
                    answer.replace(2,3, "11");
                    answer.delete(5,answer.length()-1);
                    break;
            }

            System.out.println(fields[3 + k*3]);
            Record[] records = new Lookup(fields[3 + k*3], type).run();

            answer.append(fields[2]);
            answer.append("#");

            String address = null;
            switch (type){
                case Type.A:
                        ARecord aRecord = (ARecord) records[0];
                        System.out.println("Host " + aRecord.getName() + " address " + aRecord.getAddress());
                        address = aRecord.getAddress().toString();
                    break;

                case Type.AAAA:
                    AAAARecord aaaaRecord = (AAAARecord) records[0];
                    address = aaaaRecord.getAddress().toString();
                    break;

                case Type.MX:
                    MXRecord mxRecord = (MXRecord) records[0];
                    address = mxRecord.getTarget().toString();
                    System.out.println(address);
                default:
                    break;
            }

            String[] addresses = address.split("/");
            answer.append(fields[3 + k*3]);
            answer.append("#");
            answer.append(fields[4 + k*3]);
            answer.append("#");
            answer.append(fields[5 + k*3]);
            answer.append("#");
            answer.append(addresses[1]);
            k++;
        }
        answer.append("!");
        System.out.println(answer.toString());
    }

}
