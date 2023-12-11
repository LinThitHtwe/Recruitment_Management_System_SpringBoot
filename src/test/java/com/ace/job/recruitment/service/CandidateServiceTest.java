package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.service.impl.CandidateServiceImpl;

public class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateServiceImpl candidateService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCandidate() {
        Candidate candidate = new Candidate();
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        Candidate savedCandidate = candidateService.addCandidate(candidate);

        assertEquals(candidate, savedCandidate);
        verify(candidateRepository, times(1)).save(candidate);
    }

    @Test
    public void testGetAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(new Candidate());
        candidates.add(new Candidate());

        when(candidateRepository.findAll()).thenReturn(candidates);

        List<Candidate> result = candidateService.getAllCandidate();

        assertEquals(2, result.size());
        verify(candidateRepository, times(1)).findAll();
    }

    @Test
    public void testGetCandidateById() {
        Candidate candidate = new Candidate();
        long candidateId = 1L;
        candidate.setId(candidateId);

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        Candidate result = candidateService.getCandidateById(candidateId);

        assertEquals(candidate, result);
        verify(candidateRepository, times(1)).findById(candidateId);
    }

    @Test
    public void testGetCandidates() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<Candidate> candidates = new ArrayList<>();
        candidates.add(new Candidate());
        candidates.add(new Candidate());

        when(candidateRepository.findAllById(ids)).thenReturn(candidates);

        List<Candidate> result = candidateService.getCandidates(ids);

        assertEquals(2, result.size());
        verify(candidateRepository, times(1)).findAllById(ids);
    }
}
