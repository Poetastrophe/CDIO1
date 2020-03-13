package dal;

import dto.UserDTO;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
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
            UserDTO user1 = new UserDTO(0,"Admin","Ad","0123456789","password","1");
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
        UserDTO user = new UserDTO();
        try {
            PreparedStatement ps = _connection.prepareStatement("SELECT * FROM Users WHERE UserID=?");
            ps.setInt(1, userID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt("UserID"));
                user.setUserName(resultSet.getString("UserName"));
                user.setIni(resultSet.getString("Ini"));
                user.setUserCpr(resultSet.getString("cpr"));
                user.setPassword(resultSet.getString("Password"));
                user.addRole(resultSet.getString("Role"));
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
                list.add(new UserDTO());
                list.get(list.size() - 1).setUserId(resultSet.getInt("UserID"));
                list.get(list.size() - 1).setUserName(resultSet.getString("UserName"));
                list.get(list.size() - 1).setIni(resultSet.getString("Ini"));
                list.get(list.size() - 1).setUserCpr(resultSet.getString("cpr"));
                list.get(list.size() - 1).setPassword(resultSet.getString("Password"));
                list.get(list.size() - 1).addRole(resultSet.getString("Role"));
            }
        } catch (SQLException e) {
            throw new DALException("Could not get user list");
        }
        closeConnection();
        return list;
    }

    @Override
    public void createUser(UserDTO user) throws DALException {
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
                ps.setString(6, user.getRoles().get(0));
            } catch (IndexOutOfBoundsException e){
                ps.setString(6,null);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DALException("Cannot create new user. Check for unique ID");
        }

        closeConnection();

    }

    @Override
    public void updateUser(UserDTO newUser) throws DALException {
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
    public void updateUser(UserDTO user, int oldID) throws DALException {
       deleteUser(oldID);
       createUser(user);

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
}