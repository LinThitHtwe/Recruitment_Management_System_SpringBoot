package com.ace.job.recruitment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.VacancyService;

@Controller
public class HomePageController {

	@Autowired
	private VacancyService vacancyService;

	@Autowired
	private VacancyRepository vacancyRepository;

	@ModelAttribute("/vacancyList")
	public List<Vacancy> getVacancyListByUrgent() {
		List<Vacancy> list = vacancyService.getUrgentVacancy();

		if (list == null || list.isEmpty()) {
			List<Vacancy> allList = vacancyRepository.findAllByActiveTrue();
			list = new ArrayList<>();

			if (allList.size() > 6) {
				list = allList.subList(0, 6);
			} else {
				list.addAll(allList);
			}
		}
		System.out.print("____________" + list.size() + "____________");

		return list;
	}

	@GetMapping("/")
	public String showHomePage(Model model) {
		List<Vacancy> list = vacancyService.getUrgentVacancy();

		if (list.size() < 7) {
			List<Vacancy> nonUrgentList = vacancyRepository.findAllByActiveTrueAndUrgentFalse();
			list.addAll(nonUrgentList);
		}

		if (list == null || list.isEmpty()) {
			List<Vacancy> allList = vacancyRepository.findAllByActiveTrue();
			list = new ArrayList<>();

			if (allList.size() > 6) {
				list = allList.subList(0, 6);
			} else {
				list.addAll(allList);
			}
		}

		model.addAttribute("vacancyList", list.subList(0, Math.min(list.size(), 6)));
		return "candidate/home";
	}
}
