package com.ace.job.recruitment.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ace.job.recruitment.dto.CandidateSummaryDTO;
import com.ace.job.recruitment.dto.InterviewProcressSummaryDTO;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.service.VacancyService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Controller
public class ReportController {

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	VacancyService vacancyService;

	@GetMapping("/hr/download-interview-summary/{type}")
	public ResponseEntity<?> downloadInterviewSummary(@PathVariable("type") String type, HttpServletResponse response,
			HttpServletRequest request, @RequestParam(value = "startDateFrom", required = false) String startDateFrom,
			@RequestParam(value = "endDateTo", required = false) String endDateTo,
			@RequestParam(value = "status", required = false) String statusParam,
			@RequestParam(value = "departmentId", required = false) Integer departmentId,
			@RequestParam(value = "positionId", required = false) Integer positionId

	) throws IOException, JRException {
		ClassPathResource resource = new ClassPathResource("Interview_Procress_Summary.jrxml");
		String path = resource.getFile().getAbsolutePath();
		System.out.print("____________________________" + startDateFrom + "__________________");
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ReportTitle", "Interview Summary List");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = (startDateFrom != null && !startDateFrom.equalsIgnoreCase("null")
				&& !startDateFrom.equals("")) ? LocalDate.parse(startDateFrom, dateFormatter) : null;
		LocalDate endDate = (endDateTo != null && !endDateTo.equalsIgnoreCase("null") && !endDateTo.equals(""))
				? LocalDate.parse(endDateTo, dateFormatter)
				: null;
		String status = (statusParam != null && !statusParam.equalsIgnoreCase("All")) ? statusParam : null;
		Integer active = null;
		Integer repoen = null;
		Integer urgent = null;
		if (status != null) {
			if (status.equalsIgnoreCase("Active")) {
				active = 1;
			} else if (status.equalsIgnoreCase("Inactive")) {
				active = 0;
			} else if (status.equalsIgnoreCase("Urgent")) {
				urgent = 1;
			} else if (status.equalsIgnoreCase("Reopen")) {
				repoen = 1;
			}
		}

		List<Object[]> objList = new ArrayList<Object[]>();
		objList = candidateRepository.callInterviewSummary(startDate, endDate, departmentId, positionId, active, repoen,
				urgent);

		if (objList == null || objList.isEmpty())

		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data available for the selected criteria.");
		}

		List<InterviewProcressSummaryDTO> list = new ArrayList<>();
		for (

		Object[] objArray : objList) {
			String fromDate = (String) objArray[0];
			String toDate = (String) objArray[1];
			String positionName = (String) objArray[2];
			BigInteger totalReceivedCandidate = (BigInteger) objArray[3];
			BigInteger offerMail = (BigInteger) objArray[4];
			BigInteger acceptedMail = (BigInteger) objArray[5];
			BigDecimal passedInterviewCount = (BigDecimal) objArray[6];
			BigDecimal pendingInterviewCount = (BigDecimal) objArray[7];
			BigDecimal cancelInterviewCount = (BigDecimal) objArray[8];
			BigDecimal reachedInterviewCount = (BigDecimal) objArray[9];
			BigDecimal interviewedCandidate = (BigDecimal) objArray[10];
			BigDecimal leftToView = (BigDecimal) objArray[11];

			InterviewProcressSummaryDTO dto = new InterviewProcressSummaryDTO(fromDate, toDate, positionName,
					totalReceivedCandidate, offerMail, acceptedMail, passedInterviewCount, pendingInterviewCount,
					cancelInterviewCount, reachedInterviewCount, interviewedCandidate, leftToView);

			list.add(dto);
		}

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

		JasperReport jasperReport = JasperCompileManager.compileReport(path);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		if (type.equalsIgnoreCase("pdf")) {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=Interview-Procress-Summary.pdf");

			OutputStream out = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
			out.flush();
			out.close();
		} else if (type.equalsIgnoreCase("excel")) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=Interview-Procress-Summary.xlsx");

			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setDetectCellType(true);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			exporter.setConfiguration(configuration);
			configuration.setWhitePageBackground(false);
			configuration.setCollapseRowSpan(false);
			configuration.setIgnoreGraphics(false);
			exporter.exportReport();
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid report type");
		}
		return ResponseEntity.ok("Report downloaded successfully.");
	}

	@GetMapping("/hr/download-candidate-summary/{type}")
	public ResponseEntity<?> downloadCandidateSummary(@PathVariable("type") String type, HttpServletResponse response,
			HttpServletRequest request, @RequestParam(value = "startDateFrom", required = false) String startDateFrom,
			@RequestParam(value = "endDateTo", required = false) String endDateTo,
			@RequestParam(value = "selectionStatus", required = false) String selectionStatusParam,
			@RequestParam(value = "interviewStatus", required = false) String interviewStatusParam,
			@RequestParam(value = "stage", required = false) String stageParam,
			@RequestParam(value = "positionId", required = false) int positionIdParam,
			@RequestParam(value = "departmentId", required = false) int departmentIdParam,
			@RequestParam(value = "vacancyId", required = false) int vacancyIdParam

	) throws IOException, JRException {
		ClassPathResource resource = new ClassPathResource("Candidate_View_Summary.jrxml");
		String path = resource.getFile().getAbsolutePath();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ReportTitle", "Candidate Summary List");
		System.out.print("____________________________" + startDateFrom + "__________________");
		LocalDate startDate = (startDateFrom != null && !startDateFrom.equalsIgnoreCase("All")
				&& !startDateFrom.equals("")) ? LocalDate.parse(startDateFrom, dateFormatter) : null;
		LocalDate endDate = (endDateTo != null && !endDateTo.equalsIgnoreCase("All") && !endDateTo.equals(""))
				? LocalDate.parse(endDateTo, dateFormatter)
				: null;
		String selectionStatus = (selectionStatusParam != null && !selectionStatusParam.isEmpty()
				&& !selectionStatusParam.equalsIgnoreCase("All")) ? selectionStatusParam : null;
		String interviewStatus = (interviewStatusParam != null && !interviewStatusParam.isEmpty()
				&& !interviewStatusParam.equalsIgnoreCase("All")) ? interviewStatusParam : null;
		int stage = (stageParam != null && !stageParam.isEmpty() && !stageParam.equalsIgnoreCase("All"))
				? Integer.parseInt(stageParam)
				: 0;

		int positionId = (positionIdParam != 0) ? positionIdParam : 0;
		int departmentId = (departmentIdParam != 0) ? departmentIdParam : 0;
		int vacancyId = (vacancyIdParam != 0) ? vacancyIdParam : 0;

		List<Object[]> objList = new ArrayList<Object[]>();
		Integer isEmployed = null;
		Integer isRecalled = null;
		if (interviewStatus != null) {
			if (interviewStatus.equalsIgnoreCase("employed")) {

				isEmployed = 1;
				interviewStatus = null;
			} else if (interviewStatus.equalsIgnoreCase("recalled")) {
				isRecalled = 1;
				interviewStatus = null;

			}
		}
		objList = candidateRepository.allCandidateViewSummary(vacancyId, positionId, departmentId, stage,
				selectionStatus, interviewStatus, startDate, endDate, isEmployed, isRecalled);
		if (objList == null || objList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data available for the selected criteria.");
		}

		List<CandidateSummaryDTO> list = new ArrayList<>();
		for (Object[] objArray : objList) {
			BigInteger no = (BigInteger) objArray[0];
			String name = (String) objArray[17];
			java.sql.Date submitDateSql = (java.sql.Date) objArray[19];
			LocalDate submitDate = submitDateSql.toLocalDate();
			String interviewDate = (String) objArray[26];
			String genderString = (String) objArray[24];
			String departmentName = (String) objArray[23];
			String positionName = (String) objArray[27];
			String dob = (String) objArray[2];
			String phone = (String) objArray[14];
			String education = (String) objArray[3];
			String email = (String) objArray[4];
			String techSkill = (String) objArray[21];
			String languageSkill = (String) objArray[14];
			String mainTech = (String) objArray[16];
			String exp = (String) objArray[6];
			String level = (String) objArray[15];
			String expectedSalary = (String) objArray[7];
			String combineStatus = (String) objArray[28];
			String interviewStage = (String) objArray[25];
			String candidateSpecialStatus = (String) objArray[29];

			CandidateSummaryDTO dto = new CandidateSummaryDTO(no, name, submitDate, interviewDate, genderString,
					departmentName, positionName, dob, phone, education, email, techSkill, languageSkill, mainTech, exp,
					level, expectedSalary, combineStatus, interviewStage, candidateSpecialStatus);
			list.add(dto);
		}

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

		JasperReport jasperReport = JasperCompileManager.compileReport(path);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		if (type.equalsIgnoreCase("pdf")) {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=Candidate-Summary.pdf");

			OutputStream out = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
			out.flush();
			out.close();
		} else if (type.equalsIgnoreCase("excel")) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=Candidate-Summary.xlsx");

			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setDetectCellType(true);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			exporter.setConfiguration(configuration);
			configuration.setWhitePageBackground(false);
			configuration.setCollapseRowSpan(false);
			configuration.setIgnoreGraphics(false);
			exporter.exportReport();
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid report type");
		}
		return ResponseEntity.ok("Report downloaded successfully.");
	}

	@GetMapping("/hr/download-candidate-summary-vacancy/{type}")
	public ResponseEntity<?> downloadCandidateSummaryByVacancy(@PathVariable("type") String type,
			HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "vacancyId", required = false) int vacancyIdParam

	) throws IOException, JRException {
		ClassPathResource resource = new ClassPathResource("Candidate_View_Summary.jrxml");
		String path = resource.getFile().getAbsolutePath();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ReportTitle", "Candidate Summary List");

		int vacancyId = (vacancyIdParam != 0) ? vacancyIdParam : 0;

		List<Object[]> objList = new ArrayList<Object[]>();

		objList = candidateRepository.allCandidateViewSummary(vacancyId, 0, 0, 0, null, null, null, null, null, null);
		if (objList == null || objList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data available for the selected criteria.");
		}

		List<CandidateSummaryDTO> list = new ArrayList<>();
		for (Object[] objArray : objList) {
			BigInteger no = (BigInteger) objArray[0];
			String name = (String) objArray[17];
			java.sql.Date submitDateSql = (java.sql.Date) objArray[19];
			LocalDate submitDate = submitDateSql.toLocalDate();
			String interviewDate = (String) objArray[26];
			String genderString = (String) objArray[24];
			String departmentName = (String) objArray[23];
			String positionName = (String) objArray[27];
			String dob = (String) objArray[2];
			String phone = (String) objArray[14];
			String education = (String) objArray[3];
			String email = (String) objArray[4];
			String techSkill = (String) objArray[21];
			String languageSkill = (String) objArray[14];
			String mainTech = (String) objArray[16];
			String exp = (String) objArray[6];
			String level = (String) objArray[15];
			String expectedSalary = (String) objArray[7];
			String combineStatus = (String) objArray[28];
			String interviewStage = (String) objArray[25];
			String candidateSpecialStatus = (String) objArray[29];

			CandidateSummaryDTO dto = new CandidateSummaryDTO(no, name, submitDate, interviewDate, genderString,
					departmentName, positionName, dob, phone, education, email, techSkill, languageSkill, mainTech, exp,
					level, expectedSalary, combineStatus, interviewStage, candidateSpecialStatus);
			list.add(dto);
		}

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
		String fileName = "Candidate-Summary-Vacancy-" + vacancyId;

		JasperReport jasperReport = JasperCompileManager.compileReport(path);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		if (type.equalsIgnoreCase("pdf")) {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");

			OutputStream out = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
			out.flush();
			out.close();
		} else if (type.equalsIgnoreCase("excel")) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setDetectCellType(true);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			exporter.setConfiguration(configuration);
			configuration.setWhitePageBackground(false);
			configuration.setCollapseRowSpan(false);
			configuration.setIgnoreGraphics(false);
			exporter.exportReport();
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid report type");
		}
		return ResponseEntity.ok("Report downloaded successfully.");
	}

}
