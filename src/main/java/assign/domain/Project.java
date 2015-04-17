package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlRootElement(name = "project")
//@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

	private int id;
	private String name;
	private String description;
	private int meetings;


	public int getId() {
		return id;
	}
	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMeetings() {
		return meetings;
	}
	public void setMeetings(int meetings) {
		this.meetings = meetings;
	}
}
