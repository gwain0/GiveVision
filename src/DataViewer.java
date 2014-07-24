
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class DataViewer extends JFrame {

	private int count;
	private ArrayList<Person> people;
	private int peopleSize;

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField NameField;
	private JTextField BioField;
	private JTextField ImageField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.out.println("fgdlkjgd");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Database database = new Database();
					database.run();
					ArrayList<Person> databasePeople = database.getPeople();
					
					DataViewer frame = new DataViewer(databasePeople);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DataViewer(ArrayList<Person> databasePeople) {
		people = databasePeople;
		peopleSize = people.size();
		count = 0;

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 477, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNextRecord = new JButton("Next Record");
		btnNextRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 {
					 if(count<peopleSize-1){
					 count++;
					 setPerson();
					 } else{
						 NameField.setText("No more records");	
						 BioField.setText("No more records");
						 ImageField.setText("No more records");
						 
					 }
					 
			
			}
		}});
		btnNextRecord.setBounds(95, 199, 103, 23);
		contentPane.add(btnNextRecord);

		setPerson();
	}
	

	 private void setPerson(){

	NameField = new JTextField();
	NameField.setBounds(56, 36, 176, 35);
	contentPane.add(NameField);
	NameField.setColumns(10);
	
	NameField.setText(people.get(count).getName());
	
	
	BioField = new JTextField();
	BioField.setBounds(56, 82, 176, 35);
	contentPane.add(BioField);
	BioField.setColumns(10);
	
	BioField.setText(people.get(count).getBio());
	
	ImageField = new JTextField();
	ImageField.setBounds(56, 138, 176, 35);
	contentPane.add(ImageField);
	ImageField.setColumns(10);
	
	ImageField.setText(people.get(count).getImage());
	
	JPanel panel = new JPanel();
	panel.setBounds(305, 47, 103, 132);
	contentPane.add(panel);
	
	JButton btnDelete = new JButton("Delete Record");
	btnDelete.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
	               
			
			
			//Database database = new Database();
		
			   Connection connection = null;
		          ResultSet resultSet = null;
		          Statement statement = null;
		          try {
		               
		               connection = DriverManager
		                         .getConnection("jdbc:sqlite:C:\\Users\\Mariam\\Documents\\GoogleApp\\Peopleinfo.sqlite");
		               
		               statement = connection.createStatement();;
		              
		               if(people.size() >0){
		               String sq2 = "DELETE FROM PeoplesInfo"; 
                       statement.executeUpdate(sq2);
                       NameField.setText("No more records");	
                       BioField.setText("No more records");
                       ImageField.setText("No more records");
                       
                       
                       System.out.println("Records in table have successfully been deleted");
		          }
		               
		               else{  System.out.println("No records to delete");
		               }
		             
		  			
		               
		            //http://stackoverflow.com/questions/17935728/deleting-record-from-sqlite-database-using-java  
		              
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          
            
		
		}
	});
	btnDelete.setBounds(279, 199, 112, 23);
	contentPane.add(btnDelete);
	
	
}
	 
	 public int getPeopleSize(){
		 
		 return peopleSize;
		 
		 
		 
	 }
}
