package server;

import org.xbill.DNS.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class AnswerCreator {

//    public static final String question = "902#0#00#2#www.facebook.com#CNAME#IN#outlook.com#MX#IN!";


    static String createAnswer(Connection connection, String question) throws TextParseException, SQLException {

        StringBuilder answer = new StringBuilder();
        String addressString;

        String[] fields = question.split("#");
        
        answer.append(fields[0]);
        answer.append("#1#00#");
        answer.append(fields[3]);
        answer.append("#");
        fields[fields.length - 1] = fields[fields.length - 1].replace("!", "");
        for (String w: fields){
            System.out.println(w);
        }

        Integer questionCount = Integer.valueOf(fields[3]);
        System.out.println(questionCount);

        int k = 0, i = questionCount, type = Type.A, dclass = DClass.IN;
        loop: while ( i > 0){
            --i;

            switch (fields[5 + k*3]){
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
                    answer.replace(fields[0].length()+ 3,fields[0].length()+ 5, "11");
                    answer.delete(fields[0].length()+ 5,answer.length());
                    break loop;
            }

            switch (fields[6 + k*3]){
                case "IN":
                    dclass = DClass.IN;
                    break;
                case "CH":
                    dclass = DClass.CH;
                    break;
                default:
                    answer.replace(fields[0].length()+ 3,fields[0].length()+ 5, "11");
                    answer.delete(fields[0].length()+ 5,answer.length());
                    break loop;
            }

            System.out.println(fields[4 + k*3]);

            // check record in DB
            PreparedStatement statement = connection.prepareStatement("SELECT domain_name, record_type, class, address \n" +
                    "FROM record\n" +
                    "WHERE domain_name = ? \n" +
                    "AND record_type = ? " +
                    "AND class = ?");

            statement.setString(1, fields[4 + k*3].concat("."));
            statement.setInt(2, type);
            statement.setString(3, fields[6 + k*3]);

            System.out.println(statement);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Found: ");
                addressString = resultSet.getString("address");
                System.out.println(addressString);
            }

            else { // query to cloud DNS
                System.out.println("Not Found");
                Record[] records = new Lookup(fields[4 + k*3], type).run();

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
                        String host = mxRecord.getTarget().toString();

                        records = new Lookup(host, Type.A).run();
                        ARecord aRecord1 = (ARecord) records[0];

                        address = aRecord1.getAddress().toString();
                        System.out.println(address);
                        break;

                    case Type.CNAME:
                        CNAMERecord cnameRecord = (CNAMERecord) records[0];
                        String host1 = cnameRecord.getTarget().toString();

                        records = new Lookup(host1, Type.A).run();
                        ARecord aRecord2 = (ARecord) records[0];

                        address = aRecord2.getAddress().toString();
                        break ;
                }

                String[] addresses = address.split("/");
                addressString = addresses[1];
            }


            answer.append(fields[4 + k*3]);
            answer.append("#");
            answer.append(fields[5 + k*3]);
            answer.append("#");
            answer.append(fields[6 + k*3]);
            answer.append("#");
            answer.append(addressString);
            if (i != 0)
                answer.append("#");
            k++;
        }
        answer.append("!");
        System.out.println(answer.toString());
        return answer.toString();
    }

}
