package com.flight.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.flight.dao.UserDAO;
import com.flight.model.ReservationDetails;
import com.flight.model.User;

import org.mindrot.jbcrypt.BCrypt;


public class UserService 
{
    
    UserDAO userDAO;
    private User currentUser;

    public UserService(Connection conn) 
    {
        userDAO = new UserDAO(conn);
    }


    public boolean SignInUser(User user)
    {
        try 
        {
            if (!(userDAO.findIfExist(user.getEmail()))) // if no user with the same Email exist
            {
                user.setUserType("User"); // Default user type
                System.out.println("\n user type at inscription is : '"+user.getUserType()+"'\n");
                userDAO.insert(user);
                return true;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    public boolean SignInAdmin(User user)
    {
        try 
        {
            if (!(userDAO.findIfExist(user.getEmail()))) // if no user with the same Email exist
            {
                user.setUserType("Admin"); // Default user type
                System.out.println("\n user type at inscription is : '"+user.getUserType()+"'\n");
                userDAO.insert(user);
                return true;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    public void updateEmail(String Email, User user)
    {
        try 
        {
            if (!(userDAO.findIfExist(Email)))// if no user with the same new Email exist
            {
                user.setEmail(Email);
                userDAO.update(user);
            }
            else
            {
                throw new Exception();
            }
        } catch (SQLException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void updatePassword(String password, User user)
    {
        try 
        {
            user.setPassword(hashpw(password));
            userDAO.update(user);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public User login(User user)
    {
        try 
        {
            System.out.println("\n hash passwrd: "+ userDAO.findById(userDAO.findByEmail(user.getEmail()).getUserId()).getPassword()+"\n");
            System.out.println("\n checkpwd:"+ checkPassword(user.getPassword(),userDAO.findById(userDAO.findByEmail(user.getEmail()).getUserId()).getPassword())+"\n");
            System.out.println("\n email exist ?: "+ userDAO.findIfExist(user.getEmail())+"\n");
            // Check if the user exist through its id and if it exist verifythe mdp check
            if (userDAO.findIfExist(user.getEmail())  && 
                checkPassword(user.getPassword(),userDAO.findById(userDAO.findByEmail(user.getEmail()).getUserId()).getPassword()))   
            {
                User user2 = userDAO.findById(userDAO.findByEmail(user.getEmail()).getUserId());
                user2.setPassword("");
                System.out.println("\n user2 type:*"+user2.getUserType()+"*\n");
                return user2;


            }
       
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;

    }

    public void setCurrentUser(User user)
    {
        this.currentUser = user;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public String hashpw(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); 
    }



    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
    public UserDAO getUserDAO() {
        return userDAO;
    }
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

}
