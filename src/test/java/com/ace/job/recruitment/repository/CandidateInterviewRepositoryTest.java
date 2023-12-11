package com.ace.job.recruitment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.repository.CandidateInterviewRepository;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.repository.InterviewRepository;

@SpringBootTest
@Transactional
@DirtiesContext
public class CandidateInterviewRepositoryTest {
	
	@Autowired
	private CandidateInterviewRepository ciRepo;
	
	@Autowired
	private CandidateRepository cRepo;
	
	@Autowired
	private InterviewRepository interviewRepo;
	
	@Test
	public void  testFindByCandidateId() {
		
		Candidate candidate=new Candidate();
		candidate.setId(1L);
		cRepo.save(candidate);
		
        CandidateInterview interview = new CandidateInterview();
        interview.setStatus("stage1");
        interview.setCandidate(candidate);
        
        ciRepo.save(interview);
        
        List<CandidateInterview> interviews = ciRepo.findByCandidateId(1L);

        assertEquals(1, interviews.size());
        assertEquals(interview.getCandidate(), interviews.get(0).getCandidate());
        
	}
	
	@Test
	public void testFindByCandidateIdAndInterviewStatus() {

		Candidate candidate=new Candidate();
		candidate.setId(1L);
		cRepo.save(candidate);
		
		Interview interview =new Interview();
		interview.setStatus(true);
		interviewRepo.save(interview);
		
		
        CandidateInterview Cinterview = new CandidateInterview();
        Cinterview.setCandidate(candidate);
        Cinterview.setInterview(interview);
        
        ciRepo.save(Cinterview);

        
        List<CandidateInterview> interviews = ciRepo.findByCandidateIdAndInterviewStatus(1L, true);

        assertEquals(1, interviews.size());
        assertEquals(Cinterview.getCandidate(), interviews.get(0).getCandidate());
        assertEquals(Cinterview.getInterview().isStatus(), interviews.get(0).getInterview().isStatus());
    }
	
	@Test
    public void testFindReviewsByCandidateId() {
		Candidate candidate=new Candidate();
		candidate.setId(1L);
		cRepo.save(candidate);
		
		Interview interview =new Interview();
		interview.setStatus(true);
		interviewRepo.save(interview);
		
		
        CandidateInterview Cinterview = new CandidateInterview();
        Cinterview.setCandidate(candidate);
        Cinterview.setInterview(interview);
        
        ciRepo.save(Cinterview);

        List<CandidateInterview> interviews = ciRepo.findReviewsByCandidateId(1L);

        assertEquals(1, interviews.size());
        assertEquals(Cinterview.getCandidate().getId(), interviews.get(0).getCandidate().getId());
    }

}
