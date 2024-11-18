google.charts.load('current', { 'packages': ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = [['Quizzes', 'Cantidad']];
    var quizzes = document.querySelectorAll('.quizz');
    quizzes.forEach(function(element) {
        var name = element.getAttribute('data-name');
        var value = parseInt(element.value);
        data.push([name, value]);
    });

    var dataTable = google.visualization.arrayToDataTable(data);

    var options = {
        title: '', 
        backgroundColor: '#f0f8ff',
        is3D: true, 
        legend: { position: 'bottom' }, 
        tooltip: { showColorCode: true, isHtml: true }, 
        pieSliceTextStyle: { fontSize: 14 }, 
        chartArea: {
            left: '10%',
            top: '10%',
            width: '80%',
            height: '80%',
        }, 
        responsive: true,
    };

    var chart = new google.visualization.PieChart(document.getElementById('piechart'));
    chart.draw(dataTable, options);
}

window.addEventListener('resize', drawChart);