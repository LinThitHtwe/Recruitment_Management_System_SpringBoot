package com.ace.job.recruitment.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.entity.CandidateStatus;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.Status;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.service.CandidateService;

@Service
public class CandidateServiceImpl implements CandidateService {

	@Autowired
	CandidateRepository candidateRepo;

	@Autowired
	EntityManager entityManager;

	@Override
	public Candidate addCandidate(Candidate candidate) {
		return candidateRepo.save(candidate);
	}

	@Override
	public List<Candidate> getAllCandidate() {
		return candidateRepo.findAll();
	}

	@Override
	public Candidate getCandidateById(long id) {
		return candidateRepo.findById(id).orElse(null);
	}

	@Override
	public List<Candidate> getCandidates(List<Long> id) {
		return candidateRepo.findAllById(id);
	}

	@Override
	public DataTablesOutput<Candidate> getDataTableData(@Valid DataTablesInput input) {
		return candidateRepo.findAll(input);
	}

	@Override
	public DataTablesOutput<Candidate> getDataTableDataByDepartment(@Valid DataTablesInput input, int departmentId,
			List<Long> candidateIds) {
		Specification<Candidate> specification = (root, query, criteriaBuilder) -> {
			Predicate departmentPredicate = criteriaBuilder.equal(root.get("vacancy").get("department").get("id"),
					departmentId);
			Predicate candidatePredicate = root.get("id").in(candidateIds);
			Predicate combinedPredicate = criteriaBuilder.and(departmentPredicate, candidatePredicate);
			return combinedPredicate;
		};

		return candidateRepo.findAll(input, specification);
	}

	@Override
	public DataTablesOutput<Candidate> getDataTableDataForAdmin(@Valid DataTablesInput input, List<Long> candidateIds) {
		Specification<Candidate> specification = (root, query, criteriaBuilder) -> root.get("id").in(candidateIds);

		return candidateRepo.findAll(input, specification);
	}

	@Override
	public DataTablesOutput<Candidate> getCancelCandidates(@Valid DataTablesInput input) {
		Specification<Candidate> specification = (root, query, criteriaBuilder) -> {
			Subquery<Boolean> subquery = query.subquery(Boolean.class);
			Root<CandidateInterview> subRoot = subquery.from(CandidateInterview.class);
			subquery.select(criteriaBuilder.literal(true));
			subquery.where(criteriaBuilder.and(criteriaBuilder.equal(subRoot.get("candidate"), root),
					criteriaBuilder.equal(subRoot.get("status"), "Cancel")));
			return criteriaBuilder.exists(subquery);
		};

		specification = specification
				.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isRecall")));

		return candidateRepo.findAll(input, specification);
	}

	@Override
	public DataTablesOutput<Candidate> getAllCandidatesWithFilters(DataTablesInput input, String candidateStatus,
			String interviewStatus, LocalDate startDate, LocalDate endDate, Integer interviewStage) {

		Specification<Candidate> preFilteringSpecification = null;

		Specification<Candidate> additionalSpecification = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (candidateStatus != null && !candidateStatus.isBlank()) {
				Join<Candidate, CandidateStatus> candidateStatusJoin = root.join("candidateStatusList", JoinType.LEFT);
				Join<CandidateStatus, Status> statusJoin = candidateStatusJoin.join("status", JoinType.LEFT);
				Predicate statusPredicate = builder.equal(statusJoin.get("name"), candidateStatus);

				Subquery<Integer> subquery = query.subquery(Integer.class);
				Root<CandidateStatus> subqueryRoot = subquery.from(CandidateStatus.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.and(builder.equal(subqueryRoot.get("candidate"), root), statusPredicate));

				Predicate indexPredicate = builder.equal(candidateStatusJoin.get("id"), subquery);
				predicates.add(indexPredicate);
			}

			if (interviewStatus != null && !interviewStatus.isBlank()) {

				if ("employed".equalsIgnoreCase(interviewStatus)) {
					Predicate employedPredicate = builder.isTrue(root.get("isEmploy"));
					predicates.add(employedPredicate);
				} else if ("recalled".equalsIgnoreCase(interviewStatus)) {
					Predicate recalledPredicate = builder.isTrue(root.get("isRecall"));
					predicates.add(recalledPredicate);
				} else {
					Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
							JoinType.LEFT);
					Predicate interviewStatusPredicate = builder.equal(candidateInterviewJoin.get("status"),
							interviewStatus);

					Subquery<Long> subquery = query.subquery(Long.class);
					Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
					subquery.select(builder.max(subqueryRoot.get("id")))
							.where(builder.equal(subqueryRoot.get("candidate"), root));

					Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
					predicates.add(interviewStatusPredicate);
					predicates.add(indexPredicate);
				}
			}

			if (startDate != null && endDate != null) {
				Predicate dateRangePredicate = builder.and(
						builder.greaterThanOrEqualTo(root.get("submitDate"), startDate),
						builder.lessThan(root.get("submitDate"), endDate.plusDays(1)));
				predicates.add(dateRangePredicate);
			}

			if (interviewStage != null) {
				Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
						JoinType.LEFT);
				Join<CandidateInterview, Interview> interviewJoin = candidateInterviewJoin.join("interview",
						JoinType.LEFT);
				Predicate stagePredicate = builder.equal(interviewJoin.get("stage"), interviewStage);

				Subquery<Long> subquery = query.subquery(Long.class);
				Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.equal(subqueryRoot.get("candidate"), root));

				Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
				predicates.add(stagePredicate);
				predicates.add(indexPredicate);
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};

		return candidateRepo.findAll(input, additionalSpecification, preFilteringSpecification);
	}

	@Override
	public DataTablesOutput<Candidate> getAllCancelCandidatesWithFilters(DataTablesInput input, LocalDate startDate,
			LocalDate endDate, Integer interviewStage) {
		Specification<Candidate> preFilteringSpecification = (root, query, builder) -> {
			Subquery<Boolean> subquery = query.subquery(Boolean.class);
			Root<CandidateInterview> subRoot = subquery.from(CandidateInterview.class);
			subquery.select(builder.literal(true));
			subquery.where(builder.and(builder.equal(subRoot.get("candidate"), root),
					builder.equal(subRoot.get("status"), "Cancel")));
			return builder.exists(subquery);
		};

		preFilteringSpecification = preFilteringSpecification
				.and((root, query, builder) -> builder.isFalse(root.get("isRecall")));

		Specification<Candidate> additionalSpecification = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (startDate != null && endDate != null) {
				Predicate dateRangePredicate = builder.and(
						builder.greaterThanOrEqualTo(root.get("submitDate"), startDate),
						builder.lessThan(root.get("submitDate"), endDate.plusDays(1)));
				predicates.add(dateRangePredicate);
			}

			if (interviewStage != null) {
				Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
						JoinType.LEFT);
				Join<CandidateInterview, Interview> interviewJoin = candidateInterviewJoin.join("interview",
						JoinType.LEFT);
				Predicate stagePredicate = builder.equal(interviewJoin.get("stage"), interviewStage);

				Subquery<Long> subquery = query.subquery(Long.class);
				Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.equal(subqueryRoot.get("candidate"), root));

				Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
				predicates.add(stagePredicate);
				predicates.add(indexPredicate);
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};
		Specification<Candidate> finalSpecification = preFilteringSpecification.and(additionalSpecification);

		return candidateRepo.findAll(input, finalSpecification);
	}

	@Override
	public DataTablesOutput<Candidate> getFilteredCandidateByDepartment(@Valid DataTablesInput input, int departmentId,
			List<Long> candidateIds, String interviewStatus, LocalDate startDate, LocalDate endDate,
			Integer interviewStage) {
		Specification<Candidate> preFilteringSpecification = (root, query, builder) -> {
			Predicate departmentPredicate = builder.equal(root.get("vacancy").get("department").get("id"),
					departmentId);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(departmentPredicate);

			// Build subquery for candidateIds along with the date range predicate
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
			subquery.select(builder.max(subqueryRoot.get("id")))
					.where(builder.equal(subqueryRoot.get("candidate"), root));

			if (startDate != null && endDate != null) {
				Predicate dateRangePredicate = builder.and(
						builder.greaterThanOrEqualTo(subqueryRoot.get("submitDate"), startDate),
						builder.lessThan(subqueryRoot.get("submitDate"), endDate.plusDays(1)));
				subquery.where(dateRangePredicate);
			}

			Predicate indexPredicate = builder.equal(root.join("candidateInterviews").get("id"), subquery);
			predicates.add(indexPredicate);

			return builder.and(predicates.toArray(new Predicate[0]));
		};
		Specification<Candidate> additionalSpecification = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (interviewStatus != null && !interviewStatus.isBlank()) {
				Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
						JoinType.LEFT);
				Predicate interviewStatusPredicate = builder.equal(candidateInterviewJoin.get("status"),
						interviewStatus);

				Subquery<Long> subquery = query.subquery(Long.class);
				Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.equal(subqueryRoot.get("candidate"), root));

				Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
				predicates.add(interviewStatusPredicate);
				predicates.add(indexPredicate);
			}

			if (interviewStage != null) {
				Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
						JoinType.LEFT);
				Join<CandidateInterview, Interview> interviewJoin = candidateInterviewJoin.join("interview",
						JoinType.LEFT);
				Predicate stagePredicate = builder.equal(interviewJoin.get("stage"), interviewStage);

				Subquery<Long> subquery = query.subquery(Long.class);
				Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.equal(subqueryRoot.get("candidate"), root));

				Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
				predicates.add(stagePredicate);
				predicates.add(indexPredicate);
			}

			if (startDate != null && endDate != null) {
				Subquery<Long> subquery = query.subquery(Long.class);
				Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
				subquery.select(builder.max(subqueryRoot.get("id"))).where(
						builder.equal(subqueryRoot.get("candidate"), root),
						builder.greaterThanOrEqualTo(subqueryRoot.get("submitDate"), startDate),
						builder.lessThan(subqueryRoot.get("submitDate"), endDate.plusDays(1)));

				Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
						JoinType.LEFT);
				Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
				predicates.add(indexPredicate);
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};

		Specification<Candidate> combinedSpecification = (root, query, builder) -> {
			Predicate preFilterPredicate = preFilteringSpecification.toPredicate(root, query, builder);
			Predicate additionalPredicate = additionalSpecification.toPredicate(root, query, builder);
			return builder.or(preFilterPredicate, additionalPredicate);
		};

		return candidateRepo.findAll(input, combinedSpecification);
	}

	@Override
	public DataTablesOutput<Candidate> getFilteredCandidateForAdminAtInterview(@Valid DataTablesInput input,
			List<Long> candidateIds, String interviewStatus, LocalDate startDate, LocalDate endDate,
			Integer interviewStage) {
		Specification<Candidate> additionalSpecification = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (interviewStatus != null && !interviewStatus.isBlank()) {
				Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
						JoinType.LEFT);
				Predicate interviewStatusPredicate = builder.equal(candidateInterviewJoin.get("status"),
						interviewStatus);

				Subquery<Long> subquery = query.subquery(Long.class);
				Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.equal(subqueryRoot.get("candidate"), root));

				Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
				predicates.add(interviewStatusPredicate);
				predicates.add(indexPredicate);
			}

			if (interviewStage != null) {
				Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
						JoinType.LEFT);
				Join<CandidateInterview, Interview> interviewJoin = candidateInterviewJoin.join("interview",
						JoinType.LEFT);
				Predicate stagePredicate = builder.equal(interviewJoin.get("stage"), interviewStage);

				Subquery<Long> subquery = query.subquery(Long.class);
				Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.equal(subqueryRoot.get("candidate"), root));

				Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
				predicates.add(stagePredicate);
				predicates.add(indexPredicate);
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};
		return candidateRepo.findAll(input, additionalSpecification);
	}

	@Override
	public List<Candidate> checkCandidateDuplication(String email, long vacancyId) {
		return candidateRepo.getCandidateForDuplication(email, vacancyId);
	}

	@Override
	public DataTablesOutput<Candidate> getAllCandidatesInVacancyWithFilters(DataTablesInput input,
			String candidateStatus, String interviewStatus, Integer interviewStage, long vacancyId) {
		Specification<Candidate> preFilteringSpecification = (root, query, builder) -> {
			Join<Candidate, Vacancy> vacancyJoin = root.join("vacancy");
			Predicate vacancyPredicate = builder.equal(vacancyJoin.get("id"), vacancyId);
			return builder.and(vacancyPredicate);
		};
		Specification<Candidate> additionalSpecification = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (candidateStatus != null && !candidateStatus.isBlank()) {
				Join<Candidate, CandidateStatus> candidateStatusJoin = root.join("candidateStatusList", JoinType.LEFT);
				Join<CandidateStatus, Status> statusJoin = candidateStatusJoin.join("status", JoinType.LEFT);
				Predicate statusPredicate = builder.equal(statusJoin.get("name"), candidateStatus);

				Subquery<Integer> subquery = query.subquery(Integer.class);
				Root<CandidateStatus> subqueryRoot = subquery.from(CandidateStatus.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.and(builder.equal(subqueryRoot.get("candidate"), root), statusPredicate));

				Predicate indexPredicate = builder.equal(candidateStatusJoin.get("id"), subquery);
				predicates.add(indexPredicate);
			}

			if (interviewStatus != null && !interviewStatus.isBlank()) {

				if ("employed".equalsIgnoreCase(interviewStatus)) {
					Predicate employedPredicate = builder.isTrue(root.get("isEmploy"));
					predicates.add(employedPredicate);
				} else if ("recalled".equalsIgnoreCase(interviewStatus)) {
					Predicate recalledPredicate = builder.isTrue(root.get("isRecall"));
					predicates.add(recalledPredicate);
				} else {
					Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
							JoinType.LEFT);
					Predicate interviewStatusPredicate = builder.equal(candidateInterviewJoin.get("status"),
							interviewStatus);

					Subquery<Long> subquery = query.subquery(Long.class);
					Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
					subquery.select(builder.max(subqueryRoot.get("id")))
							.where(builder.equal(subqueryRoot.get("candidate"), root));

					Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
					predicates.add(interviewStatusPredicate);
					predicates.add(indexPredicate);
				}
			}

			if (interviewStage != null) {
				Join<Candidate, CandidateInterview> candidateInterviewJoin = root.join("candidateInterviews",
						JoinType.LEFT);
				Join<CandidateInterview, Interview> interviewJoin = candidateInterviewJoin.join("interview",
						JoinType.LEFT);
				Predicate stagePredicate = builder.equal(interviewJoin.get("stage"), interviewStage);

				Subquery<Long> subquery = query.subquery(Long.class);
				Root<CandidateInterview> subqueryRoot = subquery.from(CandidateInterview.class);
				subquery.select(builder.max(subqueryRoot.get("id")))
						.where(builder.equal(subqueryRoot.get("candidate"), root));

				Predicate indexPredicate = builder.equal(candidateInterviewJoin.get("id"), subquery);
				predicates.add(stagePredicate);
				predicates.add(indexPredicate);
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};

		return candidateRepo.findAll(input, additionalSpecification, preFilteringSpecification);
	}

	@Override
	public DataTablesOutput<Candidate> getCandidatesByVacancyId(@Valid DataTablesInput input, long vacancyId) {
		Specification<Candidate> preFilteringSpecification = (root, query, builder) -> {
			Join<Candidate, Vacancy> vacancyJoin = root.join("vacancy");
			Predicate vacancyPredicate = builder.equal(vacancyJoin.get("id"), vacancyId);
			return builder.and(vacancyPredicate);
		};
		return candidateRepo.findAll(input, preFilteringSpecification);
	}

}