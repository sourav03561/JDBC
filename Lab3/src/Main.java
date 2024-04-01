import java.sql.*;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

        public static void main(String[] args) {

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String url = "jdbc:postgresql://localhost/postgres";
            String user = "postgres";
            String pass = "Naren@03561";
            Connection connexion = null;
            try {
                connexion = DriverManager.getConnection(url, user, pass);

                /* Requests to bdd will be here */
                System.out.println("Bdd Connected");
                /*displayDepartment(connexion);*/
            /*Scanner scanner = new Scanner(System.in);
            System.out.print("Enter employee number: ");
            int empno = scanner.nextInt();
            System.out.print("Enter new department number: ");
            int newDeptno = scanner.nextInt();
            moveDepartment(connexion,empno,newDeptno);
            displayTable(connexion,"emp");*/
                PreparedStatement preparedStatement = connexion.prepareStatement(
                        "SELECT * FROM emp WHERE efirst = ? AND ename = ?");

                Scanner sc = new Scanner(System.in);
                String firstName = sc.next();
                preparedStatement.setString(1, firstName);
                String lastName = sc.next();
                preparedStatement.setString(2, lastName);

                ResultSet results = preparedStatement.executeQuery();
                System.out.println(results);
                while (results.next()) {
                    int empId = results.getInt("empno");
                    String job = results.getString("job");
                    int sal = results.getInt("sal");
                    // You can continue retrieving other columns as needed

                    // Print the values
                    System.out.println("Employee ID: " + empId);
                    System.out.println("Job: " + job);
                    System.out.println("Sal: " + sal);
                }
                }catch(SQLException e ){
                    e.printStackTrace();
                } finally{
                    if (connexion != null)
                        try {
                            connexion.close();
                        } catch (SQLException ignore) {
                            ignore.printStackTrace();
                        }
                }

            }
    public static void displayDepartment(Connection connexion) throws SQLException {
        Statement statement = connexion.createStatement();
        ResultSet resultat = statement.
                executeQuery("SELECT deptno, dname,loc FROM dept");

        while (resultat.next()) {
            int deptno = resultat.getInt("deptno");
            String dname = resultat.getString("dname");
            String loc = resultat.getString("loc");
            System.out.println("Department " + deptno + " is for "
                    + dname + " and located in ? ");
            System.out.println(loc);
        }
        resultat.close();
    }
    public static void moveDepartment(Connection connexion,int empno, int newDeptno) throws SQLException {
        String sql = "UPDATE emp SET deptno = ? WHERE empno = ?";
        PreparedStatement pstmt = connexion.prepareStatement(sql);
        pstmt.setInt(1, newDeptno);
        pstmt.setInt(2, empno);
        int rowsUpdated = pstmt.executeUpdate();
        Statement statement = connexion.createStatement();
        if (rowsUpdated > 0) {
            System.out.println("Employee department moved successfully!");
        } else {
            System.out.println("Failed to move employee department!");
        }
    }
    public static void displayTable(Connection connexion,String tableName) throws SQLException {
        String columnQuery = "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = ?";
        String dataQuery = "SELECT * FROM " + tableName;

        try (PreparedStatement columnStatement = connexion.prepareStatement(columnQuery);
             PreparedStatement dataStatement = connexion.prepareStatement(dataQuery)) {

            // Get column names
            columnStatement.setString(1, tableName);
            try (ResultSet columnResult = columnStatement.executeQuery()) {
                while (columnResult.next()) {
                    String columnName = columnResult.getString("column_name");
                    System.out.print(columnName + '\t'+"|");
                }
                System.out.println(); // Move to the next line after printing column names
            }

            // Get data and display
            try (ResultSet dataResult = dataStatement.executeQuery()) {
                while (dataResult.next()) {
                    // Example: Assuming all columns are of type String for simplicity
                    for (int i = 1; i <= dataResult.getMetaData().getColumnCount(); i++) {
                        System.out.print(dataResult.getString(i) + '\t'+"|");
                    }
                    System.out.println(); // Move to the next line after printing each row
                }
            }
        }
    }

}