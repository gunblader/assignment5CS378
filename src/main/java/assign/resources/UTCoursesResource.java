package assign.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import assign.domain.Meeting;
import assign.domain.Project;
import assign.domain.Meetings;
import assign.services.CourseStudentService;
import assign.services.CourseStudentServiceImpl;

@Path("/myeavesdrop")
public class UTCoursesResource {

	CourseStudentService courseStudentService;
	String password;
	String username;
	String dburl;

	public UTCoursesResource(@Context ServletContext servletContext) {		
		//		dburl = servletContext.getInitParameter("DBURL");
		//		username = servletContext.getInitParameter("DBUSERNAME");
		//		password = servletContext.getInitParameter("DBPASSWORD");
		dburl = "jdbc:mysql://localhost:3306/assignment5_projects";
		username = "devdatta";
		password = "";
		this.courseStudentService = new CourseStudentServiceImpl(dburl, username, password);
	}

	@GET
	@Path("/hello")
	@Produces("text/html")
	public String hello() {
		System.out.println("Inside hello");

		Project p = new Project();
		p.setName("CS439");
		p.setDescription("OS for dummies");
		
		System.out.println("New course created - send it to database");

		try {
			p = courseStudentService.addProject(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Project example 'p' was created : project_id = " + p.getId());
		System.out.println("Exiting hello");

		return "Hello Paul"; 		
	}
	
	@GET          //gets a project and displays it in Xml if found in Database.
	@Path("/projects/{id}")
	@Produces("application/xml")
	public StreamingOutput getProject(@PathParam("id") int id) throws Exception {
		Project x = new Project();
		boolean good = false;

		try {
			x = courseStudentService.getProject(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Meeting m1 = courseStudentService.getMeeting(id);
		final Project x1 = x;
		
		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputProject1(outputStream, x1, m1);
			}
		};	    
	}

	@POST
	@Path("/projects")
	@Consumes("text/xml")
	public Response createCustomer(Project p) {
		Project project = p;

		if(p.getName() == "" || p.getDescription() == "")
			return Response.status(400).build();
		
		try {
			project = courseStudentService.addProject(project);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return Response.created(URI.create("/projects/" + project.getId())).build();

	}

	@PUT
	@Path("/projects/{projectid}")
	@Consumes("text/xml")
	public void updateCustomer(@PathParam("projectid") int id, Project p) {
		Project update = p;
		update.setId(id);
		Project current = null;

		if(p.getName() == "" || p.getDescription() == "")
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		
		try {
			current = courseStudentService.updateProject(update);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
	
	}

	@POST
	@Path("/projects/{projectid}/meetings")
	@Consumes("text/xml")
	public Response createMeeting(@PathParam("projectid") int project_id, Meeting m) {
		Meeting meeting = m;
		boolean check = false;
		meeting.setProject_id(project_id);
		
		if(m.getName() == "" || m.getYear() == "")
			return Response.status(400).build();
		
		//set ID here ???
		try {
			meeting = courseStudentService.addMeeting(meeting);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.created(URI.create("/projects/" + meeting.getId())).build();

	}
	
	@DELETE
	@Path("/projects/{projectid}")
	@Consumes("text/xml")
	public Response deleteProject(@PathParam("projectid") int id) {
		boolean check = false;
		try {
			check = courseStudentService.deleteProject(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (check) throw new WebApplicationException(Response.Status.NOT_FOUND);

		return Response.status(200).build();
	}

	protected void outputProjects(OutputStream os, Meetings projects) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Meetings.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(projects, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}	

	protected void outputProject(OutputStream os, Project project) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(project, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	 protected void outputProjectNone(OutputStream os, Project p) throws IOException {
	      PrintStream writer = new PrintStream(os);
	      writer.println("<project id=\"" + p.getId() + "\">");
	      writer.println("   <name>" + p.getName() + "</name>");
	      writer.println("   <description>" + p.getDescription() + "</description>");
	      writer.println("   <meetings> " );
	      
	      writer.println("   </meetings>");
	      writer.println("</project>");
	   }
	 
	 protected void outputProject1(OutputStream os, Project p, Meeting m) throws IOException {
	      PrintStream writer = new PrintStream(os);
	      writer.println("<project id=\"" + p.getId() + "\">");
	      writer.println("   <name>" + p.getName() + "</name>");
	      writer.println("   <description>" + p.getDescription() + "</description>");
	      writer.println("   <meetings> " );
	      writer.println("   <meeting>" );
	      writer.println("   <name>" + m.getName() + "</name>");
	      writer.println("   <year>" + m.getYear() + "</year>");
	      writer.println("   </meeting>" );
	      writer.println("   </meetings>");
	      writer.println("</project>");
	   }
	 
	 protected void outputProject2(OutputStream os, Project p, Meeting m, Meeting m1) throws IOException {
	      PrintStream writer = new PrintStream(os);
	      writer.println("<project id=\"" + p.getId() + "\">");
	      writer.println("   <name>" + p.getName() + "</name>");
	      writer.println("   <description>" + p.getDescription() + "</description>");
	      writer.println("   <meetings> " );
	      writer.println("   <meeting>" );
	      writer.println("   <name>" + m.getName() + "</name>");
	      writer.println("   <year>" + m.getYear() + "</year>");
	      writer.println("   </meeting>" );
	      writer.println("   <meeting>" );
	      writer.println("   <name>" + m1.getName() + "</name>");
	      writer.println("   <year>" + m1.getYear() + "</year>");
	      writer.println("   </meeting>" );
	      writer.println("   </meetings>");
	      writer.println("</project>");
	   }

	protected void outputError(OutputStream os, Project project) throws IOException {
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	protected Project readProject(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			Project project = new Project();
//			project.setId(Integer.valueOf(root.getAttribute("id")));
			NodeList nodes = root.getChildNodes();
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("name")) {
					project.setName(element.getTextContent());
				}
				else if (element.getTagName().equals("description")) {
					project.setDescription(element.getTextContent());
				}
			}
			return project;
		}
		catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
}
