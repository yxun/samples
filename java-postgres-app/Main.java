import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://" + System.getenv("DB_HOST") + ":" + System.getenv("DB_PORT") + "/" + System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        String tableName = System.getenv("TABLE_NAME");
        String sslCert = System.getenv("SSL_CERT");

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("ssl", "true");
       // props.setProperty("sslmode", "require");
        System.out.println("SSL Mode: verify-ca");
        props.setProperty("sslmode", "verify-ca");
        props.setProperty("sslrootcert", sslCert);

        try {
            while (true) {
                Connection conn = DriverManager.getConnection(url, props);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

                while (rs.next()) {
                    System.out.println(rs.getString(1) + "\t" + rs.getString(2));
                }

                rs.close();
                stmt.close();
                conn.close();
                
                // Sleep for 10 minutes before fetching data again
                Thread.sleep(600000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}