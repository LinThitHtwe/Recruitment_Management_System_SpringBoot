//counts at the top of dashboard start
//populate years
		const yearForCounts=document.getElementById('year');
        const currentYear = new Date().getFullYear();
        const minYearInput = document.getElementById('minYear');
        const minYear = parseInt(minYearInput.value);
        
          // Add the dynamically generated options
          if (minYear !== 0) {
        for (let year =minYear; year <= currentYear; year++) {
            const option = document.createElement('option');
            option.value = year.toString();
            option.textContent = year.toString();
            yearForCounts.appendChild(option);
        }
        }
        
        function getDashboardCounts(){
			const selectedYear=yearForCounts.value;
			
			fetch(`/department/dashboard-counts/${selectedYear}`)
			.then(response => response.json()) // Parse response as JSON
    		.then(data => {
        		document.getElementById("totalVacancies").textContent = data.vacancyCount;
        		document.getElementById("activeAndUrgentVacancies").textContent = data.activeVacancyCount+ '  (Urgent '+data.urgentVacancyCount+')';
        		document.getElementById("totalCandidates").textContent = data.candidateCount;
        		document.getElementById("employedCandidates").textContent = data.employedCandidateCount;
    })
    .catch(error => {
        console.error('Error fetching data:', error);
    });
}

yearForCounts.addEventListener('change', getDashboardCounts);
		
	//counts at the top of dashboard end


//use department name for all charts
const departmentName=document.getElementById('departmentName').value;

//filter vacancy count time start
//from 2020 to current year
   		const yearSelect1 = document.getElementById('yearSelect1');
        let VacanciesFilterByTime; 

        // Add the dynamically generated options
        if (minYear !== 0) {
        for (let year = minYear; year <= currentYear; year++) {
            const option = document.createElement('option');
            option.value = year.toString();
            option.textContent = year.toString();
            yearSelect1.appendChild(option);
        }
        }

    const yearSelectOption1 = document.getElementById('yearSelect1');
    const monthSelectOption1=document.getElementById('monthSelect1');


	function updateMonthSelectDisplay1() {
    const selectedYear = yearSelectOption1.value;
    const monthSelectDiv = document.getElementById('monthDiv1');

    if (selectedYear === '0') {
        // Hide the month select element when 'All' is selected in the year select
        monthSelectDiv.style.display = 'none';
        monthSelectOption1.value = '0';
    } else {
        // Show the month select element for other years
        monthSelectDiv.style.display = 'block';
    }
}

    

    // Function to create the bar chart
function createBarChart1(data) {
    const labels = Object.keys(data);
    const values = Object.values(data);

    if (VacanciesFilterByTime) {
        VacanciesFilterByTime.destroy();
    }

    // Create the bar chart using Chart.js
    VacanciesFilterByTime = new Chart('VacanciesFilterByTime', {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Vacancies',
                data: values,
                backgroundColor: '#99ccff',
                borderColor: '#99ccff',
                borderWidth: 1
            }]
        },
        options: {
            plugins: {
                title: {
                    display: true,
                    text: '', 
                }
            },
            responsive: true, 
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}


    // Function to update the bar chart based on select field values
function updateBarChart1() {
    // Get the selected values and convert them to integers
    const selectedYear = yearSelectOption1.value;
    const selectedMonth = monthSelectOption1.value;

    // Make the AJAX call to the controller to get the updated data
    fetch(`/department/vacancies-by-time/${selectedYear}/${selectedMonth}`)
        .then(response => response.json())
        .then(data => {
            // Call the function to create the bar chart with the updated data
            createBarChart1(data);

            // Update the title based on the selected options
            const year = selectedYear === '0' ? 'All' : selectedYear;
            const month = selectedMonth === '0' ? 'All' : monthSelectOption1.options[monthSelectOption1.selectedIndex].text;

            // Generate the chart title dynamically based on the selected values
            let chartTitle = `Vacancy count for "${departmentName}"`;
            if (selectedMonth !== '0') {
                chartTitle += ` for ${month}`;
                }

            if (selectedYear !== '0') {
                chartTitle += ` in ${year}`;
            }
			 // Set the title to the chart
            VacanciesFilterByTime.options.plugins.title.text = chartTitle;
            VacanciesFilterByTime.update();
        })
        .catch(error => console.error('Error fetching data:', error));
}

    
    updateMonthSelectDisplay1();

    // Add event listeners to the select fields
    yearSelectOption1.addEventListener('change', function() {
    updateMonthSelectDisplay1(); 
    updateBarChart1();
});

	monthSelectOption1.addEventListener('change',updateBarChart1);
	//filter vacancy count by time end
	
//position demand start
//Function to populate the years in the select fields
const startYearSelect = document.getElementById('startYearSelect1');
const endYearSelect = document.getElementById('endYearSelect1');
let lineChart;

// Add the dynamically generated options
if (minYear !== 0) {
for (let year = minYear; year <= currentYear; year++) {
    const startOption = document.createElement('option');
    const endOption = document.createElement('option');
    startOption.value = year.toString();
    endOption.value = year.toString();
    startOption.textContent = year.toString();
    endOption.textContent = year.toString();
    startYearSelect.appendChild(startOption);
    endYearSelect.appendChild(endOption);
}
}

function updateEndYearOptions() {
    const selectedStartYear = parseInt(startYearSelect.value);
    const selectedEndYear = endYearSelect.value ? parseInt(endYearSelect.value) : 0;

    // Check if the end year has been set before and is earlier than the current start year
    const hasEndYearBeenLessThanOrEqualWithStartYear = selectedEndYear !== 0 && selectedEndYear <= selectedStartYear;
    
    

    endYearSelect.innerHTML = '<option value="" disabled selected>Select End Year</option>';

    // Populate end year select from the year after the selected start year to the current year
    for (let year = selectedStartYear + 1; year <= currentYear; year++) {
        const option = document.createElement('option');
        option.value = year;
        option.textContent = year;
        endYearSelect.appendChild(option);
    }

    // Set the selected end year to the current year if it is not already selected or if the start year has been changed to a later year than the current selected end year
    if (selectedEndYear === 0 || hasEndYearBeenLessThanOrEqualWithStartYear) {
        endYearSelect.value = currentYear;
    } else {
        endYearSelect.value = selectedEndYear;
    }
    
    

    // Show or hide the end year select based on the selected start year
    const endYearPositionTrendLabel = document.getElementById('endYearPositionTrendLabel');
    if (selectedStartYear === currentYear) {
		endYearPositionTrendLabel.style.display = 'none';
        endYearSelect.style.display = 'none';
    } else {
		endYearPositionTrendLabel.style.display = 'inline';
        endYearSelect.style.display = 'inline';
    }
}




function updateLineChart() {
    const selectedStartYear = startYearSelect.value || '0';
    const selectedEndYear = endYearSelect.value || '0';
    
      // Update chart title based on selected years
    let chartTitle = `Position demand trend for ${departmentName}`;
    if (selectedStartYear !== '0') {
        if (selectedEndYear !== '0') {
            chartTitle += ` from ${selectedStartYear} to ${selectedEndYear}`;
        } else {
            chartTitle += ` in ${selectedStartYear}`;
        }
    }

    // Make the AJAX call to the controller to get the updated data
    fetch(`/department/position-demand/${selectedStartYear}/${selectedEndYear}`)
        .then(response => response.json())
        .then(data => {
            console.log("Fetched Data:", data);

            // Get the unique years across all data points
            const yearsSet = new Set();
            data.forEach(item => {
                yearsSet.add(item.year);
            });
            const labels = [...yearsSet].sort((a, b) => a - b); // Sort the years in ascending order
            console.log("Labels:", labels);

            // Group data points by positionName and year
            const groupedData = data.reduce((acc, item) => {
                const { positionName, year, count } = item;
                if (!acc[positionName]) {
                    acc[positionName] = {};
                }
                acc[positionName][year] = count;
                return acc;
            }, {});

            console.log("Grouped Data:", groupedData);

            // Create datasets for each position
            const datasets = [];
            for (const positionName in groupedData) {
                const positionData = groupedData[positionName];
                const countData = labels.map(year => positionData[year] || 0);
                datasets.push({
                    label: positionName,
                    data: countData,
                    borderColor: getRandomColor(),
                    backgroundColor: "rgba(0, 0, 0, 0)", // Transparent background
                    borderWidth: 2,
                    fill: false,
                });
            }
            console.log("Datasets:", datasets);

            // Destroy the existing chart (if any) before creating a new one
            if (lineChart) {
                lineChart.destroy();
            }

            lineChart = new Chart('PositionDemandThroughYears', {
                type: "line",
                data: {
                    labels: labels,
                    datasets: datasets,
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            display: true,
                            title: {
                                display: true,
                                text: "Years"
                            }
                        },
                        y: {
                            display: true,
                            title: {
                                display: true,
                                text: "Vacancy Required Post Count"
                            },
                            beginAtZero: true, // Ensure the y-axis starts from 0
                        }
                    },
                       plugins: {
                title: {
                    display: true,
                    text: chartTitle, 
                }
            }
                }
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}




function getRandomColor() {
    return `rgba(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, 1)`;
}


// Attach an event listener to the select fields to update the chart when the user selects different values
startYearSelect.addEventListener('change', function () {
    updateEndYearOptions();
    updateLineChart();
});

endYearSelect.addEventListener('change', updateLineChart);

//position demand end

//candidate count by vacancy status bar chart start
let CandidateCountByVacancyStatus;
const urgentStatusBox=document.getElementById('urgentStatus');

function createBarChart2(data, isUrgent) {
    const labels = Object.keys(data);
    const values = Object.values(data);

    if (CandidateCountByVacancyStatus) {
        CandidateCountByVacancyStatus.destroy();
    }

    let chartTitle;

        const titlePrefix = isUrgent ? 'Candidate count for urgent vacancies' : 'Candidate count for active vacancies';
        chartTitle = `${titlePrefix} in "${departmentName}"`;
    

    // Create the bar chart using Chart.js
    CandidateCountByVacancyStatus = new Chart('CandidateCountByVacancyStatus', {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Candidates',
                data: values,
                backgroundColor: '#99ccff',
                borderColor: '#99ccff',
                borderWidth: 1
            }]
        },
        options: {
            plugins: {
                title: {
                    display: true,
                    text: chartTitle,
                    padding: {
                        top: 10,
                        bottom: 30
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}




function updateBarChart2(isUrgent) {
    // Get the selected values and convert them to integers
    const urgentStatus = isUrgent ? '1' : '0';


    // Make the AJAX call to the controller to get the updated data
    fetch(`/department/candidates-by-vacancy-status/${urgentStatus}`)
        .then(response => response.json())
        .then(data => {
            // Call the function to create the bar chart with the updated data and title
            createBarChart2(data, isUrgent);
        })
        .catch(error => console.error('Error fetching data:', error));
}



urgentStatusBox.addEventListener('change', function () {
    const isUrgent = urgentStatusBox.checked;

    // Call the function to update the chart with the updated data and title
    updateBarChart2(isUrgent);
});

//candidate count by vacancy status bar chart end

//candidate count by time bar chart start
		const yearSelect2 = document.getElementById('yearSelect2');
        let candidateCountByTime; 

        // Add the dynamically generated options
        if (minYear !== 0) {
        for (let year = minYear; year <= currentYear; year++) {
            const option = document.createElement('option');
            option.value = year.toString();
            option.textContent = year.toString();
            yearSelect2.appendChild(option);
        }
        }
        
        // Get year select elements
    const yearSelectOption2 = document.getElementById('yearSelect2');
    const monthSelectOption2=document.getElementById('monthSelect2');


	function updateMonthSelectDisplay2() {
    const selectedYear = yearSelectOption2.value;
    const monthSelectDiv = document.getElementById('monthDiv2');

    if (selectedYear === '0') {
        // Hide the month select element when 'All' is selected in the year select
        monthSelectDiv.style.display = 'none';
        monthSelectOption2.value = '0';
    } else {
        // Show the month select element for other years
        monthSelectDiv.style.display = 'block';
    }
}

    

    // Function to create the bar chart
function createBarChart3(data) {
    const labels = Object.keys(data);
    const values = Object.values(data);

    if (candidateCountByTime) {
        candidateCountByTime.destroy();
    }


    // Create the bar chart using Chart.js
    candidateCountByTime = new Chart('candidateCountByTime', {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Candidates',
                data: values,
                backgroundColor: '#99ccff',
                borderColor: '#99ccff',
                borderWidth: 1
            }]
        },
           options: {
            plugins: {
                title: {
                    display: true,
                    text: '', 
                }
            },
              responsive: true, 
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}


    // Function to update the bar chart based on select field values
    function updateBarChart3() {
        // Get the selected values and convert them to integers
        const selectedYear = yearSelectOption2.value;
        const selectedMonth=monthSelectOption2.value;

        // Make the AJAX call to the controller to get the updated data
        fetch(`/department/candidates-by-time/${selectedYear}/${selectedMonth}`)
            .then(response => response.json())
            .then(data => {
                // Call the function to create the bar chart with the updated data
                
                createBarChart3(data);
                
                // Update the title based on the selected options
             // Update the title based on the selected options
            const year = selectedYear === '0' ? 'All' : selectedYear;
            const month = selectedMonth === '0' ? 'All' : monthSelectOption2.options[monthSelectOption2.selectedIndex].text;

            // Generate the chart title dynamically based on the selected values
            let chartTitle = `Candidate count for ${departmentName}`;
            
                if (selectedMonth !== '0') {
                chartTitle += ` for ${month}`;
            }
            
               if (selectedYear !== '0') {
                chartTitle += ` in ${year}`;
            }
            
            // Set the title to the chart
            candidateCountByTime.options.plugins.title.text = chartTitle;
            candidateCountByTime.update();
            })
            .catch(error => console.error('Error fetching data:', error));
    }
    
    updateMonthSelectDisplay2();

    // Add event listeners to the select fields
    yearSelectOption2.addEventListener('change', function() {
    updateMonthSelectDisplay2(); 
    updateBarChart3();
});

	monthSelectOption2.addEventListener('change',updateBarChart3);
//candidate count by time bar chart end

//candidate count per position line chart start
// Function to populate the years in the select fields
const startYearSelect2 = document.getElementById('startYearSelect2');
const endYearSelect2 = document.getElementById('endYearSelect2');
let lineChart2;

// Add the dynamically generated options
if (minYear !== 0) {
for (let year = minYear; year <= currentYear; year++) {
    const startOption = document.createElement('option');
    const endOption = document.createElement('option');
    startOption.value = year.toString();
    endOption.value = year.toString();
    startOption.textContent = year.toString();
    endOption.textContent = year.toString();
    startYearSelect2.appendChild(startOption);
    endYearSelect2.appendChild(endOption);
}
}

function updateEndYearOptions2() {
    const selectedStartYear = parseInt(startYearSelect2.value);
    const selectedEndYear = endYearSelect2.value ? parseInt(endYearSelect2.value) : 0;

    // Check if the end year has been set before and is earlier than the current start year
    const hasEndYearBeenLessThanOrEqualWithStartYear = selectedEndYear !== 0 && selectedEndYear <= selectedStartYear;

    endYearSelect2.innerHTML = '<option value="" disabled selected>Select End Year</option>';

    // Populate end year select from the year after the selected start year to the current year
    for (let year = selectedStartYear + 1; year <= currentYear; year++) {
        const option = document.createElement('option');
        option.value = year;
        option.textContent = year;
        endYearSelect2.appendChild(option);
    }

    // Set the selected end year to the current year if it is not already selected or if the start year has been changed to a later year than the current selected end year
    if (selectedEndYear === 0 || hasEndYearBeenLessThanOrEqualWithStartYear) {
        endYearSelect2.value = currentYear;
    } else {
        endYearSelect2.value = selectedEndYear;
    }
    
       // Show or hide the end year select based on the selected start year
    const endYearCandidateTrendLabel = document.getElementById('endYearCandidateTrendLabel');
    if (selectedStartYear === currentYear) {
		endYearCandidateTrendLabel.style.display = 'none';
        endYearSelect2.style.display = 'none';
    } else {
		endYearCandidateTrendLabel.style.display = 'inline';
        endYearSelect2.style.display = 'inline';
    }
}



function updateLineChart2() {
    const selectedStartYear = startYearSelect2.value || '0';
    const selectedEndYear = endYearSelect2.value || '0';
    
         // Update chart title based on selected years
    let chartTitle = `Candidate trend for ${departmentName}`;
    if (selectedStartYear !== '0') {
        if (selectedEndYear !== '0') {
            chartTitle += ` from ${selectedStartYear} to ${selectedEndYear}`;
        } else {
            chartTitle += ` in ${selectedStartYear}`;
        }
    }

    // Make the AJAX call to the controller to get the updated data
    fetch(`/department/candidate-position-trend/${selectedStartYear}/${selectedEndYear}`)
        .then(response => response.json())
        .then(data => {

            // Get the unique years across all data points
            const yearsSet = new Set();
            data.forEach(item => {
                yearsSet.add(item.year);
            });
            const labels = [...yearsSet].sort((a, b) => a - b); // Sort the years in ascending order
            console.log("Labels:", labels);

            // Group data points by positionName and year
            const groupedData = data.reduce((acc, item) => {
                const { positionName, year, count } = item;
                if (!acc[positionName]) {
                    acc[positionName] = {};
                }
                acc[positionName][year] = count;
                return acc;
            }, {});

            console.log("Grouped Data:", groupedData);

            // Create datasets for each position
            const datasets = [];
            for (const positionName in groupedData) {
                const positionData = groupedData[positionName];
                const countData = labels.map(year => positionData[year] || 0);
                datasets.push({
                    label: positionName,
                    data: countData,
                    borderColor: getRandomColor(),
                    backgroundColor: "rgba(0, 0, 0, 0)", // Transparent background
                    borderWidth: 2,
                    fill: false,
                });
            }
            console.log("Datasets:", datasets);

            // Destroy the existing chart (if any) before creating a new one
            if (lineChart2) {
                lineChart2.destroy();
            }

            lineChart2 = new Chart('CandidatePerPositionLineChart', {
                type: "line",
                data: {
                    labels: labels,
                    datasets: datasets,
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            display: true,
                            title: {
                                display: true,
                                text: "Years"
                            }
                        },
                        y: {
                            display: true,
                            title: {
                                display: true,
                                text: "Candidate Count"
                            },
                            beginAtZero: true, // Ensure the y-axis starts from 0
                        }
                    },
                       plugins: {
                title: {
                    display: true,
                    text: chartTitle, // Set the dynamic chart title
                }
            }
                }
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}

// Attach an event listener to the select fields to update the chart when the user selects different values
startYearSelect2.addEventListener('change', function () {
    updateEndYearOptions2();
    updateLineChart2();
});

endYearSelect2.addEventListener('change', updateLineChart2);
//candidate count per position line chart end

//doughnut chart for candidates per status start
const positionSelectOption = document.getElementById('positionSelect');
const vacancySelectOption=document.getElementById('vacancySelect');
const noDataMsg=document.getElementById('doughnutChartNoDataMessage');
let CandidatesPerStatusDoughnutChart;

function updateVacancyOptions() {
    const selectedPositionId = positionSelectOption.value;

    if (selectedPositionId === '0') {
        // If 'All' position is selected, disable and reset the vacancy dropdown
        vacancySelectOption.innerHTML = ''; // Clear all options
        const defaultOption = document.createElement('option');
        defaultOption.value = '0';
        defaultOption.text = 'All';
        defaultOption.selected = true;
        vacancySelectOption.appendChild(defaultOption);

        vacancySelectOption.disabled = true;
        updateDoughnutChart();
    } else {
        vacancySelectOption.disabled = false;
        fetch(`/department/vacancies-by-position/${selectedPositionId}`)
            .then(response => response.json())
            .then(data => {
                // Clear previous options
                vacancySelectOption.innerHTML = '';
                
                if (data.length === 0) {
                    // Add a disabled option indicating no vacancy
                    const noVacancyOption = document.createElement('option');
                    noVacancyOption.value = '';
                    noVacancyOption.text = 'No vacancy for this position';
                    noVacancyOption.disabled = true;
                    noVacancyOption.selected = true;
                    vacancySelectOption.appendChild(noVacancyOption);
                } else {

                // Add the default disabled option
                const defaultOption = document.createElement('option');
                defaultOption.value = '';
                defaultOption.text = 'Select Vacancy';
                defaultOption.disabled = true;
                defaultOption.selected = true;
                vacancySelectOption.appendChild(defaultOption);

                // Populate options based on the response data
                data.forEach(vacancyData => {
                    const vacancyId = Object.keys(vacancyData)[0];
                    const vacancyName = Object.values(vacancyData)[0];
                    const option = document.createElement('option');
                    option.value = vacancyId;
                    option.text = vacancyName;
                    vacancySelectOption.appendChild(option);
                });
                }
            })
          
            .catch(error => console.error('Error fetching vacancies:', error));
    }
}



function createDoughnutChart(data, vacancyName) {
    const labels = Object.keys(data);
    const values = Object.values(data);
    const total = values.reduce((acc, value) => acc + value, 0);

    // Destroy the existing chart (if any)
    if (CandidatesPerStatusDoughnutChart) {
        CandidatesPerStatusDoughnutChart.destroy();
    }

    // Determine the title based on vacancyName and positionSelectOption2 value
    let title;
    if (positionSelectOption.value === '0' && vacancySelectOption.value === '0') {
        title = `Candidate Percentages Per Status for all vacancies in "${departmentName}"`;
    } else {
        title = vacancyName ? `Candidate Percentages Per Status for "${vacancyName}" in "${departmentName}"` : `Candidate Percentages Per Status "${departmentName}"`;
    }

    // Create the doughnut chart
    CandidatesPerStatusDoughnutChart = new Chart('CandidatesPerStatusDoughnutChart', {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: values,
                backgroundColor: labels.map(() => getRandomColor()), // Generate random colors for each label
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: title,
                },
                tooltip: {
                    callbacks: {
                        label: (context) => {
                            const value = context.parsed;
                            const percentage = ((value / total) * 100).toFixed(2);
                            return `${value} (${percentage}%)`;
                        }
                    }
                }
            },
           
                  afterDraw: (chart, args, options) => {
                if (Object.keys(data).length > 0) {
                    noDataMsg.style.display = 'none';
                }
            }
        }
    });
}


function updateDoughnutChart() {
    const selectedVacancyId = vacancySelectOption.value;

    fetch(`/department/candidates-by-status/${selectedVacancyId}`)
        .then(response => response.json())
        .then(data => {
            // Process the data and create the pie chart or display the message
            if (Object.keys(data).length > 0) {
                // Data is available, create the pie chart
                noDataMsg.style.display = 'none'; 
                createDoughnutChart(data, vacancySelectOption.options[vacancySelectOption.selectedIndex].text); // Pass both data and vacancyName to create the pie chart
            } else {
                if (CandidatesPerStatusDoughnutChart) {
                    CandidatesPerStatusDoughnutChart.destroy();
                }
               noDataMsg.style.display = 'block';
               noDataMsg.innerHTML = '<p>There is no candidate for this vacancy.</p>';
            }
        })
        .catch(error => console.error('Error fetching candidate data:', error));
}

positionSelectOption.addEventListener('change', updateVacancyOptions);
vacancySelectOption.addEventListener('change', updateDoughnutChart);
//doughnut chart for candidates per status end


document.addEventListener("DOMContentLoaded", function() {
	   positionSelectOption.value = '0';

    // Trigger the change event to populate the vacancy dropdown for 'All' position
    const changeEvent = new Event('change');
    positionSelectOption.dispatchEvent(changeEvent);
	getDashboardCounts();
    updateBarChart1();
    updateLineChart();
    updateBarChart2();
	updateBarChart3()
     updateLineChart2();
     updateDoughnutChart();
     
 
});