package assign.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import assign.domain.Course;
import assign.domain.Meeting;
import assign.domain.Project;

public class CourseStudentServiceImpl implements CourseStudentService {

	String dbURL = "";
	String dbUsername = "";
	String dbPassword = "";
	DataSource ds;

	// DB connection information would typically be read from a config file.
	public CourseStudentServiceImpl(String dbUrl, String username, String password) {
		this.dbURL = dbUrl;
		this.dbUsername = username;
		this.dbPassword = password;
		
		ds = setupDataSource();
	}
	
	public DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(this.dbUsername);
        ds.setPassword(this.dbPassword);
        ds.setUrl(this.dbURL);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        return ds;
    }
	
	public Project addProject(Project p) throws Exception {
		Connection conn = ds.getConnection();
		
		String insert = "INSERT INTO projects(name, project_description) VALUES(?, ?)";
		PreparedStatement stmt = conn.prepareStatement(insert,
                Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, p.getName());
		stmt.setString(2, p.getDescription());
		
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating course failed, no rows affected.");
        }
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	p.setId(generatedKeys.getInt(1));
        }
        else {
            throw new SQLException("Creating course failed, no ID obtained.");
        }
        
        // Close the connection
        conn.close();
        
		return p;
	}
	
	public Project updateProject(Project p) throws Exception {
		Connection conn = ds.getConnection();
	    /*UPDATE students  
	    SET User_Name = 'beserious', First_Name = 'Johnny'  
	    WHERE Student_Id = '3' */ 
		
		String insert = "UPDATE projects SET ";
		insert += "name='" + p.getName() + "'"+ ",";
		insert += "project_description='" + p.getDescription() + "'";
		insert += "WHERE project_id = " + p.getId();
		PreparedStatement stmt = conn.prepareStatement(insert);
		
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating course failed, no rows affected.");
        }
                
        // Close the connection
        conn.close();
        
		return p;
	}
	public int getNumMeetings(int projectId) throws Exception { 
		
	   /* SELECT COUNT (expression)  
	    FROM tables  
	    WHERE conditions;*/
		
		
		String query = "SELECT COUNT(*) from meeings where project_id=" + projectId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		ResultSet r = s.executeQuery();
		int count = s.getUpdateCount();

		return count;
	}

	public Project getProject(int projectId) throws Exception {
		String query = "select * from projects where project_id=" + projectId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		ResultSet r = s.executeQuery();
		
		if (!r.next()) {
			return null;
		}
		
		Project p = new Project();
		p.setId(r.getInt("project_id"));
		p.setName(r.getString("name"));
		p.setDescription(r.getString("project_description"));
		// Close the connection
        conn.close();
		return p;
	}
	
	public Meeting addMeeting(Meeting m) throws Exception {
		Connection conn = ds.getConnection();
		
		String insert = "INSERT INTO meetings(name, year, project_id) VALUES(?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(insert,
                Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, m.getName());
		stmt.setString(2, m.getYear());
		stmt.setLong(3, m.getProject_id());
		
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating project failed, no rows affected.");
        }
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	m.setId(generatedKeys.getInt(1));
        }
        else {
            throw new SQLException("Creating project failed, no ID obtained.");
        }
        
        // Close the connection
        conn.close();
        
		return m;
	}
	
	public Meeting getMeeting(int projectId) throws Exception {
		String query = "select * from meetings where project_id=" + projectId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		ResultSet r = s.executeQuery();
		
		if (!r.next()) {
			return null;
		}
		
		// might need to change to return multiple different meetings. 
		
		Meeting m = new Meeting();
		m.setYear(r.getString("year"));
		m.setName(r.getString("name"));
		m.setId(r.getInt("meeting_id"));
		// Close the connection
        conn.close();
		return m;
	}
	
	public boolean deleteProject(int projectId) throws Exception {
		boolean check = false;
		String query = "delete * from projects where project_id=" + projectId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		int affectedRows = s.executeUpdate();

        if (affectedRows == 0) {
            check = true;
        }
     // Close the connection
        conn.close();
		
		String query1 = "delete * from meetings where project_id=" + projectId;
		Connection conn1 = ds.getConnection();
		PreparedStatement s1 = conn1.prepareStatement(query1);

		int affectedRows1 = s1.executeUpdate();

        if (affectedRows1 == 0) {
            check = true;
        }
     // Close the connection
        conn1.close();
        return check;
	}
	
	public Course addCourse(Course c) throws Exception {
		Connection conn = ds.getConnection();
		
		String insert = "INSERT INTO courses(name, course_num) VALUES(?, ?)";
		PreparedStatement stmt = conn.prepareStatement(insert,
                Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, c.getName());
		stmt.setString(2, c.getCourseNum());
		
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating course failed, no rows affected.");
        }
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	c.setCourseId(generatedKeys.getInt(1));
        }
        else {
            throw new SQLException("Creating course failed, no ID obtained.");
        }
        
        // Close the connection
        conn.close();
        
		return c;
	}

	public Course getCourse(int courseId) throws Exception {
		String query = "select * from courses where course_id=" + courseId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		ResultSet r = s.executeQuery();
		
		if (!r.next()) {
			return null;
		}
		
		Course c = new Course();
		c.setCourseNum(r.getString("course_num"));
		c.setName(r.getString("name"));
		c.setCourseId(r.getInt("course_id"));
		return c;
	}

}
