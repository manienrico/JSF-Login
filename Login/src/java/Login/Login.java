package Login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Enrico
 */
@ManagedBean
@SessionScoped
@RequestScoped
public class Login {
    private int id;
    private String name;
    private String username;
    private String gender;
    private String password;
    private String message;
    Connection connection;
    ArrayList usersList;
    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    
    
    public Login(){
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    //Establish db connection
    public Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root","");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        return connection;
    }
    
    //Fetch records
    public ArrayList usersList(){
        try{
            usersList = new ArrayList();
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from users");
            while(rs.next()){
                Login user = new Login();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setGender(rs.getString("gender"));
                user.setPassword(rs.getString("password"));
                usersList.add(user);
            }
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        return usersList;
    }
    
    //Register user
    public String save(){
        int result = 0;
        try{
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into users(name,username,gender,password) values (?,?,?,?)");
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, gender);
            stmt.setString(4, password);
            result = stmt.executeUpdate();
            connection.close();
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        
        if(result > 0){
            return "index.xhtml?faces-redirect=true";
        }else{
            return "index.xhtml?faces-redirect=true";
        }
    }
    
    //Login
//    public String findUserByUsernameAndPassword(String username,String password){
//        try{
//            connection = getConnection();
//            Statement stmt = getConnection().createStatement();
//            ResultSet rs = stmt.executeQuery("select * from users where username ='"+username+"' and password ='"+password+"'");
//            
//            if(rs != null){
//                return "home.xhtml";
//            }else{
//                return null;
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//            System.out.println(e);
//        }
//        return signin();
//    }
    public String signin() throws SQLException{
        
        connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select count(*) from users where username=? and password=?");
        pstmt.setString(1,username);
        pstmt.setString(2,password);
            
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            int count = rs.getInt(1);
            if(count >0){
                return "home.xhtml?faces-redirect=true";
            }
            return "home.xhtml?faces-redirect=true";
        }else{
            return "index.xhtml?faces-redirect=true";
        }
    }
    
    public String logout(){
        return "index.xhtml?faces-redirect=true";
    }
    
    
    //Get gender string
    public String getGenderName(char gender){
        if(gender == 'M'){
            return "Male";
        }else{
            return "Female";
        }
    }

    
    
    
}
