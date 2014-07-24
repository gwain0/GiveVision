
public class Person {
private String name;
private String bio;
private String image;

public Person(String name, String bio, String image){
	
	this.name=name;
	this.bio=bio;
	this.image=image;
	
	
}
public String getName(){

return name;	
	
}
public String getBio(){
	
	
	return bio;
}

public String getImage(){
	
	return image;
	
}
public void setName (String newName){
	
	name = newName;
	
}

public void setBio (String newBio){
	
	bio = newBio;
	
}

public void setImage (String newImage){
	
	image = newImage;
	
}
@Override 
public String toString(){
	return name+bio+image;
}




}


