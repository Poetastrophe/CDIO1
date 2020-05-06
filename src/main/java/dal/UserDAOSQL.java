package dal;

import dto.UserDTO;
import func.IFunc;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDAOSQL implements IUserDAO {
    //Private final attributes
    private final String _DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String _END = "?characterEncoding=latin1&serverTimezone=UTC";

    //Changing attributes
    private String _url;
    private Connection _connection;
    private Statement _statement;

    //Optional
    private String _username = "root";
    private String _password = "password";

    /**
     * Uses a database to
     *
     * @param host     Address of the host
     * @param port     Port of the SQL database
     * @param database Name of database
     */
    public UserDAOSQL(String host, String port, String database, String username, String password) {
        this._url = "jdbc:mysql://" + host + ":" + port + "/" + database + _END;
        this._username = username;
        this._password = password;
    }

    /**
     * private function for opening a connection to the server
     * @throws DALException Throws a DALException if: Class.for(driver) fails
     */
    private void openConnection() throws DALException {
        try {
            Class.forName(_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new DALException("openConnection Class not found");
        }

        try {
            _connection = DriverManager.getConnection(_url, _username, _password);
            _statement = _connection.createStatement();
        } catch (SQLException e) {
            createDummyDatabase();
        }
    }

    /**
     * If there is no database create a dummy database. Purely for showing a database working
     * @throws DALException Throws DALException
     */
    private void createDummyDatabase() throws DALException {
        System.out.println("Creating dummy database");

        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            throw new DALException("could not create driver");
        }

        //Getting the connection
        String mysqlUrl = "jdbc:mysql://localhost/" + _END;
        try {
            _connection = DriverManager.getConnection(mysqlUrl,_username,_password);
            _statement = _connection.createStatement();
        } catch (SQLException ex) {
            throw new DALException("Could not create connection, try another username and password");
        }
        System.out.println("Connection established......");
        //Initialize the script runner
        ScriptRunner sr = new ScriptRunner(_connection);
        //Creating a reader object
        URL res = getClass().getClassLoader().getResource("User_Database2.sql");
        File file;
        try {
            file = Paths.get(res.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new DALException("URI invalid");
        }
        String absolutePath = file.getAbsolutePath();
        Reader reader;
        try {
            reader = new BufferedReader(new FileReader(absolutePath));
        } catch (FileNotFoundException ex) {
            throw new DALException("could not create reader");
        }
        //Running the script
        sr.runScript(reader);
        _url = "jdbc:mysql://localhost:3306/User_Database" + _END;
        try{
            UserDTO user1 = new UserDTO(0,"Admin","Ad","0123456789","password",Arrays.asList("1"));
            createUser(user1);
        } catch (Exception ignored){}
        try {
            openConnection();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void closeConnection() throws DALException {
        try {
            _statement.close();
            _connection.close();
        } catch (SQLException e) {
            throw new DALException("Cannot close connection to database");
        }
    }

    @Override
    public UserDTO getUser(int userID) throws DALException {
        openConnection();
        // It will throw an exception if it stays null.
        UserDTO user = null;
        try {
            PreparedStatement ps = _connection.prepareStatement("SELECT * FROM Users WHERE UserID=?");
            ps.setInt(1, userID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String userName = resultSet.getString("UserName");
                String ini = resultSet.getString("Ini");
                String cpr = resultSet.getString("cpr");
                String password = resultSet.getString("Password");
                String role = resultSet.getString("Role");
                //TODO: Make sure that multiple roles can be added, how it is saved in the database, sincerely Christoffer.
                user = new UserDTO(userID,userName,ini,cpr,password, Arrays.asList(role));
            }
        } catch (Exception e) {
            throw new DALException("Cannot get user");
        }

        closeConnection();
        return user;
    }

    @Override
    public List<UserDTO> getUserList() throws DALException {
        openConnection();
        ArrayList<UserDTO> list = new ArrayList<>();
        try {
            ResultSet resultSet = _statement.executeQuery("SELECT * FROM Users");
            while (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String userName = resultSet.getString("UserName");
                String ini = resultSet.getString("Ini");
                String cpr = resultSet.getString("cpr");
                String password = resultSet.getString("Password");
                String role = resultSet.getString("Role");

                UserDTO tmpUser = new UserDTO(userID,userName,ini,cpr,password, Arrays.asList(role));

                list.add(tmpUser);
            }
        } catch (SQLException e) {
            throw new DALException("Could not get user list");
        }
        closeConnection();
        return list;
    }

    @Override
    public void createUser(UserDTO user) throws UserFormatException, DALException {
        // Check parameters
        int userID = user.getUserId();
        String userName = user.getUserName();
        String cpr = user.getCpr();
        List<String> roles = user.getRoles();

        List<IUserDAO.UserFormatException.errortypes> errorlist = new ArrayList<>();
        // Check ID
        if(!(11<=userID && userID<=99)){
            errorlist.add(IUserDAO.UserFormatException.errortypes.ID);
        }

        // Check username
        if(!(2<=userName.length() && userName.length()<=20)){
            errorlist.add(IUserDAO.UserFormatException.errortypes.username);
        }

        // Check CPR
        //boolean isInteger=true;
        //String hyphen = "";
        //try{
        //    int test = Integer.parseInt(cpr.substring(0,6));
        //    test = Integer.parseInt(cpr.substring(7,11));
        //    hyphen = cpr.substring(6,7);
        //}catch(NumberFormatException | StringIndexOutOfBoundsException e){
        //    isInteger=false;
        //    hyphen = "";
        //}
        //if(!isInteger || !hyphen.equals("-")){
        //    errorlist.add(IUserDAO.UserFormatException.errortypes.CPR);
        //}
        //System.out.println(user.getCpr());
        // Check roles
        List<String> cpyRoles = new ArrayList<>(roles);
        cpyRoles.remove(IUserDAO.RoleNames.ADMIN);
        cpyRoles.remove(IUserDAO.RoleNames.FORMAND);
        cpyRoles.remove(IUserDAO.RoleNames.OPERATOR);
        cpyRoles.remove(IUserDAO.RoleNames.FARMACEUT);
        if(cpyRoles.size()!=0){
            errorlist.add(IUserDAO.UserFormatException.errortypes.roles);
        }
        //TODO: Check password

        if(errorlist.size() > 0) {
            throw new IUserDAO.UserFormatException("One or more user parameters are not correctly formatted",errorlist);
        }

        openConnection();
        try {
            PreparedStatement ps =
                    _connection.prepareStatement(
                            "INSERT INTO users(UserID,UserName,Ini,cpr,Password,Role) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getIni());
            ps.setString(4, user.getCpr());
            ps.setString(5, user.getPassword());
            try {
                //ps.setString(6, user.getRoles().get(0));
                ps.setString(6, "1");
            } catch (IndexOutOfBoundsException e){
                ps.setString(6,null);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Cannot create new user. Check for unique ID");
        }

        closeConnection();

    }

    @Override
    public void updateUser(UserDTO newUser) throws UserFormatException,DALException {
        for (UserDTO user : getUserList())
        {
            if (user.getUserId() == newUser.getUserId())
            {
                deleteUser(user.getUserId());
                createUser(newUser);
                return;
            }
        }
        throw new DALException("The user you tried to update didn't exist");

    }

    @Override
    public void deleteUser(int userId) throws DALException {
        openConnection();
        try {
            PreparedStatement statement = _connection.prepareStatement("DELETE FROM users WHERE UserID =?");
            statement.setInt(1,userId);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
           throw new DALException("Cant delete user with ID = " + userId + ", Check ID");
        }
        closeConnection();
    }
    // Made by us during our database project in 02327
    //First element in pair: Header coulumn,
    // Second element:  rows
    public Pair<List<String>, List<List<String>>> getResultOfQuery(String query) throws DALException {
        openConnection();
        List<List<String>> rows = new ArrayList<List<String>>();
        List<String> header = new ArrayList<String>();

        try {
            ResultSet resultSet = _statement.executeQuery(query);

            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i) {
                header.add(resultSet.getMetaData().getColumnName(i));
            }
            while (resultSet.next()) {
                rows.add(new ArrayList<String>());
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i) {
                    rows.get(rows.size() - 1).add(String.valueOf(resultSet.getString(i)));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Could not fulfill query");
        }
        closeConnection();

        return new Pair<List<String>, List<List<String>>>(header, rows);
    }

    public class Pair<L,R>{
        public L left;
        public R right;
        Pair(L left, R right){
            this.left = left;
            this.right = right;
        }
    }

}