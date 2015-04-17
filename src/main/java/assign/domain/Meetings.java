package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "meetings")
@XmlAccessorType(XmlAccessType.FIELD)
public class Meetings {
	    
    private List<String> meeting = null;
    
    public List<String> getMeetings() {
    	return meeting;
    }
    
    public void setMeeings(List<String> meetings) {
    	this.meeting = meetings;
    }
}
