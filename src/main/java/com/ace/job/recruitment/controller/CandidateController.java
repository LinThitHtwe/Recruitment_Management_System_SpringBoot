package com.ace.job.recruitment.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.CandidateStatus;
import com.ace.job.recruitment.entity.Status;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.CandidateService;
import com.ace.job.recruitment.service.CandidateStatusService;
import com.ace.job.recruitment.service.StatusService;

@Controller
public class CandidateController {

	@Autowired
	CandidateService candidateServices;

	@Autowired
	VacancyRepository vacancyRepository;

	@Autowired
	StatusService statusService;

	@Autowired
	CandidateStatusService candidateStatusService;

	@GetMapping("/candidate/check-candidate-duplication")
	public ResponseEntity<String> checkCandidateDuplication(@RequestParam("email") String email,
			@RequestParam("vacancyId") long vacancyId) {
		boolean isEmailValid = isValidEmailFormat(email);
		boolean isDuplicate = false;
		String responseMessage = "";

		if (isEmailValid) {
			List<Candidate> candidateList = candidateServices.checkCandidateDuplication(email, vacancyId);
			isDuplicate = !candidateList.isEmpty();
		} else {
			responseMessage = "Invalid email format.";
		}

		if (isDuplicate) {
			responseMessage = "Email is already in use for applying this vacancy.";
		}

		if (isDuplicate || !isEmailValid) {
			return ResponseEntity.badRequest().body(responseMessage);
		} else {
			return ResponseEntity.ok("Email is valid and not a duplicate.");
		}
	}

	private boolean isValidEmailFormat(String email) {
		// Regular expression pattern for basic email format validation
		String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

		return email.matches(emailPattern);
	}

	@RequestMapping("/candidate/register")
	public String register(@RequestParam("name") String name, @RequestParam("dob") String dob,
			@RequestParam("email") String email, @RequestParam("gender") boolean gender,
			@RequestParam("phone") String phone, @RequestParam("education") String education,
			@RequestParam("techSkill") String techSkill, @RequestParam("languageSkill") String languageSkill,
			@RequestParam("mainTech") String mainTech, @RequestParam("exp") String exp,
			@RequestParam("level") String level, @RequestParam("expectedSalary") String expectedSalary,
			@RequestParam("file") MultipartFile file, @RequestParam("vacancyId") long id, Model model)
			throws IOException {

		Vacancy vacancy = vacancyRepository.findById(id).orElse(null);

		Candidate candidate = new Candidate();
		candidate.setName(name);
		candidate.setDob(dob);
		candidate.setEducation(education);
		candidate.setEmail(email);
		candidate.setExp(exp);
		candidate.setExpectedSalary(expectedSalary);
		candidate.setGender(gender);
		candidate.setLanguageSkill(languageSkill);
		candidate.setLevel(level);
		candidate.setMainTech(mainTech);
		candidate.setTechSkill(techSkill);
		candidate.setPhone(phone);
		candidate.setVacancy(vacancy);
		candidate.setSubmitDate(LocalDate.now());
		candidate.setSubmitTime(LocalTime.now());

		// Format the current date and time as a string
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		if (isWordFile(file) || isPdfFile(file)) {
			byte[] fileContent = file.getBytes();
			candidate.setFile(fileContent);
			candidate.setFiletype(file.getContentType());

			// Save the candidate to get its ID before creating CandidateStatus
			candidateServices.addCandidate(candidate);

			// Create CandidateStatus and set the date and status ID
			CandidateStatus candidateStatus = new CandidateStatus();
			candidateStatus.setDate(currentDateTime.format(formatter));
			Status status = statusService.getStatusById(1);
			candidateStatus.setStatus(status);
			candidateStatus.setCandidate(candidate);

			// Save the CandidateStatus
			candidateStatusService.saveCandidateStatus(candidateStatus);

			model.addAttribute("alertMessage", "This is a successful alert message!");
			model.addAttribute("alertType", "success");
		}

		String redirectLink = "redirect:/candidate/vacancy-info/" + id + "?success";
		return redirectLink;
	}

	// _______________________________________________for word file
	// save_____________________________________________________
	private boolean isWordFile(MultipartFile file) {
		return file.getContentType().equals("application/msword") || file.getContentType()
				.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	}

	// ____________________________________________for pdf file
	// save_________________________________________________________
	private boolean isPdfFile(MultipartFile file) {
		return file.getContentType().equals("application/pdf");
	}

//______________________________________________DOWNLOAD CV__________________________________________________________________
	@RequestMapping("/candidate/downloadCv")
	public ResponseEntity<StreamingResponseBody> downloadFiles(@RequestParam("id") List<Long> ids) {
		System.out.println(ids);
		List<Candidate> fileEntities = (List<Candidate>) candidateServices.getCandidates(ids);

		if (!fileEntities.isEmpty()) {
			if (fileEntities.size() == 1) {
				// only one candidate, return the file directly without creating a zip
				Candidate fileEntity = fileEntities.get(0);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentDispositionFormData("attachment",
						fileEntity.getName() + "-cv." + getFileExtension(fileEntity.getFiletype()));
				headers.setContentType(MediaType.parseMediaType(fileEntity.getFiletype()));

				return ResponseEntity.ok().headers(headers)
						.body(outputStream -> outputStream.write(fileEntity.getFile()));
			} else {
				// multiple candidates, create a zip file
				StreamingResponseBody responseBody = outputStream -> {
					try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
						for (Candidate fileEntity : fileEntities) {
							byte[] fileContent = fileEntity.getFile();
							String fileName = generateUniqueFileName(fileEntity.getName() + "-cv.",
									fileEntity.getFiletype());

							ZipEntry zipEntry = new ZipEntry(fileName);
							zos.putNextEntry(zipEntry);
							zos.write(fileContent);
							zos.closeEntry();
						}

						zos.finish();
					} catch (IOException e) {
						throw new RuntimeException("Error while streaming file", e);
					}
				};

				HttpHeaders headers = new HttpHeaders();
				headers.setContentDispositionFormData("attachment", "candidate.zip");
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
			}
		}

		return ResponseEntity.notFound().build();
	}

	private String generateUniqueFileName(String originalFileName, String fileType) {
		// Appending a unique identifier (UUID) to the original file name to avoid
		// duplicates
		String uniqueIdentifier = UUID.randomUUID().toString().substring(0, 8);
		return originalFileName + "_" + uniqueIdentifier + "." + getFileExtension(fileType);
	}

	private String getFileExtension(String contentType) {
		if (contentType != null) {
			if (contentType.equals("application/msword")) {
				return "doc";
			} else if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
				return "docx";
			} else if (contentType.equals("application/pdf")) {
				return "pdf";
			}
		}
		return "txt"; // Default to txt if the content type is unknown
	}

}