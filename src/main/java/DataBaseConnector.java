import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {

    private static Connection con=null;


    public static void main(String[] args){
        addEntery("test2","Test2","test.com","10x102","1998","10","here","1","me");
    }

    public static void addEntery(String title ,String detail ,String link ,String area ,String buildYear ,String price ,String address ,String roomCount ,String putBy){
            try(Connection connection=getConnection()){
                try(CallableStatement st=con.prepareCall("INSERT INTO chabaharAdds(title,detail,link,area,buildYear,price,address,roomCount,putBy) VALUES (?,?,?,?,?,?,?,?,?)")){
                    st.setString(1,title);
                    st.setString(2,detail);
                    st.setString(3,link);
                    st.setString(4,area);
                    st.setString(5,buildYear);
                    st.setString(6,price);
                    st.setString(7,address);
                    st.setString(8,roomCount);
                    st.setString(9,putBy);
                    st.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        System.out.println("added to data base");
    }

    private static Connection getConnection() throws SQLException {
       if(con==null){
           con= DriverManager.getConnection(
                   "jdbc:mysql://localhost/searchengine?" +
                           "user=root&password=\"insertPasswordForDataBase\"&useSSL=false&allowPublicKeyRetrieval=true");
       }
       return con;
    }


}
