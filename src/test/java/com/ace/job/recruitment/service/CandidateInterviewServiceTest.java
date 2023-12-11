package com.ace.job.recruitment.service;

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
import com.ace.job.recruitment.entity.Reviews;
import com.ace.job.recruitment.repository.CandidateInterviewRepository;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.repository.InterviewRepository;
import com.ace.job.recruitment.service.CandidateInterviewService;

@SpringBootTest
@Transactional
@DirtiesContext
public class CandidateInterviewServiceTest {
	
	@Autowired
	private CandidateInterviewRepository candidateInterviewRepository;
	
	@Autowired
    private CandidateInterviewService candidateInterviewService;

	@Autowired
	private CandidateRepository cRepo;
	

	@Autowired
	private InterviewRepository interviewRepo;
	
    @Test
    public void testSaveCandidateInterview() {
    	Candidate candidate=new Candidate();
		candidate.setId(1L);
		cRepo.save(candidate);
		
		Interview interview =new Interview();
		interview.setStatus(true);
		interviewRepo.save(interview);
		
		
        CandidateInterview Cinterview = new CandidateInterview();
        Cinterview.setCandidate(candidate);
        Cinterview.setInterview(interview);
        
        candidateInterviewRepository.save(Cinterview);
        
        
        CandidateInterview savedInterview = candidateInterviewService.saveCandidateInterview(Cinterview);

        CandidateInterview fetchedInterview = candidateInterviewRepository.findById(savedInterview.getId()).orElse(null);

        assertEquals(savedInterview.getId(), fetchedInterview.getId());
    }
    
    @Test
    public void testGetCandidateInterviewById() {
    	Candidate candidate=new Candidate();
		candidate.setId(1L);
		cRepo.save(candidate);
		
		Interview interview =new Interview();
		interview.setStatus(true);
		interviewRepo.save(interview);
		
		
        CandidateInterview Cinterview = new CandidateInterview();
        Cinterview.setCandidate(candidate);
        Cinterview.setInterview(interview);
        Cinterview.setId(1L);
        
        candidateInterviewRepository.save(Cinterview);

        List<CandidateInterview> interviews = candidateInterviewService.getCandidateInterviewById(1L);

        assertEquals(1, interviews.size());
        assertEquals(Cinterview.getCandidate().getId(), interviews.get(0).getCandidate().getId());
    }
    
    @Test
    public void testGetAll() {
    	Candidate candidate=new Candidate();
		candidate.setId(1L);
		cRepo.save(candidate);
		
		Interview interview =new Interview();
		interview.setStatus(true);
		interviewRepo.save(interview);
		
		
        CandidateInterview interview1 = new CandidateInterview();
        interview1.setCandidate(candidate);
        interview1.setInterview(interview);
        interview1.setId(1L);
        
        candidateInterviewRepository.save(interview1);
		
        CandidateInterview interview2 = new CandidateInterview();
        interview2.setCandidate(candidate);
        interview2.setInterview(interview);
        interview2.setId(2L);
        
        candidateInterviewRepository.save(interview2);

        List<CandidateInterview> interviews = candidateInterviewService.getAll();

        assertEquals(2, interviews.size());
    }
    
    @Test
    public void testGetCandidateInterviewByIdAndInterviewStatus() {
    	Candidate candidate=new Candidate();
		candidate.setId(1L);
		cRepo.save(candidate);
		
		Interview interview =new Interview();
		interview.setStatus(true);
		interviewRepo.save(interview);
		
		Interview interviewS =new Interview();
		interviewS.setStatus(false);
		interviewRepo.save(interviewS);
		
		
        CandidateInterview interview1 = new CandidateInterview();
        interview1.setCandidate(candidate);
        interview1.setInterview(interview);
        interview1.setId(1L);
        
        candidateInterviewRepository.save(interview1);
		
        CandidateInterview interview2 = new CandidateInterview();
        interview2.setCandidate(candidate);
        interview2.setInterview(interviewS);
        interview2.setId(1L);
        
        candidateInterviewRepository.save(interview2);

        List<CandidateInterview> interviewsWithStatusTrue = candidateInterviewService.getCandidateInterviewByIdAndInterviewStatus(1L, true);
        List<CandidateInterview> interviewsWithStatusFalse = candidateInterviewService.getCandidateInterviewByIdAndInterviewStatus(1L, false);

        assertEquals(1, interviewsWithStatusTrue.size());
        assertEquals(interview1.getCandidate().getId(), interviewsWithStatusTrue.get(0).getCandidate().getId());

        assertEquals(1, interviewsWithStatusFalse.size());
        assertEquals(interview2.getCandidate().getId(), interviewsWithStatusFalse.get(0).getCandidate().getId());
    }
    
    @Test
    public void testGetAllReviewsByCandidateId() {
    	Candidate candidate=new Candidate();
		candidate.setId(1);
		cRepo.save(candidate);

        // Create and save an Interview entity
        Interview interview = new Interview();
        interview.setStatus(true);
        interviewRepo.save(interview);

        // Create and save a CandidateInterview entity
        CandidateInterview interview1 = new CandidateInterview();
        
        interview1.setInterview(interview);
        candidateInterviewRepository.save(interview1);

        // Create and save another CandidateInterview entity with reviews
        CandidateInterview interview2 = new CandidateInterview();
        interview2.setCandidate(candidate);
        interview2.setInterview(interview);
        candidateInterviewRepository.save(interview2);

        // Add reviews to the second interview
        Reviews review1 = new Reviews();
        interview2.getReviews().add(review1);

        Reviews review2 = new Reviews();
        interview2.getReviews().add(review2);

        // Save the changes to the interview with reviews
        candidateInterviewRepository.save(interview2);

        List<CandidateInterview> interviewsWithReviews = candidateInterviewService.getAllReviewsByCandidateId(candidate.getId());

        assertEquals(1, interviewsWithReviews.size());
        assertEquals(candidate.getId(), interviewsWithReviews.get(0).getCandidate().getId());
        assertEquals(2, interviewsWithReviews.get(0).getReviews().size());
    }

}
