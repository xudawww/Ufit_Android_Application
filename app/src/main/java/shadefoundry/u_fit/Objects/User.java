package shadefoundry.u_fit.Objects;

import java.io.Serializable;
import java.util.ArrayList;

import shadefoundry.u_fit.Objects.Routine;

public class User implements Serializable {

    private String userName,firstName,lastName,email,userID;


    public User(String id, String userName, String key, String firstName, String lastName, String email, ArrayList<Routine> routines) {
        this.userID = id;
        this.userName = userName;

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }




    //getters and setters

    public String getId() {
        return userID;
    }

    public void setId(String id) {
        this. userID = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
