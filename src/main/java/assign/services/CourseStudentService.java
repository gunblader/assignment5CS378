package assign.services;

import assign.domain.Course;
import assign.domain.Meeting;
import assign.domain.Project;

public interface CourseStudentService {

	public Project addProject(Project p) throws Exception;
	
	public Project updateProject(Project p) throws Exception;

	public Project getProject(int project_Id) throws Exception;
	
	public boolean deleteProject(int projectId) throws Exception;
	
	public Meeting addMeeting(Meeting m) throws Exception;
	
	public int getNumMeetings(int projectId) throws Exception;
	
	public Meeting getMeeting(int projectId) throws Exception;

	public Course addCourse(Course c) throws Exception;

	public Course getCourse(int courseId) throws Exception;

}
