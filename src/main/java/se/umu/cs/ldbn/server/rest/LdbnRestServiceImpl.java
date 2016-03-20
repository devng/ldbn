package se.umu.cs.ldbn.server.rest;


import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.umu.cs.ldbn.server.dao.AssignmentDao;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;
import se.umu.cs.ldbn.shared.dto.HelloDto;

import com.google.inject.Singleton;

@Singleton
class LdbnRestServiceImpl implements LdbnRestService {
	
	private static final Logger log = LoggerFactory.getLogger(LdbnRestServiceImpl.class);
	
	private final AssignmentDao assignmentDao;
	
	@Inject
	LdbnRestServiceImpl(AssignmentDao assignmentDao) {
		this.assignmentDao = assignmentDao;
	}

    @Override
    public HelloDto sayHello() {
        final HelloDto hello = new HelloDto();
        hello.setMessage("I just want to say hello.");
        return hello;
    }

	@Override
	public AssignmentDto getAssignment(Integer id) {
		if (id == null || id < 0) {
			throw new IllegalArgumentException("Illegal assognment id.");
		}
		AssignmentDto dto = assignmentDao.getAssignmen(id);
		if (dto == null) {
			throw new NotFoundException();
		}
		return dto;
	}
	
	@Override
	public List<AssignmentDto> getAllAssignments() {
		List<AssignmentDto> dtos = assignmentDao.getAllAssignmen();
		if (dtos == null) {
			throw new NotFoundException();
		}
		return dtos;
	}
}
