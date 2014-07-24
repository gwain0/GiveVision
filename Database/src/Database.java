import java.sql.* ;  // for standard JDBC programs
import java.util.ArrayList;

import java.io.*;

// new commit opens up

public class Database {

    private ArrayList<Person> people;


    public Database(){
        people = new ArrayList<Person>();


    }


    public void run(){

        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;


        try {
        	//Establishes a connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:C:\\Users\\Mariam\\Desktop\\GiveVision\\Database\\PeopleInfo.sqlite);


            statement = connection.createStatement();
            //gets tables content from database
            resultSet = statement
                    .executeQuery("SELECT name, bio, image FROM PeoplesInfo");
            // checks each row, if row is empty adds John to the database 
            if(!(resultSet.next())){

            	// checks if the image exists below and if it does, it adds john
                File f = new File ("C:\\Users\\Mariam\\Documents\\GoogleApp\\images\\upload.png");

                
                if (f.exists()){
                	//prints out a statement saying the image exists
                	 System.out.println("Image exists");
                		//inserts in to the database and updates
                        String sql = "INSERT INTO PeoplesInfo (Name,bio,Image) " +
                                "VALUES ('John','California','C:\\Users\\Mariam\\Documents\\GoogleApp\\images\\upload.png');"; 
                        statement.executeUpdate(sql);
                        
                       

                    }
                    else {
                    	//if it doesn't exist prints out the below statament
                        System.out.println("Image doesn't exist");}

            }


            else {

            	// if the database is not empty it'll insert Aqib in to he datatbase
                String sql = "INSERT INTO PeoplesInfo (Name,bio,Image) " +
                        "VALUES ('Aqib','California','C:\\Users\\Mariam\\Documents\\GoogleApp\\images\\upload.png');"; 
                statement.executeUpdate(sql);

                System.out.println("Record added");


            }


            
            resultSet = statement
                    .executeQuery("SELECT name, bio, image FROM PeoplesInfo");



            //searchs through each row of the database and adds the records in to the arraylist
        while (resultSet.next()) {

                Person p = new Person(resultSet.getString("name"),resultSet.getString("bio"),resultSet.getString("image"));
                people.add(p);


                System.out.println(p.toString());
                System.out.println("Retrieved element is = " + p.getName());       



            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	//closes the connection to the database
                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    // gets the arraylist
    public ArrayList<Person> getPeople(){
        return people;




    }

}