//package com.ace.job.recruitment.repository;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.function.Function;
//
//import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
//import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.repository.NoRepositoryBean;
//import org.springframework.data.repository.PagingAndSortingRepository;
//
///**
// * Convenience interface to allow pulling in {@link PagingAndSortingRepository}
// * and {@link JpaSpecificationExecutor} functionality in one go.
// * 
// * @author Damien Arrachequesne
// */
//@NoRepositoryBean
//public interface DataTableRepositoryWithMod<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {
//
//	/**
//	 * Returns the filtered list for the given {@link DataTablesInput} and date
//	 * range.
//	 *
//	 * @param input     the {@link DataTablesInput} mapped from the Ajax request
//	 * @param startDate the start date of the range
//	 * @param endDate   the end date of the range
//	 * @return a {@link DataTablesOutput}
//	 */
//	DataTablesOutput<T> findAllWithinDateRange(DataTablesInput input, LocalDate startDate, LocalDate endDate);
//
//	/**
//	 * Returns the filtered list for the given {@link DataTablesInput} and date
//	 * range, along with custom filtering specifications.
//	 *
//	 * @param input                     the {@link DataTablesInput} mapped from the
//	 *                                  Ajax request
//	 * @param startDate                 the start date of the range
//	 * @param endDate                   the end date of the range
//	 * @param additionalSpecification   an additional {@link Specification} to apply
//	 *                                  to the query (with an "AND" clause)
//	 * @param preFilteringSpecification a pre-filtering {@link Specification} to
//	 *                                  apply to the query (with an "AND" clause)
//	 * @return a {@link DataTablesOutput}
//	 */
//	DataTablesOutput<T> findAllWithinDateRange(DataTablesInput input, LocalDate startDate, LocalDate endDate,
//			Specification<T> additionalSpecification, Specification<T> preFilteringSpecification);
//
//	/**
//	 * Returns the filtered list for the given {@link DataTablesInput} and date
//	 * range, along with custom filtering specifications and result conversion.
//	 *
//	 * @param input                     the {@link DataTablesInput} mapped from the
//	 *                                  Ajax request
//	 * @param startDate                 the start date of the range
//	 * @param endDate                   the end date of the range
//	 * @param additionalSpecification   an additional {@link Specification} to apply
//	 *                                  to the query (with an "AND" clause)
//	 * @param preFilteringSpecification a pre-filtering {@link Specification} to
//	 *                                  apply to the query (with an "AND" clause)
//	 * @param converter                 the {@link Function} to apply to the results
//	 *                                  of the query
//	 * @return a {@link DataTablesOutput}
//	 */
//	<R> DataTablesOutput<R> findAllWithinDateRange(DataTablesInput input, LocalDate startDate, LocalDate endDate,
//			Specification<T> additionalSpecification, Specification<T> preFilteringSpecification,
//			Function<T, R> converter);
//
//}
